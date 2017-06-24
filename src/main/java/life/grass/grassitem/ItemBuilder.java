package life.grass.grassitem;

import org.bukkit.inventory.ItemStack;

/**
 * Created by ecila on 2017/06/24.
 */
public class ItemBuilder {
    public static ItemStack buildByUniqueName(String uniqueName) {
        GrassJson json = JsonHandler.getGrassJson(uniqueName);
        if(json == null) return null;
        ItemStack item = new ItemStack(json.getMaterial());
        item = JsonHandler.putUniqueName(item, uniqueName);
        return item;
    }

    public static ItemStack buildByConfigString(String configString) {
        String uniqueName = configString.split(":")[0];
        String[] data = configString.split(":")[1].split(",");
        ItemStack item = buildByUniqueName(uniqueName);
        if(item == null) return null;
        for(String s: data) {
            item = JsonHandler.putDynamicData(item, s.split(".")[0], s.split(".")[1]);
        }
        return item;
    }
}
