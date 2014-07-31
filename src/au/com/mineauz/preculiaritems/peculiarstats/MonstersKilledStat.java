package au.com.mineauz.preculiaritems.peculiarstats;

import org.bukkit.ChatColor;

public class MonstersKilledStat extends PeculiarStat{

	@Override
	public String getName() {
		return "MONSTERS_KILLED";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.RED;
	}

	@Override
	public String getDisplayName() {
		return "Monsters Killed";
	}

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("SWORD") || type.equals("AXE"))
			return true;
		return false;
	}

}
