package au.com.mineauz.peculiaritems;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import au.com.addstar.monolith.MonoItemStack;
import au.com.addstar.monolith.StringTranslator;
import au.com.addstar.monolith.properties.IntegerProperty;
import au.com.addstar.monolith.properties.PropertyBase;
import au.com.addstar.monolith.properties.StringProperty;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarSubStat;
import au.com.mineauz.peculiaritems.peculiarstats.SubStatable;

public class PeculiarItem {
	private final MonoItemStack item;
	private final Map<PeculiarStat, Integer> stats;
	private PeculiarStat primaryStat;
	private String displayName;
	
	public PeculiarItem(ItemStack item) {
		this.item = new MonoItemStack(item);
		stats = Maps.newLinkedHashMap();
		
		loadStats();
	}
	
	private void loadStats() {
		for (PropertyBase<?> property : item.getProperties().getAllProperties(Constants.STORED_STATS_ID)) {
			String name = property.getName();
			int value = ((IntegerProperty)property).getValue();
			
			PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(name);
			if (stat != null) {
				stats.put(stat, value);
			}
		}
		
		displayName = item.getProperties().getString(Constants.FIELD_DISPLAYNAME, Constants.PECULIAR_DATA_ID);
		
		String primaryStatId = item.getProperties().getString(Constants.FIELD_PRIMARYSTAT, Constants.PECULIAR_DATA_ID);
		if (primaryStatId != null) {
			PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(primaryStatId);
			primaryStat = stat;
		}
	}
	
	public boolean isPeculiar() {
		return !stats.isEmpty();
	}
	
	public void addStat(PeculiarStat stat) {
		if (stat instanceof PeculiarSubStat) {
			if (!stats.containsKey(((PeculiarSubStat)stat).getParent())) {
				return;
			}
		}
		
		if (!stats.containsKey(stat)) {
			stats.put(stat, 0);
		}
		
		if (primaryStat == null) {
			primaryStat = stat;
		}

		addEnchantIfNeeded();
		checkAndUpdateName();
		saveStats();
		update();
	}
	
	public void setStat(PeculiarStat stat, int value) {
		if (!stats.containsKey(stat)) {
			return;
		}
		
		stats.put(stat, value);
		
		saveStats();
		update();
	}
	
	public void incrementStat(PeculiarStat stat, int value) {
		// Ensure that the parent is updated too, even if the subtype isnt added
		if (stat instanceof PeculiarSubStat) {
			incrementStat(((PeculiarSubStat)stat).getParent(), value);
		}
		
		if (!stats.containsKey(stat)) {
			return;
		}
		
		int oldValue = getStat(stat);
		setStat(stat, oldValue + value);
	}
	
	public void decrementStat(PeculiarStat stat, int value) {
		incrementStat(stat, -value);
	}
	
	public void incrementStatWithNotify(PeculiarStat stat, int value, Player player) {
		// Just do normal behaviour without a primary stat
		if (primaryStat == null) {
			incrementStat(stat, value);
			return;
		}
		
		// See if theres a rank change
		String currentRank = getRank();
		incrementStat(stat, value);
		String newRank = getRank();
		
		if (!newRank.equals(currentRank)) {
			// Level up!
			
			String itemName = getActualItemName();
			newRank = primaryStat.getDisplayColor() + newRank;
			
			// Display the rank up
			if (PeculiarItemsPlugin.getPlugin().isBroadcastingRankUp()) {
				Bukkit.broadcastMessage(ChatColor.AQUA + player.getDisplayName() + "'s " + ChatColor.ITALIC + itemName + ChatColor.AQUA + " has reached a new rank: " + newRank);
			} else {
				player.sendMessage(ChatColor.AQUA + "Your " + ChatColor.ITALIC + itemName + ChatColor.AQUA + " has reached a new rank: " + newRank);
			}
		}
	}
	
	public void removeStat(PeculiarStat stat) {
		if (!stats.containsKey(stat)) {
			return;
		}
		
		stats.remove(stat);
		
		// Remove sub stats
		if (stat instanceof SubStatable<?>) {
			List<PeculiarSubStat> subStats = Lists.newArrayList(Iterables.filter(stats.keySet(), PeculiarSubStat.class));
			for (PeculiarSubStat subStat : subStats) {
				if (subStat.getParent() == stat) {
					stats.remove(subStat);
				}
			}
		}
		
		if (stat.equals(primaryStat)) {
			primaryStat = null;
			
			// Try to assign a new primary
			for (PeculiarStat s : stats.keySet()) {
				if (!(s instanceof PeculiarSubStat)) {
					primaryStat = s;
					break;
				}
			}
		}
		
		saveStats();
		update();
	}
	
	public Collection<PeculiarStat> getStats() {
		return Collections.unmodifiableCollection(stats.keySet());
	}
	
	public int getStat(PeculiarStat stat) {
		Integer value = stats.get(stat);
		if (value == null) {
			return 0;
		} else {
			return value;
		}
	}
	
	public boolean hasStat(PeculiarStat stat) {
		return stats.containsKey(stat);
	}
	
	public void setDisplayName(String name) {
		displayName = name;
		saveStats();
		update();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public boolean hasDisplayName() {
		return displayName != null;
	}
	
	public String getActualItemName() {
		// Create an item to get the real name to display
		ItemStack tempItem = new ItemStack(getItemStack());
		ItemMeta meta = tempItem.getItemMeta();
		
		if (displayName != null) {
			meta.setDisplayName(displayName);
		} else {
			meta.setDisplayName(null);
		}
		
		meta.setLore(null);
		
		tempItem.setItemMeta(meta);
		return StringTranslator.getName(tempItem);
	}
	
	public void setPrimary(PeculiarStat stat) {
		Preconditions.checkArgument(!(stat instanceof PeculiarSubStat));
		primaryStat = stat;
		update();
	}
	
	private String getFormattedName() {
		String rank = getRank();
		
		if (displayName != null) {
			return ChatColor.GOLD + rank + " " + displayName;
		} else {
			return ChatColor.GOLD + rank + " " + getActualItemName();
		}
	}
	
	public void update() {
		ItemStack stack = getItemStack();
		ItemMeta meta = stack.getItemMeta();
		
		meta.setDisplayName(getFormattedName());
		
		// Display stats
		List<String> lore = Lists.newArrayList();
		for (PeculiarStat stat : stats.keySet()) {
			lore.add(stat.toDisplayString(stats.get(stat)));
		}
		
		meta.setLore(lore);
		
		stack.setItemMeta(meta);
	}
	
	public String getRank() {
		Data data = PeculiarItemsPlugin.getPlugin().getData();
		
		String rank = null;
		if (primaryStat != null) {
			int value = getStat(primaryStat);
			rank = data.getRank(value);
		}
		
		if (rank == null) {
			return "Peculiar";
		} else {
			return rank;
		}
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public void saveStats() {
		item.getProperties().clear(Constants.STORED_STATS_ID);
		item.getProperties().clear(Constants.PECULIAR_DATA_ID);
		
		// Now save PCR stuff
		for (PeculiarStat stat : stats.keySet()) {
			int value = stats.get(stat);
			
			item.getProperties().add(new IntegerProperty(stat.getName(), Constants.STORED_STATS_ID, value));
		}
		
		if (displayName != null) {
			item.getProperties().add(new StringProperty(Constants.FIELD_DISPLAYNAME, Constants.PECULIAR_DATA_ID, displayName));
		}
		
		if (primaryStat != null) {
			item.getProperties().add(new StringProperty(Constants.FIELD_PRIMARYSTAT, Constants.PECULIAR_DATA_ID, primaryStat.getName()));
		}
	}
	
	/**
	 * Checks if the actual display name matches what it should be, if not, update the stored name
	 * @return True if the name was changed
	 */
	public boolean checkAndUpdateName() {
		ItemMeta meta = item.getItemMeta();
		if (meta.hasDisplayName()) {
			String actual = meta.getDisplayName();
			String expected = getFormattedName();
			
			// Does the name match?
			if (!actual.equals(expected)) {
				displayName = actual;
				return true;
			}
		} else if (displayName != null) {
			displayName = null;
			return true;
		}
		
		return false;
	}
	
	public void addEnchantIfNeeded() {
		if (item.getEnchantmentLevel(PeculiarEnchantment.getEnchantment()) == 0) {
			item.addUnsafeEnchantment(PeculiarEnchantment.getEnchantment(), 1);
		}
	}
}
