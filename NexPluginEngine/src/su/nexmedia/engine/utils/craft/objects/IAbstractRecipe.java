package su.nexmedia.engine.utils.craft.objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.NexPlugin;

public abstract class IAbstractRecipe {

	protected String id;
	protected ItemStack result;
	
	protected NamespacedKey key;
	
	public IAbstractRecipe(@NotNull String id, @NotNull ItemStack result) {
		this.id = id.toLowerCase();
		this.result = result;
	}
	
	@NotNull
	public String getId() {
		return this.id;
	}
	
	@NotNull
	public ItemStack getResult() {
		return this.result;
	}
	
	public void setResult(@NotNull ItemStack result) {
		this.result = result;
	}
	
	public abstract void addIngredient(int slot, @Nullable ItemStack item);
	
	@NotNull
	public NamespacedKey getRecipeKey(@NotNull NexPlugin<?> plugin) {
		if (this.key == null) {
			String type = "";
			if (this instanceof NCraftRecipe) {
				type = "craft";
			}
			else if (this instanceof NFurnaceRecipe) {
				type = "furnace";
			}
			String key = type + "-" + this.getId();
			
			this.key = new NamespacedKey(plugin, key);
		}
		return this.key;
	}
}
