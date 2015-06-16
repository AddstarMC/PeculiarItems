package au.com.mineauz.peculiaritems.peculiarstats;

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

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("PICKAXE") || type.equals("AXE") || type.equals("SPADE"))
			return true;
		return false;
	}

}
