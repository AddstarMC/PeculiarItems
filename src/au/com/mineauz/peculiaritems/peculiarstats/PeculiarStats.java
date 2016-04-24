package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;

public class PeculiarStats {
	public static final BlocksBrokenStat BLOCKS_BROKEN = new BlocksBrokenStat();
	public static final MonstersKilledStat MOBS_KILLED = new MonstersKilledStat();
	
	private final Map<String, PeculiarStat> stats;
	private final SetMultimap<Plugin, PeculiarStat> pluginStats;
	
	public PeculiarStats() {
		stats = Maps.newHashMap();
		pluginStats = HashMultimap.create();
		
		addStat(BLOCKS_BROKEN, PeculiarItemsPlugin.getPlugin());
		addStat(MOBS_KILLED, PeculiarItemsPlugin.getPlugin());
		addStat(new TimesProtectedStat(), PeculiarItemsPlugin.getPlugin());
	}
	
	public void addStat(PeculiarStat stat, Plugin plugin) {
		Preconditions.checkArgument(!(stat instanceof PeculiarSubStat));
		
		if (stats.containsValue(stat)) {
			return;
		}
		
		stats.put(stat.getName().toUpperCase(), stat);
		pluginStats.put(plugin, stat);
	}
	
	public PeculiarStat getRandomStat() {
		return Iterables.get(stats.values(), RandomUtils.nextInt(stats.size()), null);
	}
	
	public Collection<PeculiarStat> getAllStats() {
		return Collections.unmodifiableCollection(stats.values());
	}
	
	public PeculiarStat loadStat(String name) {
		String[] parts = name.split("\\[");
		
		PeculiarStat parentStat = stats.get(parts[0].toUpperCase());
		if (parentStat == null) {
			return null;
		}
		
		if (parts.length == 1) {
			return parentStat;
		} else {
			if (parentStat instanceof SubStatable<?>) {
				return ((SubStatable<?>)parentStat).ofEncoded(parts[1]);
			} else {
				return null;
			}
		}
	}
}
