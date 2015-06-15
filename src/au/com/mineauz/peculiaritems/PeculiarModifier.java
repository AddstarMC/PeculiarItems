package au.com.mineauz.peculiaritems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PeculiarModifier extends PeculiarObject{
	
	public PeculiarModifier(ItemStack item) {
		super(item);
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = null;
		if(meta.getLore() == null){
			lore = new ArrayList<String>();
			
			lore.add(0, ChatColor.GOLD + "---Peculiar Modifier---");
			lore.add(1, ChatColor.AQUA + "Right click an item");
			lore.add(2, ChatColor.AQUA + "in your inventory with");
			lore.add(3, ChatColor.AQUA + "this to apply the");
			lore.add(4, ChatColor.AQUA + "peculiar statistic.");
			
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

}
