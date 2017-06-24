package life.grass.grassitem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonBucket {
    private static final String ITEMS_DIR_PATH = GrassItem.getInstance().getDataFolder().getPath() + File.separator + "items";
    private static final String ENCHANTS_DIR_PATH = GrassItem.getInstance().getDataFolder().getPath() + File.separator + "enchants";

    private static Gson gson;
    private static JsonBucket jsonBucket;

    private Map<String, JsonObject> grassJsonMap;
    private Map<String, JsonObject> enchantsMap;

    static {
        gson = new Gson();
        jsonBucket = new JsonBucket();
    }

    private JsonBucket() {
        grassJsonMap = new HashMap<>();
        enchantsMap = new HashMap<>();

        File itemFolder = new File(ITEMS_DIR_PATH);
        if (!itemFolder.exists()) itemFolder.mkdirs();

        File enchantFolder = new File(ENCHANTS_DIR_PATH);
        if (!enchantFolder.exists()) enchantFolder.mkdirs();

        Arrays.stream(itemFolder.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> loadJsonFromFile(json).ifPresent(jsonObject -> grassJsonMap.put(jsonObject.get("UniqueName").getAsString(), jsonObject)));

         Arrays.stream(enchantFolder.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> loadJsonFromFile(json).ifPresent(jsonObject -> enchantsMap.put(jsonObject.get("Name").getAsString(), jsonObject)));

    }

    public static JsonBucket getInstance() {
        return jsonBucket;
    }

    /* package */ Optional<JsonObject> findGrassJson(String uniqueName) {
        return Optional.ofNullable(grassJsonMap.getOrDefault(uniqueName.replace("\"", ""), null));
    }

    Optional<JsonObject> findEnchantJson(String name) {
        return Optional.ofNullable(enchantsMap.getOrDefault(name.replace("\"", ""), null));
    }


    private static Optional<JsonObject> loadJsonFromFile(File file) {
        JsonObject root;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            root = gson.fromJson(br, JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            root = null;
        }

        return Optional.ofNullable(root);
    }
}
