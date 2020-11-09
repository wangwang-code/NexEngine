package su.nexmedia.engine.manager;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.NexPlugin;

public abstract class IListener<P extends NexPlugin<P>> implements Listener {
	
    @NotNull public final P plugin;
    
    public IListener(@NotNull P plugin) {
        this.plugin = plugin;
    }
    
    public void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }
    
    public void unregisterListeners() {
        HandlerList.unregisterAll(this);
    }
}
