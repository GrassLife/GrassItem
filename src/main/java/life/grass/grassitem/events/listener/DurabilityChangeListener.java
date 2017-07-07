package life.grass.grassitem.events.listener;

import life.grass.grassitem.JsonHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by ecila on 2017/07/06.
 */
public class DurabilityChangeListener implements Listener {
    @EventHandler
    public void onDurabilityChange(PlayerItemDamageEvent e) {
        ItemStack item = e.getItem();
        ItemStack damagedItem = JsonHandler.damageItem(item);
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if(inventory.getItemInMainHand().equals(item)) inventory.setItemInMainHand(damagedItem);
        if(inventory.getItemInOffHand().equals(item)) inventory.setItemInOffHand(damagedItem);
        if(inventory.getHelmet() != null && inventory.getHelmet().equals(item)) inventory.setHelmet(damagedItem);
        if(inventory.getChestplate() != null && inventory.getChestplate().equals(item)) inventory.setChestplate(damagedItem);
        if(inventory.getLeggings() != null && inventory.getLeggings().equals(item)) inventory.setLeggings(damagedItem);
        if(inventory.getBoots() != null && inventory.getBoots().equals(item)) inventory.setBoots(damagedItem);
        e.getPlayer().updateInventory();
        e.setCancelled(true);
    }
}
