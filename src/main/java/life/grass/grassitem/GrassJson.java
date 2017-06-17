package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Material;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GrassJson {
    private static final String JSON_DIR_PATH = Main.getInstance().getDataFolder().getPath() + File.separator + "json" + File.separator;

    private static Map<String, GrassJson> grassJsonMap;
    private static Gson gson;

    private JsonObject json;

    static {
        grassJsonMap = new HashMap<>();
        gson = new Gson();

        Arrays.stream(new File(JSON_DIR_PATH).listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> {
                    String fileName = json.getName().replace(".json", "");
                    grassJsonMap.put(fileName, loadJsonFromFile(json));
                });
    }

    private GrassJson(JsonObject json) {
        this.json = json;
    }

    public static Optional<GrassJson> findGrassJson(String uniqueName) {
        return Optional.ofNullable(grassJsonMap.getOrDefault(uniqueName, null));
    }

    private static GrassJson loadJsonFromFile(File file) {
        JsonObject root;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            root = gson.fromJson(br, JsonObject.class);
        } catch (Exception ex) {
            return null;
        }

        return new GrassJson(root);
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
        return Optional.ofNullable(json.getAsJsonObject("DynamicData").get(key).getAsString());
    }

    public Optional<Integer> getDynamicDataAsInteger(String key) {
        return Optional.of(json.getAsJsonObject("DynamicData").get(key).getAsInt());
    }

    public Optional<Double> getDynamicDataAsDouble(String key) {
        return Optional.of(json.getAsJsonObject("DynamicData").get(key).getAsDouble());
    }

    public Optional<Integer> getDynamicDataAsCalculatedInteger(String key, String mask) {
        return getDynamicDataAsCalculatedDouble(key, mask).map(Double::intValue);
    }

    public Optional<Double> getDynamicDataAsCalculatedDouble(String key, String mask) {
        if (!json.getAsJsonObject("DynamicData").has(key)) return Optional.empty();
        double value = json.getAsJsonObject("DynamicData").get(key).getAsDouble();

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
}
