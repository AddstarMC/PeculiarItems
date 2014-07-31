package au.com.mineauz.peculiaritems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PCRUtils {
	
	private static List<String> items = new ArrayList<String>();
	
	static{
		items.add("AXE");
		items.add("BOOTS");
		items.add("CHESTPLATE");
		items.add("HELMET");
		items.add("HOE");
		items.add("LEGGINGS");
		items.add("PICKAXE");
		items.add("SPADE");
		items.add("SWORD");
	}
	
	public static boolean matchMaterial(ItemStack modifier, ItemStack item){
		String type = item.getType().toString().split("_")[1];
		Material needed = Material.getMaterial(modifier.getType().toString().split("_")[0] + "_" + type);
		if(needed != null || modifier.getType() == Material.NAME_TAG){
			if((item.getType() == needed || modifier.getType() == Material.NAME_TAG) && items.contains(type))
				return true;
		}
		return false;
	}
	
	public static String arrayToString(String[] arr){
		String st = ChatColor.GRAY + "";
		boolean alt = false;
		for(String s : arr){
			st += s;
			if(!arr[arr.length - 1 ].equals(s)){
				st += ", ";
				if(alt){
					st += ChatColor.GRAY;
					alt = false;
				}
				else{
					st += ChatColor.WHITE;
					alt = true;
				}
			}
		}
		return st;
	}
	
	public static boolean isPeculiarItem(ItemStack item){
		if(item == null || item.getType() == Material.AIR) return false;
		if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().get(0).equals(ChatColor.GOLD + "---Peculiar Item---")){
			return true;
		}
		return false;
	}
	
	public static boolean isPeculiarModifier(ItemStack item){
		if(item == null || item.getType() == Material.AIR) return false;
		if(item.getItemMeta().getLore() != null && 
				item.getItemMeta().getLore().get(0).equals(ChatColor.GOLD + "---Peculiar Modifier---") && 
				(item.getType() == Material.NAME_TAG || item.getType() == Material.DIAMOND || 
				item.getType() == Material.GOLD_INGOT || item.getType() == Material.IRON_INGOT)){
			return true;
		}
		return false;
	}
	
	public static void setPeculiarItem(ItemStack item){
		if(item == null) return;
		if(isPeculiarItem(item) || isPeculiarModifier(item)) return;
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.getLore() == null)
			lore = new ArrayList<String>();
		else
			lore = meta.getLore();
		
		meta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + "Peculiar " + getItemName(item));
		lore.add(0, ChatColor.GOLD + "---Peculiar Item---");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static void setPeculiarModifier(ItemStack item){
		if(item == null) return;
		if(isPeculiarItem(item) || isPeculiarModifier(item)) return;
		ItemMeta meta = item.getItemMeta();
		List<String> lore;
		if(meta.getLore() == null)
			lore = new ArrayList<String>();
		else
			lore = meta.getLore();
		
		meta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + "Peculiar " + getItemName(item));
		lore.add(0, ChatColor.GOLD + "---Peculiar Modifier---");
		lore.add(1, ChatColor.AQUA + "Right click an item");
		lore.add(2, ChatColor.AQUA + "in your inventory with");
		lore.add(3, ChatColor.AQUA + "this to apply the");
		lore.add(4, ChatColor.AQUA + "peculiar statistic.");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static String capitalize(String toCapitalize){
		String val = toCapitalize.toLowerCase();
		String[] spl = val.split(" ");
		val = "";
		for(String s : spl){
			String c = Character.toString(s.charAt(0));
			s = s.substring(1);
			c = c.toUpperCase();
			s = c + s;
			val += s + " ";
		}
		val = val.trim();
		return val;
	}
	
	public static String getItemName(ItemStack item){
		return capitalize(item.getType().toString().replace("_", " "));
	}
}
