package au.com.mineauz.peculiaritems;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Sets;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import net.md_5.bungee.api.ChatColor;

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
	
	@EventHandler
	private void onItemRename(PrepareAnvilEvent event) {
		ItemStack resultStack = event.getResult();
		
		if (resultStack == null || resultStack.getType() == Material.AIR) {
			return;
		}
		
		PeculiarItem item = new PeculiarItem(resultStack);
		// Needs to be a peculiar item
		if (!item.isPeculiar()) {
			return;
		}
		
		if (item.checkAndUpdateName()) {
			item.saveStats();
		}
		
		item.update();
		
		// Force the item to have a normal name for renaming purposes
		ItemMeta meta = resultStack.getItemMeta();
		
		if (item.hasDisplayName()) {
			meta.setDisplayName(item.getDisplayName());
		} else {
			meta.setDisplayName(null);
		}
		
		List<String> lore = meta.getLore();
		lore.add(0, ChatColor.GOLD + item.getRank() + " " + item.getActualItemName());
		meta.setLore(lore);
		
		resultStack.setItemMeta(meta);
		
		event.setResult(item.getItemStack());
	}
	
	@EventHandler()
	private void onOpenAnvil(InventoryOpenEvent event) {
		if (event.getInventory().getType() != InventoryType.ANVIL || !(event.getPlayer() instanceof Player)) {
			return;
		}
		
		// Reformat all peculiar items so that they will have plain names for renaming
		for (ItemStack stack : event.getPlayer().getInventory()) {
			if (stack == null) {
				continue;
			}
			
			PeculiarItem item = new PeculiarItem(stack);
			// Needs to be a peculiar item
			if (!item.isPeculiar()) {
				continue;
			}
			
			// Force the item to have a normal name for renaming purposes
			ItemMeta meta = stack.getItemMeta();
			
			if (item.hasDisplayName()) {
				meta.setDisplayName(item.getDisplayName());
			} else {
				meta.setDisplayName(null);
			}
			
			List<String> lore = meta.getLore();
			lore.add(0, ChatColor.GOLD + item.getRank() + " " + item.getActualItemName());
			meta.setLore(lore);
			
			stack.setItemMeta(meta);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	private void onCloseAnvil(final InventoryCloseEvent event) {
		if (event.getInventory().getType() != InventoryType.ANVIL || !(event.getPlayer() instanceof Player)) {
			return;
		}
		
		// Reformat all items to their real appearance
		for (ItemStack stack : event.getPlayer().getInventory()) {
			if (stack == null || stack.getType() == Material.AIR) {
				continue;
			}
			
			PeculiarItem item = new PeculiarItem(stack);
			// Needs to be a peculiar item
			if (!item.isPeculiar()) {
				continue;
			}
			
			item.update();
		}
		
		// Refresh the view
		Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(PeculiarItemsPlugin.class), new Runnable() {
			@Override
			public void run() {
				((Player)event.getPlayer()).updateInventory();
			}
		});
	}

}
