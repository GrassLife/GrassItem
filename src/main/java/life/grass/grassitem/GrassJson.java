package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GrassJson {
    private static Gson gson;

    private JsonObject root, maskJsonObject;

    static {
        gson = new Gson();
    }

    /* package */ GrassJson(String uniqueName, JsonObject maskJsonObject) throws IllegalArgumentException {
        this.root = JsonBucket.getInstance().findGrassJson(uniqueName).orElseThrow(IllegalArgumentException::new);
        this.maskJsonObject = maskJsonObject == null ? new JsonObject() : maskJsonObject;
    }

    public String getUniqueName() {
        return root.get("UniqueName").getAsString();
    }

    public Material getMaterial() {
        return Material.valueOf(root.get("Material").getAsString());
    }

    public short getMeta() {
        return root.get("Meta").getAsShort();
    }

    public String getDisplayName() {
        return root.get("DisplayName").getAsString();
    }

    public String getDescription() {
        return root.get("Description").getAsString();
    }

    public boolean hasItemTag(String tag) {
        return Arrays.asList(gson.fromJson(root.getAsJsonArray("ItemTags"), String[].class)).contains(tag);
    }

    public boolean hasDynamicValue(String dynamicKey) {
        return hasDynamicValueInItem(dynamicKey) || hasDynamicValueInJson(dynamicKey);
    }

    public boolean hasDynamicValueInJson(String dynamicKey) {
        return root.getAsJsonObject("DynamicData").get(dynamicKey) != null;
    }

    public boolean hasDynamicValueInItem(String dynamicKey) {
        return maskJsonObject.get(dynamicKey) != null;
    }

    public GrassJsonDataValue getDynamicValue(String dynamicKey) {
        JsonElement jsonElement = root.getAsJsonObject("DynamicData").get(dynamicKey);
        return new GrassJsonDataValue(
                jsonElement,
                maskJsonObject.get(dynamicKey) == null ? null : maskJsonObject.get(dynamicKey).getAsString(),
                getEnchantList(dynamicKey)
        );
    }

    public boolean hasStaticValue(String staticKey) {
        return root.getAsJsonObject("StaticData").get(staticKey) != null;
    }

    public GrassJsonDataValue getStaticValue(String staticKey) {
        JsonElement jsonElement = root.getAsJsonObject("StaticData").get(staticKey);
        return jsonElement == null ? null : new GrassJsonDataValue(jsonElement, null);
    }

    public List<JsonElement> getEnchantList(String key) {
        ArrayList<JsonElement> list = new ArrayList<>();
        String[] enchantsPosition = {"Enchant/Prefix", "Enchant/Suffix", "Enchant/Special"};
        for(String pos: enchantsPosition) {
            JsonObject dynamicData = root.getAsJsonObject("DynamicData").getAsJsonObject(pos);
            String enchantName = maskJsonObject.get(pos) == null ? dynamicData.get(pos).getAsString() : maskJsonObject.get(pos).getAsString();
            JsonBucket.getInstance().findEnchantJson(enchantName).ifPresent(e -> list.add(e.get("Mask").getAsJsonObject().get(key)));
        }
        return list;
    }

    public ItemStack toItemStack() {
        return this.toItemStack(1);
    }

    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(getMaterial(), amount, getMeta());

        item = JsonHandler.putUniqueName(item, getUniqueName());

        JsonObject maskDynamicData = maskJsonObject.getAsJsonObject("DynamicData");
        if (maskDynamicData != null) {
            for (Map.Entry<String, JsonElement> entry : maskDynamicData.entrySet()) {
                if (entry.getValue() != null)
                    item = JsonHandler.putDynamicData(item, entry.getKey(), entry.getValue());
            }
        }

        return item;
    }
}
