package au.com.mineauz.peculiaritems;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.preculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.preculiaritems.peculiarstats.PeculiarStats;

public class Events implements Listener{
	
	@EventHandler(ignoreCancelled = true)
	private void blockBreak(BlockBreakEvent event){
		Player ply = event.getPlayer();
		
		if(PCRUtils.isPeculiarItem(ply.getItemInHand())){
			if(PeculiarStats.getStat("BLOCKS_BROKEN").hasStat(ply.getItemInHand())){
				PeculiarStats.getStat("BLOCKS_BROKEN").incrementStat(ply, ply.getItemInHand(), 1);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
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
	
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	private void invClick(InventoryClickEvent event){
		if(event.getClick().isRightClick() && 
				PCRUtils.isPeculiarModifier(event.getCursor()) &&
				event.getCurrentItem() != null){
			
			ItemStack item = event.getCurrentItem();
			ItemStack mod = event.getCursor();
			
			if(PCRUtils.matchMaterial(mod, item)){
				ItemStack nitem = item.clone();
				String type = item.getType().toString().split("_")[1];
				PCRUtils.setPeculiarItem(nitem);
				
				int adstats = 0;
				
				for(PeculiarStat stat : PeculiarStats.getAllStats()){
					if(stat.hasStat(mod) && stat.isCompatibleItem(type) && !stat.hasStat(nitem)){
						stat.addStat(nitem, 0);
						adstats++;
					}
				}
				
				if(adstats > 0){
					event.setCurrentItem(nitem);
					event.setCancelled(true);
					event.setCursor(null);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void playerHurt(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			Player ply = (Player)event.getEntity();
//			if(ply.getNoDamageTicks() == 0){
				for(ItemStack i : ply.getInventory().getArmorContents()){
					if(PCRUtils.isPeculiarItem(i)){
						if(PeculiarStats.getStat("TIMES_PROTECTED").hasStat(i)){
							PeculiarStats.getStat("TIMES_PROTECTED").incrementStat(ply, i, 1);
						}
					}
				}
//			}
		}
	}

}
