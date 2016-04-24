package au.com.mineauz.peculiaritems;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

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
	private void invClick(InventoryClickEvent event){
		if(event.getClick().isRightClick() && 
				PCRUtils.isPeculiarModifier(event.getCursor()) &&
				event.getCurrentItem() != null){
			
			ItemStack item = event.getCurrentItem();
			PeculiarModifier mod = new PeculiarModifier(event.getCursor());
			
			if(PCRUtils.matchMaterial(mod.getItem(), item)){
				ItemStack nitem = item.clone();
				String type = item.getType().toString().split("_")[1];
				PeculiarItem pitem = new PeculiarItem(nitem);
				
				for(PeculiarStat stat : mod.getAllStats()){
					if(!pitem.hasStat(stat) && stat.isCompatibleItem(type)){
						pitem.addStat(stat);
					}
				}
				
				event.setCurrentItem(nitem);
				event.setCancelled(true);
				event.setCursor(null);
			}
		}
	}

}
