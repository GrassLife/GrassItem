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

        meta.setDisplayName(ChatColor.GRAY + json.getDisplayName() + (json.hasDynamicValueInItem("CustomDisplayName") ?
                " / " + json.getDynamicValue("CustomDisplayName").getAsOverwritedString().orElse("") : ""));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + json.getDescription());

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}