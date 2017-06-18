package life.grass.grassitem;

import com.google.gson.Gson;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GrassItemHandler {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static ItemStack putUniqueNameToItemStack(ItemStack item, String uniqueName) {
        return setNBTString(item, "UniqueName", uniqueName);
    }

    public static ItemStack putDynamicDataToItemStack(ItemStack item, String key, String value) {
        return setNBTString(item, "Dynamic/" + key, value);
    }

    public static Optional<String> findUniqueNameFromItemStack(ItemStack item) {
        return Optional.ofNullable(getNBTString(item, "UniqueName"));
    }

    public static Map<String, String> getDynamicDataMapFromItemStack(ItemStack item) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        List<String> dynamicTagList = new ArrayList<>();
        nbtTag.c().stream()
                .filter(nbt -> nbt.startsWith("Dynamic/"))
                .map(nbt -> nbt.replace("Dynamic/", ""))
                .forEach(dynamicTagList::add);

        Map<String, String> dynamicDataMap = new HashMap<>();
        dynamicTagList.forEach(tag -> findDynamicDataFromItemStack(item, tag).ifPresent(data -> dynamicDataMap.put(tag, data)));

        return dynamicDataMap;
    }

    public static Optional<String> findDynamicDataFromItemStack(ItemStack item, String tag) {
        return Optional.ofNullable(getNBTString(item, "Dynamic/" + tag));
    }

    private static String getNBTString(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.getTag();

        return nbtTag == null ? null : nbtTag.get(key).toString();

    }

    private static ItemStack setNBTString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.set(key, new NBTTagString(value));

        nmsItem.setTag(nbtTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
