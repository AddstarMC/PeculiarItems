package au.com.mineauz.peculiaritems;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.attributes.Attributes;
import com.comphenix.attributes.Attributes.Attribute;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class PeculiarModifier {
	private final Attributes storage;
	private final Set<PeculiarStat> stats;
	private PeculiarStat primaryStat;
	
	public PeculiarModifier(ItemStack item) {
		stats = Sets.newHashSet();
		storage = new Attributes(item);
		loadStats();
	}
	
	private void loadStats() {
		for (Attribute attribute : storage.values()) {
			if (attribute.getName().startsWith("PCM|")) {
				String[] parts = attribute.getName().split("\\|");
				String name = parts[1];
				
				PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(name.toUpperCase());
				if (stat != null) {
					stats.add(stat);
				}
			}
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
	
	public ItemStack getItemStack() {
		return storage.getStack();
	}
	
	public void addStat(PeculiarStat stat) {
		stats.add(stat);
		
		update();
		saveStats();
	}
	
	public void removeStat(PeculiarStat stat) {
		stats.remove(stat);
		
		update();
		saveStats();
	}
	
	public Set<PeculiarStat> getStats() {
		return Collections.unmodifiableSet(stats);
	}
	
	public void update() {
		ItemStack item = getItemStack();
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = Lists.newArrayList();
		lore.add(ChatColor.GRAY + "Right click an item");
		lore.add(ChatColor.GRAY + "in your inventory with");
		lore.add(ChatColor.GRAY + "this to apply the");
		lore.add(ChatColor.GRAY + "peculiar statistic.");
		lore.add("");
		
		lore.add(ChatColor.GOLD + "Stats Contained: ");
		for (PeculiarStat stat : stats) {
			lore.add(" " + stat.getDisplayColor() + stat.getDisplayName());
		}
		
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.GOLD + "Peculiar Modifier");
		item.setItemMeta(meta);
	}
	
	private void saveStats() {
		for (PeculiarStat stat : stats) {
			String attributeName = "PCM|" + stat.getName();
			UUID attributeUUID = UUID.nameUUIDFromBytes(stat.getName().getBytes());
			
			PCRUtils.setAttribute(storage, attributeUUID, attributeName);
		}
		
		if (primaryStat != null) {
			PCRUtils.setAttribute(storage, Constants.PRIMARYSTAT_ID, primaryStat.getName());
		}
	}
}
