package life.grass.grassitem.events;

import life.grass.grassitem.GrassJson;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ecila on 2017/07/03.
 */
public class ItemRewriteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private List<String> lore;
    private RewriteType type;
    private boolean showable;
    private GrassJson json;
    
    public ItemRewriteEvent(RewriteType type, GrassJson json) {
        this.lore = new ArrayList<>();
        this.type = type;
        this.showable = false;
        this.json = json;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public RewriteType getType() {
        return type;
    }

    public void setType(RewriteType type) {
        this.type = type;
    }

    public boolean isShowable() {
        return showable;
    }

    public void setShowable(boolean showable) {
        this.showable = showable;
    }

    public GrassJson getJson() {
        return json;
    }

    public void setJson(GrassJson json) {
        this.json = json;
    }
}
