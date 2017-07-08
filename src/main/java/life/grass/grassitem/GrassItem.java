package life.grass.grassitem;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import life.grass.grassitem.events.listener.DurabilityChangeListener;
import life.grass.grassitem.events.listener.PlayerDeathListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GrassItem extends JavaPlugin {
    private static GrassItem instance;
    private static ProtocolManager protocolManager;
    private static PluginManager pluginManager;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        pluginManager = getServer().getPluginManager();
        JsonBucket.getInstance();

        protocolManager = ProtocolLibrary.getProtocolManager();
        ItemPacketRewriter.getInstance().addListener(protocolManager, this);
        pluginManager.registerEvents(new DurabilityChangeListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);

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
