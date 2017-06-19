package life.grass.grassitem;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemHandler {

    public static GrassJson getGrassJson(ItemStack item) {
        String uniqueName = getNBTString(item, "UniqueName");
        return uniqueName == null ? getVanillaGrassJson(item) : getGrassJson(item, uniqueName);
    }

    public static GrassJson getGrassJson(ItemStack item, String uniqueName) {
        if (uniqueName == null) throw new IllegalArgumentException("uniqueName must not be null.");
        return new GrassJson(uniqueName, getDynamicDataMap(item));
    }

    private static GrassJson getVanillaGrassJson(ItemStack item) {
        if (item == null) return null;

        try {
            return new GrassJson("Vanilla_" + item.getType().toString(), getDynamicDataMap(item));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static ItemStack putUniqueName(ItemStack item, String uniqueName) {
        if (uniqueName == null) throw new IllegalArgumentException("uniqueName must not be null.");

        return setNBTString(item, "UniqueName", uniqueName);
    }

    public static ItemStack putDynamicData(ItemStack item, String key, Object value) {
        return setNBTString(item, "DynamicData/" + key, value.toString());
    }

    private static Map<String, String> getDynamicDataMap(ItemStack item) {
        Set<String> keySet = getNBTKeySet(item).stream()
                .filter(key -> key.startsWith("DynamicData/"))
                .map(key -> key.replace("DynamicData/", "").replace("\"", ""))
                .collect(Collectors.toSet());

        Map<String, String> dynamicDataMap = new HashMap();
        keySet.forEach(key -> dynamicDataMap.put(key, getNBTString(item, "DynamicData/" + key)));

        return dynamicDataMap;
    }

    private static Set<String> getNBTKeySet(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        return nbtTag.c();
    }

    private static String getNBTString(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.getTag();

        return nbtTag == null || !nbtTag.hasKey(key) ? null : nbtTag.get(key).toString().replace("\"", "");
    }

    private static ItemStack setNBTString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.set(key, new NBTTagString(value));

        nmsItem.setTag(nbtTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}