package life.grass.grassitem;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ItemPacketRewriter {
    private static ItemPacketRewriter instance;

    static {
        instance = new ItemPacketRewriter();
    }

    private ItemPacketRewriter() {
    }

    public static ItemPacketRewriter getInstance() {
        return instance;
    }


    public void addListener(ProtocolManager manager, JavaPlugin plugin) {
        manager.addPacketListener(new PacketAdapter(plugin,
                ListenerPriority.LOW,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Server.WINDOW_ITEMS) {

            @Override
            public void onPacketSending(PacketEvent event) {
                PacketType type = event.getPacketType();
                if (type.equals(PacketType.Play.Server.SET_SLOT)) {
                    for (ItemStack item : new ItemStack[]{event.getPacket().getItemModifier().read(0)}) {
                        rewriteItem(item);
                    }
                } else if (type.equals(PacketType.Play.Server.WINDOW_ITEMS)) {
                    for (ItemStack item : event.getPacket().getItemListModifier().read(0)) {
                        rewriteItem(item);
                    }
                }
            }
        });
    }

    private void rewriteItem(ItemStack item) {
        if (item == null) return;
        GrassJson json = JsonHandler.getGrassJson(item);

        if (json == null) return;
        item.setType(json.getMaterial());

        item.setDurability(json.getMeta());

        if (json.hasDynamicValueInItem("CustomMaterial"))
            item.setType(Material.valueOf(json.getDynamicValue("CustomMaterial").getAsOverwritedString().orElse("COBBLESTONE")));

        ItemMeta meta = item.getItemMeta();


        String name = ChatColor.WHITE + "";

        // 腐った設定
        if (json.hasDynamicValue("ExpireDate") &&
                LocalDateTime.parse(json.getDynamicValue("ExpireDate").getAsOverwritedString().orElse(LocalDateTime.now().minusSeconds(1).toString()))
                        .isBefore(LocalDateTime.now())) {
            name += "腐った ";
        }

        // Enchantの設定
        if (json.hasDynamicValue("Enchant/Prefix")) {
            String title = "";
            if (JsonBucket.getInstance().findEnchantJson(json.getDynamicValue("Enchant/Prefix").getAsOverwritedString().orElse("")).isPresent()) {
                title = JsonBucket.getInstance().findEnchantJson(json.getDynamicValue("Enchant/Prefix").getAsOverwritedString().orElse("")).get().get("DisplayName").getAsString();
            }
            name += title + " ";
        }
        // 名前の設定
        name += json.getDisplayName()
                + (json.hasDynamicValueInItem("CustomDisplayName") ?
                " / " + json.getDynamicValue("CustomDisplayName").getAsOverwritedString().orElse("") : "");
        name = name.replaceAll("%name%", json.getDisplayName());
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + json.getDescription());

        lore.add("");
        // 食材周りの設定
        if (json.hasDynamicValue("Calorie")) {
            String cuisine = ChatColor.GRAY + json.getDynamicValue("Calorie").getAsMaskedInteger().orElse(0).toString() + "kcal";
            if (json.hasDynamicValue("Weight"))
                cuisine += " / " + json.getDynamicValue("Weight").getAsMaskedInteger().orElse(0).toString() + "g";
            lore.add(cuisine);
        }

        if (json.hasDynamicValue("ExpireDate")) {
            LocalDateTime expireDate = LocalDateTime.parse(json.getDynamicValue("ExpireDate").getAsOverwritedString().orElse(LocalDateTime.now().toString()));
            expireDate = expireDate.minusMinutes(expireDate.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
            lore.add(ChatColor.DARK_GRAY + "消費期限: " + expireDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        }

        if (json.hasItemTag("Ingredient"))
            lore.add(ChatColor.DARK_GRAY + "調理可能アイテム");

        // 食材効果の設定
        if (json.hasDynamicValue("FoodEffect/HEAVY_STOMACH")) {
            lore.add(ChatColor.BLUE + "効果");
            lore.add(ChatColor.BLUE + "胃もたれ Lv" + json.getDynamicValue("FoodEffect/HEAVY_STOMACH").getAsMaskedInteger());
        }


        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}