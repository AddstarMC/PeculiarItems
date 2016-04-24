package au.com.mineauz.peculiaritems;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.attributes.Attributes;
import com.comphenix.attributes.Attributes.Attribute;
import com.comphenix.attributes.Attributes.AttributeType;
import com.comphenix.attributes.Attributes.Operation;
import com.google.common.base.Objects;
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
		
		Attribute nameAttribute = findAttribute(Constants.DISPLAYNAME_ID);
		if (nameAttribute != null) {
			displayName = nameAttribute.getName();
		}
		
		Attribute primaryAttribute = findAttribute(Constants.PRIMARYSTAT_ID);
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
			
			setAttribute(attributeUUID, attributeName);
		}
		
		if (displayName != null) {
			setAttribute(Constants.DISPLAYNAME_ID, displayName);
		}
		
		if (primaryStat != null) {
			setAttribute(Constants.PRIMARYSTAT_ID, primaryStat.getName());
		}
	}
	
	private Attribute findAttribute(UUID id) {
		for (Attribute attribute : storage.values()) {
			if (Objects.equal(attribute.getUUID(), id)) {
				return attribute;
			}
		}
		
		return null;
	}
	
	private void setAttribute(UUID id, String name) {
		Attribute statAttribute = findAttribute(id);
		if (statAttribute != null) {
			statAttribute.setName(name);
		} else {
			statAttribute = Attribute.newBuilder()
					.name(name)
					.amount(0)
					.operation(Operation.ADD_NUMBER)
					.uuid(Constants.DISPLAYNAME_ID)
					.type(AttributeType.GENERIC_ATTACK_DAMAGE)
					.build();
			
			storage.add(statAttribute);
		}
	}
}
