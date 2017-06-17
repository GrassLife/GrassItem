package life.grass.grassitem.tag;

import life.grass.grassitem.GrassNBTTag;

public enum UnityTag implements GrassNBTTag {
    JSON_NAME("JsonName", String.class);

    private String key;
    private Class clazz;

    UnityTag(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public String getKey() {
        return "Unity/" + key;
    }

    @Override
    public Class getValueClass() {
        return clazz;
    }
}
