package au.com.mineauz.peculiaritems;

import java.util.HashMap;
import java.util.Map;

public class Data {
	private Map<Integer, String> ranks = new HashMap<Integer, String>();
	
	/**
	 * Adds or sets a rank value.
	 * @param level The level the rank is set at.
	 * @param rank The rank name.
	 */
	public void setRank(int level, String rank){
		ranks.put(level, rank);
	}
	
	/**
	 * Gets the highest rank at a predetermined level.
	 * @param level The level to get the rank of.
	 * @return The rank name or <code>null</code> if one is not defined.
	 */
	public String getRank(int level){
		// TODO: Improve this
		for (int i = level; i >= 0; --i) {
			String rank = ranks.get(i);
			if (rank != null) {
				return rank;
			}
		}
		
		return null;
	}
	
	/**
	 * Clear all preset ranks. No leveling up will occour.
	 */
	public void clearRanks(){
		ranks.clear();
	}
	
	/**
	 * Checks if a preset rank has been assigned.
	 * @param level The level the rank is set at.
	 * @return <code>true</code> if a rank is defined at the specific level.
	 */
	public boolean hasRank(int level){
		return ranks.containsKey(level);
	}
}
