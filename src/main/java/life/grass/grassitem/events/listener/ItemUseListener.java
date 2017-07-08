package life.grass.grassitem.events.listener;

import life.grass.grassitem.GrassJson;
import life.grass.grassitem.JsonHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by ecila on 2017/07/09.
 */
public class ItemUseListener implements Listener {
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if(!e.hasItem()) return;
            Player player = e.getPlayer();

            ItemStack item = e.getItem();
            GrassJson json = JsonHandler.getGrassJson(item);
            ItemStack target = player.getInventory().getItemInOffHand();
            if(json == null || target == null) return;
            GrassJson targetJson = JsonHandler.getGrassJson(target);
            if(targetJson == null) return;

            if(json.hasItemTag("BlessedPotion") && !targetJson.hasDynamicValue("Enchant/Special") && targetJson.hasItemTag("Blessable")) {
                consume(item);
                player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1.0f, 1.0f);
                player.getWorld().spawnParticle(
                        Particle.FIREWORKS_SPARK,
                        player.getEyeLocation(),
                        20,
                        0.25,
                        0.25,
                        0.25,
                        0);
                target = JsonHandler.putDynamicData(target, "Enchant/Special", "Blessed");
                player.getInventory().setItemInOffHand(target);
                player.updateInventory();
                e.setCancelled(true);
            }
        }
    }

    private void consume(ItemStack item) {
        item.setAmount(item.getAmount() - 1);
    }
}
