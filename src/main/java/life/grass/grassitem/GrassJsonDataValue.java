package life.grass.grassitem;

import com.google.gson.JsonElement;

import java.util.Optional;

public class GrassJsonDataValue {

    private JsonElement jsonElement;
    private String mask;

    /* package */ GrassJsonDataValue(JsonElement jsonElement, String mask) {
        this.jsonElement = jsonElement;
        this.mask = mask == null ? null : mask.replace("\"", "");
    }

    public Optional<String> getAsOriginalString() {
        return Optional.ofNullable(jsonElement.getAsString());
    }

    public Optional<String> getAsOverwritedString() {
        return Optional.ofNullable(mask);
    }

    public Optional<Integer> getAsOriginalInteger() {
        return Optional.of(getAsOriginalDouble().get().intValue());
    }

    public Optional<Integer> getAsOverwritedInteger() {
        return Optional.ofNullable(!getAsOverwritedDouble().isPresent() ? null : getAsOverwritedDouble().get().intValue());
    }

    public Optional<Integer> getAsMaskedInteger() {
        return Optional.of(getAsMaskedDouble().get().intValue());
    }

    public Optional<Double> getAsOriginalDouble() {
        return Optional.of(jsonElement == null ? 0 : jsonElement.getAsDouble());
    }

    public Optional<Double> getAsOverwritedDouble() {
        return Optional.ofNullable(mask == null ? null : Double.parseDouble(mask));
    }

    public Optional<Double> getAsMaskedDouble() {
        Double value = getAsOriginalDouble().get();

        if (mask == null || mask.equalsIgnoreCase("")) return getAsOriginalDouble();

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
}
