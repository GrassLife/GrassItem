package life.grass.grassitem.events.listener;

import life.grass.grassitem.GrassItem;
import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by ecila on 2017/07/08.
 */
public class PlayerDeathListener implements Listener {
    private static String ENCHANT_SPECIAL = "Enchant/Special";

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        for(ItemStack item: player.getInventory().getContents()) {
            if(item == null) continue;
            GrassJson json = JsonHandler.getGrassJson(item);
            if(json == null || !json.hasDynamicValue(ENCHANT_SPECIAL)) continue;
            if(json.getDynamicValue(ENCHANT_SPECIAL).getAsMaskedString().orElse("").equals("Blessed")) {
                e.getDrops().remove(item);
                item = JsonHandler.removeDynamicData(item, ENCHANT_SPECIAL);
                ItemStack finalItem = item;
                Bukkit.getScheduler().scheduleSyncDelayedTask(GrassItem.getInstance(), () -> player.getInventory().addItem(finalItem));
            }
        }
    }
}
