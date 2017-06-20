package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Material;

import java.util.Arrays;

public class GrassJson {
    private static Gson gson;

    private JsonObject root, maskJsonObject;

    static {
        gson = new Gson();
    }

    /* package */ GrassJson(String uniqueName, JsonObject maskJsonObject) throws IllegalArgumentException {
        this.root = JsonBucket.getInstance().findJsonObject(uniqueName).orElseThrow(IllegalArgumentException::new);
        this.maskJsonObject = maskJsonObject;
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

    public GrassJsonDataValue getDynamicValue(String dynamicKey) {
        JsonElement jsonElement = root.getAsJsonObject("DynamicData").get(dynamicKey);
        return new GrassJsonDataValue(jsonElement, maskJsonObject.get(dynamicKey) == null ? null : maskJsonObject.get(dynamicKey).getAsString());
    }

    public GrassJsonDataValue getStaticValue(String staticKey) {
        JsonElement jsonElement = root.getAsJsonObject("StaticData").get(staticKey);
        return jsonElement == null ? null : new GrassJsonDataValue(jsonElement, null);
    }

}
