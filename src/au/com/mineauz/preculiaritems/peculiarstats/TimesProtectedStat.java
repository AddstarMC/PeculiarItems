package au.com.mineauz.preculiaritems.peculiarstats;

import org.bukkit.ChatColor;

public class TimesProtectedStat extends PeculiarStat{

	@Override
	public String getName() {
		return "TIMES_PROTECTED";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.GRAY;
	}

	@Override
	public String getDisplayName() {
		return "Times Protected";
	}

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("LEGGINGS") || type.equals("HELMET") || type.equals("CHESTPLATE") || type.equals("BOOTS"))
			return true;
		return false;
	}

}
