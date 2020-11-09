package su.nexmedia.engine.core.config;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import su.nexmedia.engine.NexEngine;
import su.nexmedia.engine.config.api.ILangTemplate;
import su.nexmedia.engine.utils.StringUT;

public class CoreLang extends ILangTemplate {

	public CoreLang(@NotNull NexEngine plugin) {
		super(plugin);
	}

	@Override
	protected void setupEnums() {
		this.setupEnum(EntityType.class);
		this.setupEnum(Material.class);
		
		for (PotionEffectType type : PotionEffectType.values()) {
			this.config.addMissing("PotionEffectType." + type.getName(), StringUT.capitalizeFully(type.getName().replace("_", " ")));
		}
		
		for (Enchantment e : Enchantment.values()) {
			this.config.addMissing("Enchantment." + e.getKey().getKey(), StringUT.capitalizeFully(e.getKey().getKey().replace("_", " ")));
		}
	}
}
