package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class PeculiarSubStat extends PeculiarStat {
	private final PeculiarStat parent;
	
	public PeculiarSubStat(PeculiarStat parent) {
		this.parent = parent;
	}
	
	public final PeculiarStat getParent() {
		return parent;
	}
	
	@Override
	public ChatColor getDisplayColor() {
		return parent.getDisplayColor();
	}
	
	@Override
	public final boolean isCompatibleItem(ItemStack item) {
		return parent.isCompatibleItem(item);
	}
	
	public abstract String getSubName();
	
	@Override
	public final String getName() {
		return parent.getName() + "[" + getSubName();
	}
	
	@Override
	public String toDisplayString(int value) {
		return " " + getDisplayColor() + getDisplayName() + ": " + value;
	}
}
