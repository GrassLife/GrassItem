package life.grass.grassitem.tag;

public enum CookingTag implements GrassNBTTag {
    RESTORE_AMOUNT("RestoreAmount", Integer.class);

    private String key;
    private Class clazz;

    CookingTag(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public String getKey() {
        return "CookingTag/" + key;
    }

    @Override
    public Class getValueClass() {
        return clazz;
    }
}
