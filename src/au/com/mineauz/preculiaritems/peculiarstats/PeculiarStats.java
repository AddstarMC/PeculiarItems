package au.com.mineauz.preculiaritems.peculiarstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class PeculiarStats {
	private Map<String, PeculiarStat> stats = new HashMap<String, PeculiarStat>();
	
	public PeculiarStats() {
		addStat(new BlocksBokenStat());
		addStat(new MonstersKilledStat());
		addStat(new TimesProtectedStat());
	}
	
	public void addStat(PeculiarStat stat){
		if(!stats.containsKey(stat.getName().toUpperCase().replace(" ", "_")))
			stats.put(stat.getName().toUpperCase().replace(" ", "_"), stat);
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
}
