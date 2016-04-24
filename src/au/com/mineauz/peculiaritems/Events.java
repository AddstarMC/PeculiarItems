package au.com.mineauz.peculiaritems;

import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.google.common.collect.Sets;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class Events implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	private void invClick(InventoryClickEvent event) {
		if (event.getClick() != ClickType.RIGHT || event.getCurrentItem() == null || event.getCursor() == null) {
			return;
		}
		
		PeculiarModifier modifier = new PeculiarModifier(event.getCursor());
		PeculiarItem item = new PeculiarItem(event.getCurrentItem());
		
		if (!modifier.isPeculiar()) {
			return;
		}
		
		
		// TODO: Item type check. ie. gold for gold items, diamond for diamond items, etc.
		
		Set<PeculiarStat> toRemove = Sets.newHashSet();
		
		// Add the stats to the item
		for (PeculiarStat stat : modifier.getStats()) {
			if (!item.hasStat(stat) && stat.isCompatibleItem(item.getItemStack())) {
				item.addStat(stat);
				toRemove.add(stat);
			}
		}
		
		// Remove the added stats from the modifier
		for (PeculiarStat stat : toRemove) {
			modifier.removeStat(stat);
		}
		
		if (modifier.getStats().isEmpty()) {
			// Remove it
			event.setCursor(null);
		} else {
			event.setCursor(modifier.getItemStack());
		}
		
		event.setCurrentItem(item.getItemStack());
		event.setCancelled(true);
	}

}
