package life.grass.grassitem;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GrassItem extends JavaPlugin {
    private static GrassItem instance;
    private static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        JsonBucket.getInstance();

        protocolManager = ProtocolLibrary.getProtocolManager();
        ItemPacketRewriter.getInstance().addListener(protocolManager, this);

        getCommand("grassitem").setExecutor(new GrassItemCommandExecutor());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        instance = null;
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public static GrassItem getInstance() {
        return instance;
    }

    public void refillJsonBucket() {
    }
}
