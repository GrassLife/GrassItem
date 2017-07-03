package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class JsonHandler {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static GrassJson getGrassJson(ItemStack item) {
        String uniqueName = getNBTString(item, "UniqueName");
        try {
            return uniqueName == null ? getVanillaGrassJson(item) : getGrassJson(item, uniqueName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static GrassJson getGrassJson(String uniqueName) {
        if (uniqueName == null) throw new IllegalArgumentException("uniqueName must not be null.");
        try {
            return new GrassJson(uniqueName, null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static GrassJson getGrassJson(ItemStack item, String uniqueName) {
        if (uniqueName == null) throw new IllegalArgumentException("uniqueName must not be null.");
        return new GrassJson(uniqueName, getMaskJsonObject(item));
    }

    private static GrassJson getVanillaGrassJson(ItemStack item) {
        if (item == null) return null;

        try {
            return new GrassJson("Vanilla_" + item.getType().toString(), getMaskJsonObject(item));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static ItemStack putUniqueName(ItemStack item, String uniqueName) {
        if (uniqueName == null) throw new IllegalArgumentException("uniqueName must not be null.");

        return setNBTString(item, "UniqueName", uniqueName);
    }

    public static ItemStack putDynamicData(ItemStack item, String key, Object value) {
        JsonObject mapJsonObject = getMaskJsonObject(item);
        mapJsonObject.addProperty(key, value.toString());

        return setNBTString(item, "DynamicData", gson.toJson(mapJsonObject));
    }

    public static ItemStack putExpireDateHours(ItemStack item, int hours) {
        LocalDateTime time = LocalDateTime.now();
        return putDynamicData(item, "ExpireDate", time.plusHours(hours).minusMinutes(time.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES));
    }

    public static String printExpireDate(LocalDateTime localDateTime) {
        localDateTime = localDateTime.minusMinutes(localDateTime.getMinute() % 10).truncatedTo(ChronoUnit.MINUTES);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }


    private static JsonObject getMaskJsonObject(ItemStack item) {
        String json = getNBTString(item, "DynamicData");

        JsonObject maskJsonObject;
        if (json == null) maskJsonObject = new JsonObject();
        else maskJsonObject = gson.fromJson(json.substring(1, json.length() - 1).replace("\\", ""), JsonObject.class);

        return maskJsonObject;
    }

    private static String getNBTString(ItemStack item, String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.getTag();

        return nbtTag == null || !nbtTag.hasKey(key) ? null : nbtTag.get(key).toString();
    }

    private static ItemStack setNBTString(ItemStack item, String key, String value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        nbtTag.set(key, new NBTTagString(value));

        nmsItem.setTag(nbtTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}