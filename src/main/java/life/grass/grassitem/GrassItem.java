package life.grass.grassitem;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GrassItem {
    private ItemStack item;

    public GrassItem(ItemStack item) {
        this.item = item.clone();
    }

    public ItemStack toItemStack() {
        return item;
    }

    public boolean hasNBT(GrassNBTTag tag) {
        return hasVanillaTag(tag);
    }

    public Optional<Object> getNBT(GrassNBTTag tag) {
        Class clazz = tag.getValueClass();

        if (tag.getValueClass().getName().startsWith("java.lang") && !hasVanillaTag(tag)) return Optional.empty();

        if (clazz.isArray()) {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    return Optional.ofNullable(getTagByteArray(tag));
                case "Integer":
                    return Optional.ofNullable(getTagIntArray(tag));
            }
        } else {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    return Optional.of(getTagByte(tag));
                case "Short":
                    return Optional.of(getTagShort(tag));
                case "Integer":
                    return Optional.of(getTagInt(tag));
                case "Long":
                    return Optional.of(getTagLong(tag));
                case "Float":
                    return Optional.of(getTagFloat(tag));
                case "Double":
                    return Optional.of(getTagDouble(tag));
                case "String":
                    return Optional.ofNullable(getTagString(tag));
                case "HashMap":
                    return Optional.ofNullable(getTagHashMap(tag));
            }
        }

        return Optional.empty();
    }

    public void setNBT(GrassNBTTag tag, Object value) {
        Class clazz = value.getClass();

        if (clazz.isArray()) {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    setTagByteArray(tag, (byte[]) value);
                    return;
                case "Integer":
                    setTagIntArray(tag, (int[]) value);
                    return;
            }
        } else {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    setTagByte(tag, (byte) value);
                    return;
                case "Short":
                    setTagShort(tag, (short) value);
                    return;
                case "Integer":
                    setTagInt(tag, (int) value);
                    return;
                case "Long":
                    setTagLong(tag, (long) value);
                    return;
                case "Float":
                    setTagFloat(tag, (float) value);
                    return;
                case "Double":
                    setTagDouble(tag, (double) value);
                    return;
                case "String":
                    setTagString(tag, (String) value);
                    return;
                case "HashMap":
                    setTagHashMap(tag, (HashMap) value);
                    return;
            }
        }
    }

    private void setNBT(String key, Class clazz, Object value) {
        setNBT(new GrassNBTTag() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Class getValueClass() {
                return clazz;
            }
        }, value);
    }

    private boolean hasVanillaTag(GrassNBTTag tag) {
        return getTag(tag.getKey()) != null;
    }

    private byte getTagByte(GrassNBTTag tag) {
        return (this.<NBTTagByte>getTag(tag.getKey())).g();
    }

    private short getTagShort(GrassNBTTag tag) {
        return (this.<NBTTagShort>getTag(tag.getKey()).f());
    }

    private int getTagInt(GrassNBTTag tag) {
        return (this.<NBTTagInt>getTag(tag.getKey()).e());
    }

    private long getTagLong(GrassNBTTag tag) {
        return (this.<NBTTagLong>getTag(tag.getKey()).d());
    }

    private float getTagFloat(GrassNBTTag tag) {
        return (this.<NBTTagFloat>getTag(tag.getKey()).i());
    }

    private double getTagDouble(GrassNBTTag tag) {
        return (this.<NBTTagDouble>getTag(tag.getKey()).asDouble());
    }

    private byte[] getTagByteArray(GrassNBTTag tag) {
        return (this.<NBTTagByteArray>getTag(tag.getKey()).c());
    }

    private String getTagString(GrassNBTTag tag) {
        return (this.<NBTTagString>getTag(tag.getKey()).c_());
    }

    private int[] getTagIntArray(GrassNBTTag tag) {
        return (this.<NBTTagIntArray>getTag(tag.getKey()).d());
    }

    private Map<String, String> getTagHashMap(GrassNBTTag tag) {
        Map<String, String> map = new HashMap();
        NBTTagCompound nbtTag = CraftItemStack.asNMSCopy(item).getTag();
        String key = tag.getKey();

        if (nbtTag != null)
            nbtTag.c().stream()
                    .filter(nbtKey -> nbtKey.startsWith(key + "/Map/"))
                    .forEach(nbtKey -> map.put(nbtKey.replace(key + "/Map/", ""), getTag(nbtKey).toString()));

        return map;
    }

    private <T extends NBTBase> T getTag(String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.getTag();

        return nbtTag == null ? null : (T) nbtTag.get(key);
    }

    private void setTagByte(GrassNBTTag tag, byte value) {
        setTag(tag.getKey(), new NBTTagByte(value));
    }

    private void setTagShort(GrassNBTTag tag, short value) {
        setTag(tag.getKey(), new NBTTagShort(value));
    }

    private void setTagInt(GrassNBTTag tag, int value) {
        setTag(tag.getKey(), new NBTTagInt(value));
    }

    private void setTagLong(GrassNBTTag tag, long value) {
        setTag(tag.getKey(), new NBTTagLong(value));
    }

    private void setTagFloat(GrassNBTTag tag, float value) {
        setTag(tag.getKey(), new NBTTagFloat(value));
    }

    private void setTagDouble(GrassNBTTag tag, double value) {
        setTag(tag.getKey(), new NBTTagDouble(value));
    }

    private void setTagByteArray(GrassNBTTag tag, byte[] value) {
        setTag(tag.getKey(), new NBTTagByteArray(value));
    }

    private void setTagString(GrassNBTTag tag, String value) {
        setTag(tag.getKey(), new NBTTagString(value));
    }

    private void setTagIntArray(GrassNBTTag tag, int[] value) {
        setTag(tag.getKey(), new NBTTagIntArray(value));
    }

    private void setTagHashMap(GrassNBTTag tag, HashMap value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        String key = tag.getKey();

        nbtTag.c().stream()
                .filter(nbtKey -> nbtKey.startsWith(key + "/Map/"))
                .forEach(this::removeTag);

        value.forEach((mapKey, mapValue) -> setNBT(tag.getKey() + "/Map/" + mapKey, mapValue.getClass(), mapValue));
    }

    private void setTag(String key, NBTBase value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        nbtTag.set(key, value);

        nmsItem.setTag(nbtTag);
        item = CraftItemStack.asBukkitCopy(nmsItem);
    }

    private void removeTag(String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        nbtTag.remove(key);

        nmsItem.setTag(nbtTag);
        item = CraftItemStack.asBukkitCopy(nmsItem);
    }
}
