package au.com.mineauz.preculiaritems.peculiarstats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeculiarStats {
	private static Map<String, PeculiarStat> stats = new HashMap<String, PeculiarStat>();
	
	static{
		addStat(new BlocksBokenStat());
		addStat(new MonstersKilledStat());
		addStat(new TimesProtectedStat());
	}
	
	public static void addStat(PeculiarStat stat){
		if(!stats.containsKey(stat.getName().toUpperCase().replace(" ", "_")))
			stats.put(stat.getName().toUpperCase().replace(" ", "_"), stat);
	}
	
	public static PeculiarStat getStat(String name){
		return stats.get(name);
	}
	
	public static PeculiarStat getRandomStat(){
		List<PeculiarStat> s = new ArrayList<PeculiarStat>(stats.values());
		Collections.shuffle(s);
		return s.get(0);
	}
	
	public static List<PeculiarStat> getAllStats(){
		return new ArrayList<PeculiarStat>(stats.values());
	}
}
