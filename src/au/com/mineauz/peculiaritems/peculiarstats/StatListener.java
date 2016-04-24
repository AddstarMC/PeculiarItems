package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PeculiarItem;

public class StatListener implements Listener {
	@EventHandler
	private void blockBreak(BlockBreakEvent event) {
		ItemStack held = event.getPlayer().getInventory().getItemInMainHand();
		if (held == null) {
			return;
		}
		
		PeculiarItem item = new PeculiarItem(held);
		if (!item.isPeculiar()) {
			return;
		}
		
		PeculiarStat stat = PeculiarStats.BLOCKS_BROKEN.of(event.getBlock().getState().getData());
		item.incrementStat(stat, 1);
	}
	
	@EventHandler(ignoreCancelled = true)
	private void entityKill(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Monster)) {
			return;
		}
		
		// TODO: Track bows correctly. Record bow item upon firing
		LivingEntity ent = event.getEntity();
		if (ent.getKiller() == null) {
			return;
		}
		
		ItemStack held = ent.getKiller().getInventory().getItemInMainHand();
		if (held == null) {
			return;
		}
		
		PeculiarItem item = new PeculiarItem(held);
		if (!item.isPeculiar()) {
			return;
		}
		
		PeculiarStat stat = PeculiarStats.MOBS_KILLED.of(event.getEntityType());
		item.incrementStat(stat, 1);
	}
}
