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
        return mask == null ? getAsOriginalString() : Optional.of(mask);
    }

    public Optional<Integer> getAsOriginalInteger() {
        return Optional.ofNullable(getAsOriginalDouble().isPresent() ? getAsOriginalDouble().get().intValue() : null);
    }

    public Optional<Integer> getAsOverwritedInteger() {
        return Optional.ofNullable(getAsOverwritedDouble().isPresent() ? getAsOverwritedDouble().get().intValue() : null);
    }

    public Optional<Integer> getAsMaskedInteger() {
        return Optional.ofNullable(getAsMaskedDouble().isPresent() ? getAsMaskedDouble().get().intValue() : null);
    }

    public Optional<Double> getAsOriginalDouble() {
        return Optional.ofNullable(jsonElement == null ? null : jsonElement.getAsDouble());
    }

    public Optional<Double> getAsOverwritedDouble() {
        return mask == null ? null : Optional.of(Double.parseDouble(mask));
    }

    public Optional<Double> getAsMaskedDouble() {
        Double value = getAsOriginalDouble().orElse(0.0);

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
