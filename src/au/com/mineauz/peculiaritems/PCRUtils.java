package au.com.mineauz.peculiaritems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
	
	/**
	 * Automatically assembles a tab complete array for the use in commands, matching a given string.
	 * @param orig The full list to match the string to
	 * @param match The string used to match
	 * @return A list of possible tab completions
	 */
	public static List<String> tabCompleteMatch(List<String> orig, String match){
		if(match.equals(""))
			return orig;
		else{
			List<String> ret = new ArrayList<String>(orig.size());
			for(String m : orig){
				if(m.toLowerCase().startsWith(match.toLowerCase()))
					ret.add(m);
			}
			return ret;
		}
	}
	
	public static ToolType getType(ItemStack item) {
		switch (item.getType()) {
		case WOOD_PICKAXE:
		case STONE_PICKAXE:
		case IRON_PICKAXE:
		case GOLD_PICKAXE:
		case DIAMOND_PICKAXE:
			return ToolType.Pickaxe;
		
		case WOOD_AXE:
		case STONE_AXE:
		case IRON_AXE:
		case GOLD_AXE:
		case DIAMOND_AXE:
			return ToolType.Axe;
			
		case WOOD_HOE:
		case STONE_HOE:
		case IRON_HOE:
		case GOLD_HOE:
		case DIAMOND_HOE:
			return ToolType.Hoe;
			
		case WOOD_SPADE:
		case STONE_SPADE:
		case IRON_SPADE:
		case GOLD_SPADE:
		case DIAMOND_SPADE:
			return ToolType.Shovel;
			
		case WOOD_SWORD:
		case STONE_SWORD:
		case IRON_SWORD:
		case GOLD_SWORD:
		case DIAMOND_SWORD:
			return ToolType.Sword;
			
		case SHEARS:
			return ToolType.Shears;
			
		default:
			return ToolType.None;
		}
	}
	
	public enum ToolType {
		None,
		Pickaxe,
		Axe,
		Hoe,
		Shovel,
		Sword,
		Shears
	}
	
	public static ArmorType getArmorType(ItemStack item) {
		switch (item.getType()) {
		case LEATHER_HELMET:
		case IRON_HELMET:
		case CHAINMAIL_HELMET:
		case GOLD_HELMET:
		case DIAMOND_HELMET:
			return ArmorType.Helmet;
			
		case LEATHER_CHESTPLATE:
		case IRON_CHESTPLATE:
		case CHAINMAIL_CHESTPLATE:
		case GOLD_CHESTPLATE:
		case DIAMOND_CHESTPLATE:
			return ArmorType.Chestplate;
			
		case LEATHER_LEGGINGS:
		case IRON_LEGGINGS:
		case CHAINMAIL_LEGGINGS:
		case GOLD_LEGGINGS:
		case DIAMOND_LEGGINGS:
			return ArmorType.Leggings;
			
		case LEATHER_BOOTS:
		case IRON_BOOTS:
		case CHAINMAIL_BOOTS:
		case GOLD_BOOTS:
			return ArmorType.None;
		case DIAMOND_BOOTS:
			return ArmorType.Boots;
			
		case SHIELD:
			return ArmorType.Shield;
			
		default:
			return ArmorType.None;
		}
	}
	
	public enum ArmorType {
		None,
		Helmet,
		Chestplate,
		Leggings,
		Boots,
		Shield
	}
	
}
