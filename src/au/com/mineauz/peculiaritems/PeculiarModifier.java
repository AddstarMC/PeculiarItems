package au.com.mineauz.peculiaritems;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import au.com.addstar.monolith.MonoItemStack;
import au.com.addstar.monolith.properties.PropertyBase;
import au.com.addstar.monolith.properties.StringProperty;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class PeculiarModifier {
	private final MonoItemStack item;
	private final Set<PeculiarStat> stats;
	private PeculiarStat primaryStat;
	
	public PeculiarModifier(ItemStack item) {
		this.item = new MonoItemStack(item);
		stats = Sets.newHashSet();
		loadStats();
	}
	
	private void loadStats() {
		for (PropertyBase<?> property : item.getProperties().getAllProperties(Constants.PENDING_STATS_ID)) {
			PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(property.getName());
			if (stat != null) {
				stats.add(stat);
			}
		}
		
		String primaryName = item.getProperties().getString(Constants.FIELD_PRIMARYSTAT, Constants.PECULIAR_DATA_ID);
		if (primaryName != null) {
			PeculiarStat stat = PeculiarItemsPlugin.getPlugin().getStats().loadStat(primaryName);
			primaryStat = stat;
		}
	}
	
	public boolean isPeculiar() {
		return !stats.isEmpty();
	}
	
	public ItemStack getItemStack() {
		return item;
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
		item.getProperties().clear(Constants.PECULIAR_DATA_ID);
		item.getProperties().clear(Constants.PENDING_STATS_ID);
		
		for (PeculiarStat stat : stats) {
			item.getProperties().add(new StringProperty(stat.getName(), Constants.PENDING_STATS_ID, ""));
		}
		
		if (primaryStat != null) {
			item.getProperties().add(new StringProperty(Constants.FIELD_PRIMARYSTAT, Constants.PECULIAR_DATA_ID, primaryStat.getName()));
		}
	}
}
