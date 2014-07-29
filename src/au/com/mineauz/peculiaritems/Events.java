package au.com.mineauz.peculiaritems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import au.com.mineauz.preculiaritems.peculiarstats.PeculiarStats;

public class Events implements Listener{
	
	@EventHandler
	private void blockBreak(BlockBreakEvent event){
		Player ply = event.getPlayer();
		
		if(PCRUtils.isPeculiarItem(ply.getItemInHand())){
			if(PeculiarStats.getStat("BLOCKS_BROKEN").hasStat(ply.getItemInHand())){
				PeculiarStats.getStat("BLOCKS_BROKEN").incrementStat(ply, ply.getItemInHand(), 1);
			}
		}
	}

}
