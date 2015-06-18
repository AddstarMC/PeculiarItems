package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.peculiaritems.PeculiarObject;

public class PeculiarSubStat {
	
	/**
	 * Increments a sub statistics value.
	 * @param item The {@link PeculiarObject} to increment the statistic of.
	 * @param stat The stat that holds the sub statistic.
	 * @param amount The amount to increment by.
	 * @param special The name of the sub statistic.
	 */
	public static void incrementSubStat(PeculiarObject item, PeculiarStat stat, int amount, String special){
		ItemMeta meta = item.getItem().getItemMeta();
		if(!hasSubStat(item, special)) return;
		
		int line = getSubStatLine(item, special);
		int inc = getSubStatLevel(item, line, special);
		
		inc += amount;
		
		addSubStat(item, inc, line, stat, special);
		meta = item.getItem().getItemMeta();
		
		item.getItem().setItemMeta(meta);
	}
	
	/**
	 * Sets the lore of the item for sub statistics.
	 * @param item The {@link PeculiarObject} to add the statistic to.
	 * @param amount The amount to set on the object.
	 * @param line The lore line that this statistic is on. (-1 if no line yet)
	 * @param stat The stat that holds this sub stat.
	 * @param special The name of the sub stat.
	 */
	public static void addSubStat(PeculiarObject item, int amount, int line, PeculiarStat stat, String special){
		ItemMeta meta = item.getItem().getItemMeta();
		List<String> lore = meta.getLore();
		
		if(line != -1){
			lore.set(line, "- " + stat.getDisplayColor() + special + ": " + amount);
		}
		else{
			line = stat.getStatLine(item) + 1;
			lore.add(line, "- " + stat.getDisplayColor() + special + ": " + amount);
		}
		
		meta.setLore(lore);
		item.getItem().setItemMeta(meta);
	}
	
	/**
	 * Gets the line that the sub statistic is on.
	 * @param item The {@link PeculiarObject} that holds the statistic.
	 * @param special The name of the sub statistic.
	 * @return The line number or -1 if it hasn't been assigned one.
	 */
	public static int getSubStatLine(PeculiarObject item, String special){
		int line = -1;
		List<String> lore = item.getItem().getItemMeta().getLore();
		for(String l : lore){
			if(ChatColor.stripColor(l).matches("- " + special + ": " + "[0-9]+")){
				line = item.getItem().getItemMeta().getLore().indexOf(l);
				break;
			}
		}
		
		return line;
	}
	
	/**
	 * Gets the level value of this sub statistic.
	 * @param item The {@link PeculiarObject} to get the sub statistic from.
	 * @param loreLine The line this statistic is on.
	 * @param special The name of the sub statistic.
	 * @return The level value of this statistic.
	 */
	public static int getSubStatLevel(PeculiarObject item, int loreLine, String special){
		ItemMeta meta = item.getItem().getItemMeta();
		return Integer.valueOf(ChatColor.stripColor(
				meta.getLore().get(loreLine)).replace("- " + special + ": ", ""));
	}
	
	/**
	 * Checks if an item has a specific sub statistic.
	 * @param item The {@link PeculiarObject} to check.
	 * @param special The name of the sub statistic.
	 * @return <code>true</code> if it has this statistic.
	 */
	public static boolean hasSubStat(PeculiarObject item, String special){
		return getSubStatLine(item, special) != -1;
	}
	
}
