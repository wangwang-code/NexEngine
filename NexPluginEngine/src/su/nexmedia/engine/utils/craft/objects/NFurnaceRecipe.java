package su.nexmedia.engine.utils.craft.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NFurnaceRecipe extends IAbstractRecipe {

	private ItemStack input;
	private float exp;
	private int time;
	
	public NFurnaceRecipe(@NotNull String id, @NotNull ItemStack result, float exp, double time) {
		super(id, result);
		this.exp = exp;
		this.time = (int)Math.max(1, 20D * time);
	}
	
	@NotNull
	public ItemStack getInput() {
		return this.input;
	}
	
	public float getExp() {
		return this.exp;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public void addIngredient(@NotNull ItemStack ing) {
		this.addIngredient(0, ing);
	}
	
	@Override
	public void addIngredient(int slot, @Nullable ItemStack ing) {
		if (ing == null || ing.getType() == Material.AIR) {
			throw new IllegalArgumentException("Input can not be null or AIR!");
		}
		this.input = ing;
	}
}
