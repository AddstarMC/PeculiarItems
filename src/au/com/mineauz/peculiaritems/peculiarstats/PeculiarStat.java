package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class PeculiarStat {
	public abstract String getName();
	public abstract ChatColor getDisplayColor();
	public abstract String getDisplayName();
	public abstract boolean isCompatibleItem(ItemStack item);
	
	public String toDisplayString(int value) {
		return getDisplayColor() + getDisplayName() + ": " + value; 
	}
}
