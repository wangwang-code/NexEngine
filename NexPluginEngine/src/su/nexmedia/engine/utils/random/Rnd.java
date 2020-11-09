package su.nexmedia.engine.utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import su.nexmedia.engine.utils.CollectionsUT;

public class Rnd {
	
    public static final MTRandom rnd;
    
    private static final List<String> MATERIAL_COLORS;
    
    static {
	    rnd = new MTRandom();
	    
	    MATERIAL_COLORS = Arrays.asList(
				"WHITE",
				"BLACK",
				"ORANGE",
				"YELLOW",
				"RED",
				"GREEN",
				"LIME",
				"BLUE",
				"CYAN",
				"BROWN",
				"GRAY",
				"PURPLE",
				"PINK",
				"MAGENTA",
				"LIGHT_GRAY",
				"LIGHT_BLUE"
			);
	}

	public static float get() {
        return Rnd.rnd.nextFloat();
    }
    
    public static float get(boolean normalize) {
        float f = Rnd.get();
        if (normalize) f *= 100f;
        return f;
    }
    
    public static int get(int n) {
        return Rnd.nextInt(n);
    }
    
    public static int get(int min, int max) {
        return min + (int)Math.floor(Rnd.rnd.nextDouble() * (max - min + 1));
    }
    
	public static double getDouble(double min, double max) {
		return min + (max - min) * rnd.nextDouble();
	}
	
	public static double getDoubleNega(double min, double max) {
		double range = max - min;
		double scaled = rnd.nextDouble() * range;
		double shifted = scaled + min;
		return shifted;
	}
    
    public static boolean chance(int chance) {
        return chance >= 1 && (chance > 99 || nextInt(99) + 1 <= chance);
    }
    
    public static boolean chance(double chance) {
        return nextDouble() <= chance / 100.0;
    }
    
    @NotNull
    public static <E> E get(@NotNull E[] list) {
        return list[get(list.length)];
    }
    
    public static int get(int[] list) {
        return list[get(list.length)];
    }
    
    @Nullable
    public static <E> E get(@NotNull List<E> list) {
        return list.isEmpty() ? null : list.get(get(list.size()));
    }
    
    @Nullable
	public static <T> List<T> getItemsByWeight(@NotNull Map<T, Double> src, boolean once) {
		if (src.isEmpty()) return Collections.emptyList();
		Set<T> set = new HashSet<>(); // Final drop list
		
		for (Entry<T, Double> en : src.entrySet()) {
			// Negative chance always drop
			if (en.getValue() <= 0) {
				set.add(en.getKey());
				continue;
			}
		}
		
		int amount = 1;
		if (!once) amount = src.size();
		
		for (int i = 0; i < amount; i++) {
			@SuppressWarnings("null")
			T item = Rnd.getRandomItem(src, false);
			if (item != null) {
				set.add(item);
			}
		}
		
		return new ArrayList<>(set);
	}
    
	@Nullable
    public static <T> T getRandomItem(@NotNull Map<T, Double> map) {
    	return getRandomItem(map, true);
    }
    
	@Nullable
    public static <T> T getRandomItem(@NotNull Map<T, Double> map, boolean alwaysHundred) {
		if (map.isEmpty()) return null;
		
    	map = CollectionsUT.sortByValue(map); // Sort for chance order
    	
		if (alwaysHundred) {
			List<T> fix = new ArrayList<>();
			for (Entry<T, Double> e : map.entrySet()) {
				if (e.getValue() >= 100D) {
					fix.add(e.getKey());
					//return e.getKey();
				}
			}
			if (!fix.isEmpty()) {
				return fix.get(Rnd.nextInt(fix.size()));
			}
		}
		
	    List<T> items = new ArrayList<>(map.keySet());
	    double totalSum = 0;

	    for(double d : map.values()) {
	        totalSum = totalSum + d;
	    }

	    double index = Rnd.getDouble(0, totalSum);
	    double sum = 0;
	    int i = 0;
	    while(sum < index) {
	        sum = sum + map.get(items.get(i++));
	    }
	    return items.get(Math.max(0,i-1));
	}
    
    
    
    
    public static int nextInt(int n) {
        return (int)Math.floor(Rnd.rnd.nextDouble() * n);
    }
    
    public static int nextInt() {
        return Rnd.rnd.nextInt();
    }
    
    public static double nextDouble() {
        return Rnd.rnd.nextDouble();
    }
    
    public static double nextGaussian() {
        return Rnd.rnd.nextGaussian();
    }
    
    public static boolean nextBoolean() {
        return Rnd.rnd.nextBoolean();
    }
    
    
    
    
    @NotNull
    public static Firework spawnRandomFirework(@NotNull Location loc) {
    	World w = loc.getWorld();
    	if (w == null) w = Bukkit.getWorlds().get(0);
    	
        Firework fw = (Firework) w.spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = fw.getFireworkMeta();
        FireworkEffect.Type type = Rnd.get(FireworkEffect.Type.values());
        Color c1 = Color.fromBGR(Rnd.nextInt(254), Rnd.nextInt(254), Rnd.nextInt(254));
        Color c2 = Color.fromBGR(Rnd.nextInt(254), Rnd.nextInt(254), Rnd.nextInt(254));
        FireworkEffect effect = FireworkEffect.builder().flicker(Rnd.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(Rnd.nextBoolean()).build();
        meta.addEffect(effect);
        
        int power = Rnd.get(5);
        meta.setPower(power);
        fw.setFireworkMeta(meta);
        
        return fw;
    }
    
    @NotNull
    public static Material getColoredMaterial(@NotNull Material m) {
    	String name = m.name();
    	for (String c : Rnd.MATERIAL_COLORS) {
    		if (name.startsWith(c)) {
    			String color = Rnd.get(Rnd.MATERIAL_COLORS);
    			name = name.replace(c, color);
    			break;
    		}
    	}
    	
    	Material get = Material.getMaterial(name);
    	if (get == null) {
    		return Material.BARRIER;
    	}
    	return get;
    }
}
