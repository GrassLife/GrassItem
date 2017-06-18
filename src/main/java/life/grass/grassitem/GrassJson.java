package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GrassJson {
    private static Gson gson;
    private static JsonBucket jsonBucket;

    private JsonObject json;
    private Map<String, String> maskMap;

    static {
        gson = new Gson();
        jsonBucket = JsonBucket.getInstance();
    }

    private GrassJson(JsonObject json) {
        this(json, new HashMap<>());
    }

    private GrassJson(JsonObject json, Map<String, String> maskMap) {
        this.json = json;
        this.maskMap = maskMap;
    }

    public static Optional<GrassJson> findGrassJson(String uniqueName) {
        JsonObject jsonObject = jsonBucket.findJsonObject(uniqueName).orElse(null);

        if (jsonObject == null) return Optional.empty();
        else return Optional.of(new GrassJson(jsonObject));
    }

    public static Optional<GrassJson> findGrassJson(String uniqueName, ItemStack maskItem) {
        JsonObject jsonObject = jsonBucket.findJsonObject(uniqueName).orElse(null);

        if (jsonObject == null) return Optional.empty();
        else return Optional.of(new GrassJson(jsonObject, GrassItemHandler.getDynamicDataMapFromItemStack(maskItem)));
    }

    public static Optional<GrassJson> findGrassJsonFromItemStack(ItemStack item) {
        if (item == null) return Optional.empty();

        String uniqueName = GrassItemHandler.findUniqueNameFromItemStack(item).orElse(null);
        JsonObject jsonObject;

        if (uniqueName == null || uniqueName.equalsIgnoreCase("")) uniqueName = "Vanilla_" + item.getType().toString();
        jsonObject = jsonBucket.findJsonObject(uniqueName).orElse(null);

        if (jsonObject == null) return Optional.empty();
        else return Optional.of(new GrassJson(jsonObject, GrassItemHandler.getDynamicDataMapFromItemStack(item)));
    }

    public void setMask(ItemStack maskItem) {
        maskMap = GrassItemHandler.getDynamicDataMapFromItemStack(maskItem);
    }

    public String getUniqueName() {
        return json.get("UniqueName").getAsString();
    }

    public Material getMaterial() {
        return Material.valueOf(json.get("Material").getAsString());
    }

    public short getMeta() {
        return json.get("Meta").getAsShort();
    }

    public String getDisplayName() {
        return json.get("DisplayName").getAsString();
    }

    public String getDescription() {
        return json.get("Description").getAsString();
    }

    public boolean hasItemTag(String tag) {
        return Arrays.asList(gson.fromJson(json.getAsJsonArray("ItemTags"), String[].class)).contains(tag);
    }

    public Optional<String> getDynamicDataAsString(String key) {
        JsonElement jsonElement = exploreJsonElement(key);
        return Optional.ofNullable(jsonElement == null ? null : jsonElement.getAsString());
    }

    public Optional<Integer> getDynamicDataAsInteger(String key) {
        JsonElement jsonElement = exploreJsonElement(key);
        return Optional.ofNullable(jsonElement == null ? null : jsonElement.getAsInt());
    }

    public Optional<Double> getDynamicDataAsDouble(String key) {
        JsonElement jsonElement = exploreJsonElement(key);
        return Optional.ofNullable(jsonElement == null ? null : jsonElement.getAsDouble());
    }

    public Optional<Integer> getDynamicDataAsMaskedInteger(String key) {
        return getDynamicDataAsCalculatedInteger(key, maskMap.getOrDefault(key, null));
    }

    public Optional<Integer> getDynamicDataAsCalculatedInteger(String key, String mask) {
        return getDynamicDataAsCalculatedDouble(key, mask).map(Double::intValue);
    }

    public Optional<Double> getDynamicDataAsMaskedDouble(String key) {
        return getDynamicDataAsCalculatedDouble(key, maskMap.getOrDefault(key, null));
    }

    public Optional<Double> getDynamicDataAsCalculatedDouble(String key, String mask) {
        JsonElement jsonElement = exploreJsonElement(key);
        if (jsonElement == null) return Optional.empty();
        double value = jsonElement.getAsDouble();

        if (mask == null || mask.equalsIgnoreCase("")) return Optional.of(value);

        mask = mask.replace("\"", "");
        String subtractedMask = mask.substring(1);
        if (mask.startsWith("+")) {
            return Optional.of(value + Double.valueOf(subtractedMask));
        } else if (mask.startsWith("-")) {
            return Optional.of(value - Double.valueOf(subtractedMask));
        } else if (mask.startsWith("*")) {
            return Optional.of(value * Double.valueOf(subtractedMask));
        } else if (mask.startsWith("/")) {
            return Optional.of(value / Double.valueOf(subtractedMask));
        } else {
            return Optional.of(Double.valueOf(mask));
        }
    }

    public Optional<String> getStaticDataAsString(String key) {
        return Optional.ofNullable(json.getAsJsonObject("StaticData").get(key).getAsString());
    }

    private JsonElement exploreJsonElement(String key) {
        return json.getAsJsonObject("DynamicData").get(key);
    }
}
