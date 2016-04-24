package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;

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
	public final boolean isCompatibleItem(String type) {
		return parent.isCompatibleItem(type);
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
