package au.com.mineauz.peculiaritems;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

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
	
	@EventHandler
	private void entityKill(EntityDeathEvent event){
		if(event.getEntity() instanceof Monster){
			LivingEntity ent = event.getEntity();
			if(ent.getKiller() != null){
				Player ply = ent.getKiller();
				if(PCRUtils.isPeculiarItem(ply.getItemInHand())){
					if(PeculiarStats.getStat("MONSTERS_KILLED").hasStat(ply.getItemInHand())){
						PeculiarStats.getStat("MONSTERS_KILLED").incrementStat(ply, ply.getItemInHand(), 1);
					}
				}
			}
		}
	}

}
