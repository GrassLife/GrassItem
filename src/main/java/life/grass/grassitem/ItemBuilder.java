package life.grass.grassitem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by ecila on 2017/06/24.
 */
public class ItemBuilder {
    public static ItemStack buildByUniqueName(String uniqueName) {
        if(uniqueName.startsWith("Vanilla")) {
            Material material = Material.getMaterial(uniqueName.replaceAll("Vanilla_", ""));
            return new ItemStack(material);
        }
        GrassJson json = JsonHandler.getGrassJson(uniqueName);
        if(json == null) return null;
        ItemStack item = new ItemStack(json.getMaterial());
        item = JsonHandler.putUniqueName(item, uniqueName);
        return item;
    }

    public static ItemStack buildByConfigString(String configString) {
        String[] base = configString.split("::");
        String uniqueName = base[0];
        ItemStack item;
        if(uniqueName.startsWith("Vanilla")) {
            Material material = Material.getMaterial(uniqueName.replaceAll("Vanilla_", ""));
            item = new ItemStack(material);
        } else {
            item = buildByUniqueName(uniqueName);
        }
        if(base.length == 1) return item;
        String[] data = base[1].split(",");
        if(item == null) return null;
        for(String s: data) {
            String[] itemData = s.split("#");
            if(itemData.length == 1) break;
            item = JsonHandler.putDynamicData(item,
                    itemData[0],
                    itemData[1]);
        }
        return item;
    }
}
