package su.nexmedia.engine.modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.NexPlugin;

public class ModuleManager<P extends NexPlugin<P>> {

	@NotNull private P plugin;
	private Map<String, IModule<P>> modules;
	
	public ModuleManager(@NotNull P plugin) {
		this.plugin = plugin;
	}
	
	public void setup() {
		this.modules = new LinkedHashMap<>();
	}
	
	public void shutdown() {
		for (IModule<P> module : new HashMap<>(this.modules).values()) {
			this.unregister(module);
		}
		this.modules.clear();
	}
	
	/**
	 * @param module Module instance.
	 * @return An object instance of registered module. Returns NULL if module hasn't registered.
	 */
	@Nullable
	public IModule<P> register(@NotNull IModule<P> module) {
		if (!module.isEnabled()) return null;
		
		String id = module.getId();
		if (this.modules.containsKey(id)) {
			this.plugin.error("Could not register " + id + " module! Module with such id already registered!");
			return null;
		}
		
		long loadTook = System.currentTimeMillis();
		module.load();
		loadTook = System.currentTimeMillis() - loadTook;
		
		if (!module.isLoaded()) {
			this.plugin.error("Failed module load: " + module.name() + " v" + module.version());
			return null;
		}
		
		this.plugin.info("Loaded module: " + module.name() + " v" + module.version() + " in " + loadTook + " ms.");
		this.modules.put(id, module);
		return module;
	}
	
	public void unregister(@NotNull IModule<?> module) {
		String id = module.getId();
		if (this.modules.remove(id) != null) {
			this.plugin.info("Unloaded module: " + module.name() + " v" + module.version());
		}
		module.unload();
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends IModule<P>> T getModule(@NotNull Class<T> clazz) {
        for (IModule<?> module : this.modules.values()) {
            if (clazz.isAssignableFrom(module.getClass())) {
                return (T) module;
            }
        }
        return null;
    }
	
	@Nullable
	public IModule<P> getModule(@NotNull String id) {
		return this.modules.get(id.toLowerCase());
	}
	
	@NotNull
	public Collection<IModule<P>> getModules() {
		return this.modules.values();
	}
}
