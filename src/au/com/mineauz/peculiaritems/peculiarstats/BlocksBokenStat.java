package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;

public class BlocksBokenStat extends PeculiarStat implements Listener{

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
	
	@EventHandler
	private void blockBreak(BlockBreakEvent event){
		PCRPlayer ply = Main.getPlugin().getData().getPlayer(event.getPlayer());
		if(ply == null) return;
		
		ply.incrementActiveItemStat(getName(), 1);
	}

}
