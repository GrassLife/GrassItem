package life.grass.grassitem;

import life.grass.grassitem.tag.GrassNBTTag;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

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
        return hasTag(tag.getKey());
    }

    public Optional<Object> getNBT(GrassNBTTag tag) {
        String key = tag.getKey();
        Class clazz = tag.getValueClass();

        if (!hasTag(key)) return Optional.empty();

        if (clazz.isArray()) {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    return Optional.ofNullable(getTagByteArray(key));
                case "Integer":
                    return Optional.ofNullable(getTagIntArray(key));
            }
        } else {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    return Optional.of(getTagByte(key));
                case "Short":
                    return Optional.of(getTagShort(key));
                case "Integer":
                    return Optional.of(getTagInt(key));
                case "Long":
                    return Optional.of(getTagLong(key));
                case "Float":
                    return Optional.of(getTagFloat(key));
                case "Double":
                    return Optional.of(getTagDouble(key));
                case "String":
                    return Optional.ofNullable(getTagString(key));
            }
        }

        return Optional.empty();
    }

    public void setNBT(GrassNBTTag tag, Object value) {
        String key = tag.getKey();
        Class clazz = tag.getValueClass();

        if (clazz.isArray()) {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    setTagByteArray(key, (byte[]) value);
                    return;
                case "Integer":
                    setTagIntArray(key, (int[]) value);
                    return;
            }
        } else {
            switch (clazz.getSimpleName()) {
                case "Byte":
                    setTagByte(key, (byte) value);
                    return;
                case "Short":
                    setTagShort(key, (short) value);
                    return;
                case "Integer":
                    setTagInt(key, (int) value);
                    return;
                case "Long":
                    setTagLong(key, (long) value);
                    return;
                case "Float":
                    setTagFloat(key, (float) value);
                    return;
                case "Double":
                    setTagDouble(key, (double) value);
                    return;
                case "String":
                    setTagString(key, (String) value);
                    return;
            }
        }

        throw new IllegalArgumentException();
    }

    private boolean hasTag(String key) {
        return getTag(key) != null;
    }

    private byte getTagByte(String key) {
        return (this.<NBTTagByte>getTag(key)).g();
    }

    private short getTagShort(String key) {
        return (this.<NBTTagShort>getTag(key).f());
    }

    private int getTagInt(String key) {
        return (this.<NBTTagInt>getTag(key).e());
    }

    private long getTagLong(String key) {
        return (this.<NBTTagLong>getTag(key).d());
    }

    private float getTagFloat(String key) {
        return (this.<NBTTagFloat>getTag(key).i());
    }

    private double getTagDouble(String key) {
        return (this.<NBTTagDouble>getTag(key).asDouble());
    }

    private byte[] getTagByteArray(String key) {
        return (this.<NBTTagByteArray>getTag(key).c());
    }

    private String getTagString(String key) {
        return (this.<NBTTagString>getTag(key).c_());
    }

    private int[] getTagIntArray(String key) {
        return (this.<NBTTagIntArray>getTag(key).d());
    }

    private <T extends NBTBase> T getTag(String key) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.getTag();

        return nbtTag == null ? null : (T) nbtTag.get(key);
    }

    private void setTagByte(String key, byte value) {
        setTag(key, new NBTTagByte(value));
    }

    private void setTagShort(String key, short value) {
        setTag(key, new NBTTagShort(value));
    }

    private void setTagInt(String key, int value) {
        setTag(key, new NBTTagInt(value));
    }

    private void setTagLong(String key, long value) {
        setTag(key, new NBTTagLong(value));
    }

    private void setTagFloat(String key, float value) {
        setTag(key, new NBTTagFloat(value));
    }

    private void setTagDouble(String key, double value) {
        setTag(key, new NBTTagDouble(value));
    }

    private void setTagByteArray(String key, byte[] value) {
        setTag(key, new NBTTagByteArray(value));
    }

    private void setTagString(String key, String value) {
        setTag(key, new NBTTagString(value));
    }

    private void setTagIntArray(String key, int[] value) {
        setTag(key, new NBTTagIntArray(value));
    }

    private void setTag(String key, NBTBase value) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtTag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        nbtTag.set(key, value);

        nmsItem.setTag(nbtTag);
        item = CraftItemStack.asBukkitCopy(nmsItem);
    }
}
