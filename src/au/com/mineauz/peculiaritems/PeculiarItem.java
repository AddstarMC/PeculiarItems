package au.com.mineauz.peculiaritems;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.attributes.Attributes;
import com.comphenix.attributes.Attributes.Attribute;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import au.com.addstar.monolith.StringTranslator;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarSubStat;
import au.com.mineauz.peculiaritems.peculiarstats.SubStatable;

public class PeculiarItem {
	private final Attributes storage;
	private final Map<PeculiarStat, Integer> stats;
	private PeculiarStat primaryStat;
	private String displayName;
	
	public PeculiarItem(ItemStack item) {
		storage = new Attributes(item);
		stats = Maps.newHashMap();
		
		loadStats();
	}
	
	private void loadStats() {
		for (Attribute attribute : storage.values()) {
			if (attribute.getName().startsWith("PCS|")) {
				String[] parts = attribute.getName().split("\\|");
				String name = parts[1];
				int value = Integer.parseInt(parts[2]);
				
				PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(name.toUpperCase());
				if (stat != null) {
					stats.put(stat, value);
				}
			}
		}
		
		Attribute nameAttribute = PCRUtils.findAttribute(storage, Constants.DISPLAYNAME_ID);
		if (nameAttribute != null) {
			displayName = nameAttribute.getName();
		}
		
		Attribute primaryAttribute = PCRUtils.findAttribute(storage, Constants.PRIMARYSTAT_ID);
		if (primaryAttribute != null) {
			String statName = primaryAttribute.getName();
			PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(statName.toUpperCase());
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
			
			// Create an item to get the real name to display
			ItemStack tempItem = new ItemStack(getItemStack());
			if (displayName != null) {
				ItemMeta meta = tempItem.getItemMeta();
				meta.setDisplayName(displayName);
				meta.setLore(null);
				tempItem.setItemMeta(meta);
			}
			
			String itemName = StringTranslator.getName(tempItem);
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
	
	public void setPrimary(PeculiarStat stat) {
		Preconditions.checkArgument(!(stat instanceof PeculiarSubStat));
		primaryStat = stat;
		update();
	}
	
	public void update() {
		ItemStack stack = getItemStack();
		ItemMeta meta = stack.getItemMeta();
		
		String rank = getRank();
		
		if (displayName != null) {
			meta.setDisplayName(ChatColor.GOLD + rank + " " + displayName);
		} else {
			String itemName = StringTranslator.getName(getItemStack());
			meta.setDisplayName(ChatColor.GOLD + rank + " " + itemName);
		}
		
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
		
		if (primaryStat != null) {
			int value = getStat(primaryStat);
			return data.getRank(value);
		} else {
			return "Peculiar";
		}
	}
	
	public ItemStack getItemStack() {
		return storage.getStack();
	}
	
	private void saveStats() {
		for (PeculiarStat stat : stats.keySet()) {
			int value = stats.get(stat);
			
			String attributeName = "PCS|" + stat.getName() + "|" + value;
			UUID attributeUUID = UUID.nameUUIDFromBytes(stat.getName().getBytes());
			
			PCRUtils.setAttribute(storage, attributeUUID, attributeName);
		}
		
		if (displayName != null) {
			PCRUtils.setAttribute(storage, Constants.DISPLAYNAME_ID, displayName);
		}
		
		if (primaryStat != null) {
			PCRUtils.setAttribute(storage, Constants.PRIMARYSTAT_ID, primaryStat.getName());
		}
	}
}
