package life.grass.grassitem.ItemData;

import life.grass.grassplayer.GrassPlayer;
import life.grass.grassplayer.StatusType;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by ecila on 2017/09/25.
 */
public enum WeaponSkill {
    BLOW("打撃") {
        @Override
        public double apply(Player player) {
            GrassPlayer grassPlayer = GrassPlayer.findOrCreate(player);
            return ((double) grassPlayer.getStatus(StatusType.STRENGTH)) * 0.05;
        }
    },
    TECHNIQUE("技巧") {
        @Override
        public double apply(Player player) {
            GrassPlayer grassPlayer = GrassPlayer.findOrCreate(player);
            return ((double) grassPlayer.getStatus(StatusType.STRENGTH)) * 0.05 + ((double) grassPlayer.getStatus(StatusType.DEXTERITY)) * 0.05;
        }
    },
    HEAVY_BLOW("重打") {
        @Override
        public double apply(Player player) {
            GrassPlayer grassPlayer = GrassPlayer.findOrCreate(player);
            return ((double) grassPlayer.getStatus(StatusType.STRENGTH)) * 0.05 + ((double) grassPlayer.getStatus(StatusType.VITALITY)) * 0.05;
        }
    },
    SNIPE("狙撃") {
        @Override
        public double apply(Player player) {
            GrassPlayer grassPlayer = GrassPlayer.findOrCreate(player);
            return ((double) grassPlayer.getStatus(StatusType.DEXTERITY)) * 0.1;
        }
    },
    NOTHING("なし") {
        @Override
        public double apply(Player player) {
            GrassPlayer grassPlayer = GrassPlayer.findOrCreate(player);
            return 0;
        }
    };

    private String label;

    WeaponSkill(String label) {
        this.label = label;
    }

    public abstract double apply(Player player);

    public String getLabel() {
        return this.label;
    }

    public static WeaponSkill getSkill(String name) {
        return Arrays.stream(WeaponSkill.values()).filter(skill -> skill.name().equals(name)).findFirst().orElse(WeaponSkill.NOTHING);
    }
}
