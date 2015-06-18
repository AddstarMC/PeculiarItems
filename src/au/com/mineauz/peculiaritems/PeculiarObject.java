package au.com.mineauz.peculiaritems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarSubStat;

public class PeculiarObject {
	private ItemStack item;
	private List<String> stats = new ArrayList<String>();
	
	public PeculiarObject(ItemStack item){
		this.item = item;
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = null;
		if(meta.getLore() != null){
			lore = meta.getLore();
			for(int i = 1; i < lore.size(); i++){
				PeculiarStat stat = Main.getPlugin().getStats().matchStat(lore.get(i));
				if(stat != null)
					stats.add(stat.getName());
			}
		}
		
		if(meta.getDisplayName() == null || 
				!meta.getDisplayName().startsWith(ChatColor.RESET.toString() + 
						ChatColor.GOLD)){
			String name = PCRUtils.capitalize(item.getType().toString().replace("_", " "));
			if(meta.getDisplayName() != null)
				name = meta.getDisplayName();
			meta.setDisplayName(ChatColor.RESET.toString() + ChatColor.GOLD + "Peculiar " + 
				ChatColor.RESET + ChatColor.GOLD + name);
			
			item.setItemMeta(meta);
		}
	}
	
	/**
	 * Gets the item referred to by this peculiar object.
	 * @return The {@link PeculiarObject}.
	 */
	public ItemStack getItem(){
		return item;
	}
	
	/**
	 * Checks if this object has a specific stat.
	 * @param stat The stat to check.
	 * @return <code>true</code> if it has this stat.
	 */
	public boolean hasStat(String stat){
		return stats.contains(stat.toUpperCase());
	}
	
	/**
	 * Gets a {@link PeculiarStat} from this object.
	 * @param stat The stat to get.
	 * @return The {@link PeculiarStat} or <code>null</code> if the one requested hasn't been assigned.
	 */
	public PeculiarStat getStat(String stat){
		if(hasStat(stat))
			return Main.getPlugin().getStats().getStat(stat.toUpperCase());
		return null;
	}
	
	/**
	 * Adds a new {@link PeculiarStat} to this object.
	 * @param stat The stat to add.
	 */
	public void addStat(PeculiarStat stat){
		if(!stats.contains(stat.getName())){
			stats.add(stat.getName());
			stat.addStat(this, 0, stat.getStatLine(this));
		}
	}
	
	/**
	 * Gets all the stats on this item.
	 * @return a {@link List} of {@link PeculiarStat}s.
	 */
	public List<PeculiarStat> getAllStats(){
		List<PeculiarStat> stats = new ArrayList<PeculiarStat>();
		
		for(String stat : this.stats){
			stats.add(getStat(stat));
		}
		
		return stats;
	}
	
	/**
	 * Adds a sub statistic to a particular statistic.
	 * @param stat The statistic to add this stat to.
	 * @param special The name of this sub statistic.
	 */
	public void addSubStat(PeculiarStat stat, String special){
		PeculiarSubStat.addSubStat(this, 0, PeculiarSubStat.getSubStatLine(this, special), stat, special);
	}
	
	/**
	 * Gets all the sub statistics of a specific statistic.
	 * @param stat The stat to get any sub statistics from.
	 * @return A list containing all the sub statistic names or an empty list if there are none.
	 */
	public List<String> getAllSubStats(PeculiarStat stat){
		List<String> sub = new ArrayList<String>();
		if(hasStat(stat.getName())){
			int line = stat.getStatLine(this);
			List<String> lore = getItem().getItemMeta().getLore();
			for(int i = line + 1; i < lore.size(); i++){
				if(lore.get(i).startsWith("- "))
					sub.add(ChatColor.stripColor(lore.get(i)).replaceAll("(- )|(: [0-9]+)", ""));
				else
					break;
			}
		}
		return sub;
	}
	
	/**
	 * Gets the current rank of this object.
	 * @return The objects rank.
	 */
	public String getCurrentRank(){
		String rank = item.getItemMeta().getDisplayName();
		return rank.split(ChatColor.RESET.toString() + ChatColor.GOLD.toString())[1];
	}
	
	/**
	 * Gets the objects actual name without the rank.
	 * @return The objects name.
	 */
	public String getItemName(){
		return ChatColor.stripColor(item.getItemMeta().getDisplayName().replace(getCurrentRank(), ""));
	}
	
	/**
	 * Sets the items name and adds the rank in front of it automatically.
	 * @param name The new name for the object.
	 */
	public void setItemName(String name){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + meta.getDisplayName().replace(getItemName(), name));
		item.setItemMeta(meta);
	}
	
	/**
	 * Gets the primary {@link PeculiarStat} of this object. This is the one used for ranks.
	 * @return
	 */
	public PeculiarStat getPrimaryLevelStat(){
		if(!stats.isEmpty())
			return getStat(stats.get(0));
		return null;
	}
	
	/**
	 * Checks if a specific {@link PeculiarStat} is the primary stat for this object.
	 * @param stat The stat to check.
	 * @return <code>true</code> if this stat is the primary stat.
	 */
	public boolean isPrimaryLevelStat(PeculiarStat stat){
		if(!stats.isEmpty())
			if(stat.getName().equals(stats.get(0)))
				return true;
		return false;
	}
}
