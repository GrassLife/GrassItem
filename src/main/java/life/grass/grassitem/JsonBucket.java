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
    private static final String JSON_DIR_PATH = GrassItem.getInstance().getDataFolder().getPath() + File.separator + "json";

    private static Gson gson;
    private static JsonBucket jsonBucket;

    private Map<String, JsonObject> jsonObjectMap;

    static {
        gson = new Gson();
        jsonBucket = new JsonBucket();
    }

    private JsonBucket() {
        jsonObjectMap = new HashMap<>();

        File folder = new File(JSON_DIR_PATH);
        if (!folder.exists()) folder.mkdirs();

        Arrays.stream(folder.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> loadJsonFromFile(json).ifPresent(jsonObject -> jsonObjectMap.put(jsonObject.get("UniqueName").getAsString(), jsonObject)));
    }

    public static JsonBucket getInstance() {
        return jsonBucket;
    }

    /* package */ Optional<JsonObject> findJsonObject(String uniqueName) {
        return Optional.ofNullable(jsonObjectMap.getOrDefault(uniqueName.replace("\"", ""), null));
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
