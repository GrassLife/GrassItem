package life.grass.grassitem.tag;

public enum CookingTag implements GrassNBTTag {
    EXPIRE_DATE("ExpireDate", String.class),
    RESTORE_AMOUNT("RestoreAmount", Integer.class),
    ELEMENT_SWEET("ElementSweet", Integer.class),
    ELEMENT_SPICY("ElementSpicy", Integer.class),
    ELEMENT_SALTY("ElementSalty", Integer.class),
    ELEMENT_UMAMI("ElementUmami", Integer.class);

    private String key;
    private Class clazz;

    CookingTag(String key, Class clazz) {
        this.key = key;
        this.clazz = clazz;
    }

    @Override
    public String getKey() {
        return "Cooking/" + key;
    }

    @Override
    public Class getValueClass() {
        return clazz;
    }
}
