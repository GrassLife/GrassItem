package life.grass.grassitem;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.JsonObject;
import life.grass.grassitem.events.ItemRewriteEvent;
import life.grass.grassitem.events.RewriteType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
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

        if (json == null || json.getDynamicValue("Ignore").getAsMaskedInteger().orElse(0) == 1) return;
        GrassJsonReader jsonReader = json.getJsonReader();
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
        for(JsonObject enchant: json.getEnchantList()) {
            name += enchant.get("DisplayName").getAsString() + " ";
        }

        // 名前の設定
        name += (json.hasDynamicValueInItem("CustomDisplayName") ? json.getDynamicValue("CustomDisplayName").getAsOverwritedString().orElse("") : json.getDisplayName());
        name = name.replaceAll("%name%", json.getDisplayName());
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + json.getDescription());

        // Loreの設定
        for(RewriteType type: RewriteType.values()) {
            ItemRewriteEvent event = new ItemRewriteEvent(type, json);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if(event.isShowable()) lore.addAll(event.getLore());
        }

        if(json.hasItemTag("Tool")) {
            StringBuilder builder = new StringBuilder();
            item.setDurability((short) ((item.getData().getItemType().getMaxDurability()) - (item.getData().getItemType().getMaxDurability() * jsonReader.getDurabilityRate())));
            lore.add("");
            for(int i = 0; i < 10; i++) {
                builder.append(getEffectBarElement(json.getDynamicValue("EffectBar/" + i).getAsMaskedDouble().orElse(0.0), i == jsonReader.getDurabilityRateIndex()));
            }
            lore.add(ChatColor.GRAY + "性能: " + ChatColor.WHITE + "[" + builder.toString() + ChatColor.WHITE + "]");
            lore.add(ChatColor.GRAY + "耐久性: " + json.getDynamicValue("MaxDurability").getAsMaskedInteger().orElse(0));
            lore.add(ChatColor.GRAY + "丈夫さ: " + json.getDynamicValue("Toughness").getAsMaskedInteger().orElse(100));
        }

        if(json.hasDynamicValue("GatheringPower")) {
            lore.add(ChatColor.GRAY + "採取力: " + json.getDynamicValue("GatheringPower").getAsMaskedInteger().orElse(0));
        }

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    private static String GAGE = "▍";
    private static String CURRENT = "▋";

    public static String getEffectBarElement(double effect, boolean current) {
        if(effect <= 0.0) {
            return ChatColor.DARK_GRAY + (current ? CURRENT : GAGE);
        } else if(effect <= 0.2) {
            return ChatColor.DARK_RED + (current ? CURRENT : GAGE);
        } else if(effect <= 0.4) {
            return ChatColor.RED + (current ? CURRENT : GAGE);
        } else if(effect <= 0.6) {
            return ChatColor.GOLD + (current ? CURRENT : GAGE);
        } else if(effect <= 0.8) {
            return ChatColor.YELLOW + (current ? CURRENT : GAGE);
        } else if(effect <= 1.0) {
            return ChatColor.GREEN + (current ? CURRENT : GAGE);
        } else if(effect <= 1.2) {
            return ChatColor.DARK_AQUA + (current ? CURRENT : GAGE);
        } else if(effect <= 1.4) {
            return ChatColor.BLUE + (current ? CURRENT : GAGE);
        } else if(effect <= 1.6) {
            return ChatColor.WHITE + (current ? CURRENT : GAGE);
        } else if(effect <= 1.8) {
            return ChatColor.LIGHT_PURPLE + (current ? CURRENT : GAGE);
        } else {
            return ChatColor.WHITE + (current ? CURRENT : GAGE);
        }
    }
}