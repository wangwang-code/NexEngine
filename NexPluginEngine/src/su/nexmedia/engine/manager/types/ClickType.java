package su.nexmedia.engine.manager.types;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public enum ClickType {
	
	LEFT,
	RIGHT,
	MIDDLE,
	SHIFT_LEFT,
	SHIFT_RIGHT,
	;
	
	@NotNull
	public static ClickType from(@NotNull InventoryClickEvent e) {
		if (e.isShiftClick()) {
			if (e.isLeftClick()) return SHIFT_LEFT;
			else return SHIFT_RIGHT;
		}
		if (e.getClick() == org.bukkit.event.inventory.ClickType.MIDDLE) {
			return MIDDLE;
		}
		if (e.isRightClick()) {
			return RIGHT;
		}
		return LEFT;
	}
}
