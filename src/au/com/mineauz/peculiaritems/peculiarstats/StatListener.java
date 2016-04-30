package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
		item.incrementStatWithNotify(stat, 1, event.getPlayer());
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
		item.incrementStatWithNotify(stat, 1, ent.getKiller());
	}
	
	@EventHandler(ignoreCancelled = true)
	private void playerHurt(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player)event.getEntity();
		
		for (ItemStack armor : player.getEquipment().getArmorContents()) {
			if (armor == null || armor.getType() == Material.AIR) {
				continue;
			}
			
			PeculiarItem item = new PeculiarItem(armor);
			if (item.isPeculiar()) {
				item.incrementStatWithNotify(PeculiarStats.TIMES_PROTECTED, 1, player);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
	private void playerTakeDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player)event.getEntity();
		int amount = (int)event.getFinalDamage();
		
		if (amount <= 0) {
			return;
		}
		
		for (ItemStack armor : player.getEquipment().getArmorContents()) {
			if (armor == null || armor.getType() == Material.AIR) {
				continue;
			}
			
			PeculiarItem item = new PeculiarItem(armor);
			if (item.isPeculiar()) {
				item.incrementStatWithNotify(PeculiarStats.DAMAGE_TAKEN, amount, player);
			}
		}
	}
}
