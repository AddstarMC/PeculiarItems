package au.com.mineauz.peculiaritems;

import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.google.common.collect.Sets;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class Events implements Listener{
	
	private PeculiarItemsPlugin plugin = PeculiarItemsPlugin.getPlugin();
	
	@EventHandler
	private void playerLogin(PlayerJoinEvent event){
		//Add player when they join
		plugin.getData().addPlayer(event.getPlayer());
	}
	
	@EventHandler
	private void playerQuit(PlayerQuitEvent event){
		//Remove player when they quit
		plugin.getData().removePlayer(event.getPlayer());
	}
	
	@EventHandler
	private void itemchange(PlayerItemHeldEvent event){
		PCRPlayer player = plugin.getData().getPlayer(event.getPlayer());
		if(player == null) return;
		
		//Check if new active item is peculiar
		if(PCRUtils.isPeculiarItem(player.getItem(event.getNewSlot()))){
			player.setActiveItem(new PeculiarItem(player.getItem(event.getNewSlot())));
		}
		else
			player.setActiveItem(null);
	}
	
	@EventHandler
	private void inventoryClose(InventoryCloseEvent event){
		PCRPlayer player = plugin.getData().getPlayer(event.getPlayer().getUniqueId());
		if(player == null) return;
		
		//Check for peculiar armor
		player.updatePeculiarArmor();
		
		//Check active item is peculiar
		if(PCRUtils.isPeculiarItem(player.getItemInHand()))
			player.setActiveItem(new PeculiarItem(player.getItemInHand()));
		else
			player.setActiveItem(null);
	}
	
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
