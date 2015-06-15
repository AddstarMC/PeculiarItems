package au.com.mineauz.peculiaritems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PeculiarItem extends PeculiarObject{
	
	public PeculiarItem(ItemStack item){
		super(item);
		
		ItemMeta meta = item.getItemMeta();
		if(meta.getLore() == null){
			createPeculiarItem(item);
		}
	}
	
	public static void createPeculiarItem(ItemStack item){
		if(!PCRUtils.isPeculiarItem(item)){
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			lore.add(0, ChatColor.GOLD + "---Peculiar Item---");
			
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}
}
