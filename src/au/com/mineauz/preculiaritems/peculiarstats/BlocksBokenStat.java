package au.com.mineauz.preculiaritems.peculiarstats;

import org.bukkit.ChatColor;

public class BlocksBokenStat extends PeculiarStat{

	@Override
	public String getName() {
		return "BLOCKS_BROKEN";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public String getDisplayName() {
		return "Blocks Broken";
	}

}
