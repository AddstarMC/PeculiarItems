package au.com.mineauz.peculiaritems.peculiarstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import au.com.mineauz.peculiaritems.Main;

public class PeculiarStats {
	private Map<String, PeculiarStat> stats = new HashMap<String, PeculiarStat>();
	
	public PeculiarStats() {
		addStat(new BlocksBokenStat(), Main.getPlugin());
		addStat(new MonstersKilledStat(), Main.getPlugin());
		addStat(new TimesProtectedStat(), Main.getPlugin());
	}
	
	public void addStat(PeculiarStat stat, Plugin plugin){
		if(!stats.containsKey(stat.getName().toUpperCase().replace(" ", "_"))){
			stats.put(stat.getName().toUpperCase().replace(" ", "_"), stat);
			stat.registerEvents(plugin);
		}
	}
	
	public PeculiarStat getStat(String name){
		return stats.get(name);
	}
	
	public PeculiarStat getRandomStat(){
		List<PeculiarStat> s = new ArrayList<PeculiarStat>(stats.values());
		Collections.shuffle(s);
		return s.get(0);
	}
	
	public List<PeculiarStat> getAllStats(){
		return new ArrayList<PeculiarStat>(stats.values());
	}
	
	public List<String> getAllStatNames(){
		return new ArrayList<String>(stats.keySet());
	}
	
	public PeculiarStat matchStat(String lore){
		for(PeculiarStat stat : getAllStats()){
			if(ChatColor.stripColor(lore).matches("(" + stat.getDisplayName() + ":) [0-9]+"))
				return stat;
		}
		return null;
	}
	
	public void removeStat(String name){
		name = name.toUpperCase();
		if(stats.containsKey(name)){
			stats.get(name).unregisterEvents();
			stats.remove(name);
		}
	}
}
