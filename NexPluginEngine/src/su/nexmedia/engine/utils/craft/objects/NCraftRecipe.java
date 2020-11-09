package su.nexmedia.engine.utils.craft.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NCraftRecipe extends IAbstractRecipe {

	private boolean isShape;
	private String[] shape;
	private ItemStack[] ings;
	
	public NCraftRecipe(@NotNull String id, @NotNull ItemStack result, boolean isShape) {
		super(id, result);
		this.isShape = isShape;
		this.shape = new String[] {"ABC", "DEF", "GHI"};
		this.ings = new ItemStack[(int) Math.pow(this.shape.length, 2)];
		for (int i = 0; i < this.ings.length; i++) {
			this.ings[i] = new ItemStack(Material.AIR);
		}
	}
	
	public boolean isShaped() {
		return this.isShape;
	}
	
	public ItemStack[] getIngredients() {
		return this.ings;
	}
	
	@NotNull
	public String[] getShape() {
		return this.shape;
	}
	
	@Override
	public void addIngredient(int pos, @Nullable ItemStack item) {
		if (pos >= Math.pow(shape.length, 2)) {
			throw new IllegalArgumentException("Ingredient slot is out of shape size!");
		}
		
		if (item == null) item = new ItemStack(Material.AIR);
		this.ings[pos] = item;
	}
}
