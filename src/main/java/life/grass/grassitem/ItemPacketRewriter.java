package life.grass.grassitem;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

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

        if (json.hasDynamicValueInItem("CustomMaterial"))
            item.setType(Material.valueOf(json.getDynamicValue("CustomMaterial").getAsOverwritedString().orElse("COBBLESTONE")));

        ItemMeta meta = item.getItemMeta();

        // Enchantの設定
        String name = ChatColor.GRAY + "";
        if(json.hasDynamicValue("Enchant/Prefix")) {
            String title = JsonBucket.getInstance().findEnchantJson(json.getDynamicValue("Enchant/Prefix").getAsOverwritedString().get()).get().get("DisplayName").getAsString();
            if(title != null) name += title + " ";
        }
        // 名前の設定
        name += json.getDisplayName()
                + (json.hasDynamicValueInItem("CustomDisplayName") ?
                " / " + json.getDynamicValue("CustomDisplayName").getAsOverwritedString().orElse("") : "");
        name = name.replaceAll("%name%", json.getDisplayName());
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + json.getDescription());

        // 食材周りの設定
        if(json.hasDynamicValue("ExpireDate")) lore.add(ChatColor.GRAY + "消費期限: " + json.getDynamicValue("ExpireDate").getAsOverwritedString().orElse("-"));
        if(json.hasDynamicValue("Calorie") && json.hasDynamicValue("Weight"))
            lore.add(ChatColor.GRAY + json.getDynamicValue("Calorie").getAsMaskedInteger().orElse(0).toString() + "kcal / " + json.getDynamicValue("Weight").getAsMaskedInteger().orElse(0).toString() + "g");

        // 食材効果の設定
        if(json.hasDynamicValue("FoodEffect/HEAVY_STOMACH")) {
            lore.add(ChatColor.BLUE + "効果");
            lore.add(ChatColor.BLUE + "胃もたれ Lv" + json.getDynamicValue("FoodEffect/HEAVY_STOMACH").getAsMaskedInteger());
        }



        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}