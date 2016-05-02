package au.com.mineauz.peculiaritems;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Sets;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarSubStat;
import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	private void invClick(InventoryClickEvent event) {
		if (event.getClick() != ClickType.RIGHT || event.getCurrentItem() == null || event.getCursor() == null) {
			return;
		}
		
		if (event.getCurrentItem().getType() == Material.AIR || event.getCursor().getType() == Material.AIR) {
			return;
		}
		
		PeculiarModifier modifier = new PeculiarModifier(event.getCursor());
		PeculiarItem item = new PeculiarItem(event.getCurrentItem());
		
		if (!modifier.isPeculiar()) {
			return;
		}
		
		Set<PeculiarStat> toRemove = Sets.newHashSet();
		
		boolean addedAny = false;
		// Add the stats to the item
		for (PeculiarStat stat : modifier.getStats()) {
			if (!item.hasStat(stat) && stat.isCompatibleItem(item.getItemStack())) {
				// Add parent stat if needed
				if (stat instanceof PeculiarSubStat) {
					PeculiarStat parent = ((PeculiarSubStat)stat).getParent();
					
					if (!item.hasStat(parent)) {
						item.addStat(parent);
					}
				}
				
				item.addStat(stat);
				toRemove.add(stat);
				addedAny = true;
			}
		}
		
		// Check if the modifier would have any remaining stats after removing the used ones
		boolean hasRemainingStats = false;
		for (PeculiarStat stat : modifier.getStats()) {
			if (!toRemove.contains(stat)) {
				hasRemainingStats = true;
				break;
			}
		}
		
		if (!toRemove.isEmpty()) {
			// Handle multiple item stacks
			if (modifier.getItemStack().getAmount() > 1)
			{
				if (hasRemainingStats) {
					ItemStack clonedModifierStack = modifier.getItemStack().clone();
					clonedModifierStack.setAmount(1);
					PeculiarModifier clonedModifier = new PeculiarModifier(clonedModifierStack);
					
					// Remove the added stats from the modifier
					for (PeculiarStat stat : toRemove) {
						clonedModifier.removeStat(stat);
					}
					
					// Attempt to add the item back
					if (!event.getWhoClicked().getInventory().addItem(clonedModifier.getItemStack()).isEmpty()) {
						// Didnt fit, just drop it
						event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), clonedModifier.getItemStack());
					}
				}
				
				// decrease the amount held
				modifier.getItemStack().setAmount(modifier.getItemStack().getAmount() - 1);
			} else {
				if (hasRemainingStats) {
					// Remove the added stats from the modifier
					for (PeculiarStat stat : toRemove) {
						modifier.removeStat(stat);
					}
				} else {
					event.setCursor(null);
				}
			}
		}
		
		event.setCurrentItem(item.getItemStack());
		event.setCancelled(true);
		
		Player player = (Player)event.getWhoClicked();
		
		if (addedAny) {
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 10, 1);
		} else {
			player.sendMessage(ChatColor.RED + "That item can not accept this stat");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 1);
		}
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
	
	@EventHandler
	private void onOpenInventory(InventoryOpenEvent event) {
		if (!(event.getPlayer() instanceof Player)) {
			return;
		}
		
		if (event.getInventory().getType() != InventoryType.ANVIL && 
			event.getInventory().getType() != InventoryType.ENCHANTING) {
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
			
			if (event.getInventory().getType() == InventoryType.ANVIL) {
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
				stack.removeEnchantment(PeculiarEnchantment.getEnchantment());
				
			} else if (event.getInventory().getType() == InventoryType.ENCHANTING) {
				// Remove the shine enchant so it can be enchanted
				stack.removeEnchantment(PeculiarEnchantment.getEnchantment());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	private void onCloseInventory(final InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player)) {
			return;
		}
		
		if (event.getInventory().getType() != InventoryType.ANVIL &&
			event.getInventory().getType() != InventoryType.ENCHANTING) {
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
			item.addEnchantIfNeeded();
		}
		
		// Refresh the view
		Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(PeculiarItemsPlugin.class), new Runnable() {
			@Override
			public void run() {
				((Player)event.getPlayer()).updateInventory();
			}
		});
	}
	
	@EventHandler(ignoreCancelled=true)
	private void onDropItem(PlayerDropItemEvent event) {
		ItemStack stack = event.getItemDrop().getItemStack();
		PeculiarItem item = new PeculiarItem(stack);
		// Needs to be a peculiar item
		if (!item.isPeculiar()) {
			return;
		}
		
		// Just in case it was dropped from an anvil or enchanting table
		item.update();
		item.addEnchantIfNeeded();
	}
}
