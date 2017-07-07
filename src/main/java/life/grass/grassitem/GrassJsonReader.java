package life.grass.grassitem;

/**
 * Created by ecila on 2017/07/06.
 */
public class GrassJsonReader {
    GrassJson json;

    public GrassJsonReader(GrassJson json) {
        this.json = json;
    }

    public double getDurabilityRate() {
        if(!json.hasDynamicValue("CurrentDurability") && json.hasDynamicValue("MaxDurability")) return 0.0;
        return json.getDynamicValue("CurrentDurability").getAsMaskedDouble().orElse(1.0) / json.getDynamicValue("MaxDurability").getAsMaskedDouble().orElse(1.0);
    }

    public int getDurabilityRateIndex() {
        return (int) Math.max(0, (getDurabilityRate() - 0.001) * 10);
    }

    public double getEffectRate() {
        if(!json.hasDynamicValue("EffectBar/" + getDurabilityRateIndex())) return 1.0;
        return json.getDynamicValue("EffectBar/" + getDurabilityRateIndex()).getAsMaskedDouble().orElse(1.0);
    }

    public int getActualGatheringPower() {
        if(!json.hasDynamicValue("GatheringPower")) return 0;
        return (int) (json.getDynamicValue("GatheringPower").getAsMaskedDouble().orElse(0.0) * getEffectRate());
    }
}
