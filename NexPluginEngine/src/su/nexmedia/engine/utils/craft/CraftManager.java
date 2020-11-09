package su.nexmedia.engine.utils.craft;

import java.util.Iterator;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.NexPlugin;
import su.nexmedia.engine.utils.craft.objects.IAbstractRecipe;
import su.nexmedia.engine.utils.craft.objects.NCraftRecipe;
import su.nexmedia.engine.utils.craft.objects.NFurnaceRecipe;

@SuppressWarnings("deprecation")
public class CraftManager<P extends NexPlugin<P>> {
	
	@NotNull protected P plugin;
	protected String pluginKey;
	
	public CraftManager(@NotNull P plugin) {
		this.plugin = plugin;
		this.pluginKey = new NamespacedKey(plugin, "dummy").getNamespace();
	}
	
	public boolean register(@NotNull IAbstractRecipe recipe) {
		Recipe bukkitRecipe = null;
		if (recipe instanceof NCraftRecipe) {
			bukkitRecipe = this.getRecipe((NCraftRecipe) recipe);
		}
		else if (recipe instanceof NFurnaceRecipe) {
			bukkitRecipe = this.getRecipe((NFurnaceRecipe) recipe);
		}
		
		if (bukkitRecipe == null) {
			this.plugin.warn("Could not register recipe '" + recipe.getId() + "': No recipe handler found.");
			return false;
		}
		
		try {
			if (!this.plugin.getServer().addRecipe(bukkitRecipe)) {
				this.plugin.error("Could not register recipe: '" + recipe.getId() + "': Unknown reason.");
				return false;
			}
		}
		catch (Exception ex) {
			this.plugin.error("Could not register recipe: '" + recipe.getId() + "': ");
			ex.printStackTrace();
			return false;
		}
		
		this.discoverRecipe(recipe.getRecipeKey(this.plugin));
		this.plugin.info("Recipe registered: '" + recipe.getId() + "' !");
		return true;
	}
	
	private void discoverRecipe(@NotNull NamespacedKey key) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p == null) continue;
			
			p.discoverRecipe(key);
			//this.plugin.info("Recipe undiscover for " + p.getName() + ": " + b + " (" + key.getKey() + ")");
		}
	}
	
	private void undiscoverRecipe(@NotNull NamespacedKey key) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p == null) continue;
			
			p.undiscoverRecipe(key);
			//this.plugin.info("Recipe undiscover for " + p.getName() + ": " + b + " (" + key.getKey() + ")");
		}
	}

	public void unregisterAll() {
		Iterator<Recipe> iter = this.plugin.getServer().recipeIterator();
		while (iter.hasNext()) {
			Recipe recipe = iter.next();
			NamespacedKey recipeKey = getRecipeKey(recipe);
			if (recipeKey != null && recipeKey.getNamespace().equals(this.pluginKey)) {
				this.undiscoverRecipe(recipeKey);
				iter.remove();
				this.plugin.info("Recipe unregistered: '" + recipeKey.getKey() + "' !");
			}
		}
	}
	
	public void unregister(@NotNull IAbstractRecipe recipe) {
		this.unregister(recipe.getId());
	}
	
	public void unregister(@NotNull String id) {
		id = id.toLowerCase();
		Iterator<Recipe> iter = this.plugin.getServer().recipeIterator();
		while (iter.hasNext()) {
			Recipe recipe = iter.next();
			NamespacedKey key = getRecipeKey(recipe);
			if (key != null && key.getNamespace().equals(this.pluginKey) && key.getKey().endsWith(id)) {
				this.undiscoverRecipe(key);
				iter.remove();
				this.plugin.info("Recipe unregistered: '" + id + "' !");
			}
		}
	}

	@NotNull
	private Recipe getRecipe(@NotNull NCraftRecipe recipe) {
		ItemStack result = recipe.getResult();
		NamespacedKey key = recipe.getRecipeKey(this.plugin);
		ItemStack[] ings = recipe.getIngredients();
		
		if (recipe.isShaped()) {
			char[] ziga = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
			
			ShapedRecipe sr = new ShapedRecipe(key, result);
			sr.shape(recipe.getShape());
			
			for (int i = 0; i < ings.length; i++) {
				char c = ziga[i];
				ItemStack ing = ings[i];
				if (ing.hasItemMeta()) {
					sr.setIngredient(c, new RecipeChoice.ExactChoice(ing));
				}
				else {
					//if (ing.getData() != null) {
					//	sr.setIngredient(c, ing.getData());
					//}
					//else {
						sr.setIngredient(c, ing.getType());
					//}
				}
			}
			return sr;
		}
		else {
			ShapelessRecipe sr = new ShapelessRecipe(key, result);
			for (ItemStack ing : ings) {
				if (ing.hasItemMeta()) {
					sr.addIngredient(new RecipeChoice.ExactChoice(ing));
				}
				else {
					MaterialData data = ing.getData();
					if (data != null) {
						sr.addIngredient(data);
					}
					else {
						sr.addIngredient(ing.getType());
					}
				}
			}
			return sr;
		}
	}
	
	@NotNull
	private Recipe getRecipe(@NotNull NFurnaceRecipe recipe) {
		NamespacedKey key = recipe.getRecipeKey(this.plugin);
		ItemStack input = recipe.getInput();
		ItemStack result = recipe.getResult();
		float exp = recipe.getExp();
		int time = recipe.getTime();
		
		if (input.hasItemMeta()) {
			return new FurnaceRecipe(key, result, new RecipeChoice.ExactChoice(input), exp, time);
		}
		else {
			return new FurnaceRecipe(key, result, input.getType(), exp, time);
		}
	}
	
	@Nullable
	private static NamespacedKey getRecipeKey(@NotNull Recipe r) {
		if (r instanceof ShapedRecipe) {
			return (((ShapedRecipe)r).getKey());
		}
		else if (r instanceof ShapelessRecipe) {
			return (((ShapelessRecipe)r).getKey());
		}
		else if (r instanceof FurnaceRecipe) {
			return (((FurnaceRecipe)r).getKey());
		}
		return null;
	}
}
