package au.com.mineauz.peculiaritems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.exceptions.DuplicatePlayerException;
import au.com.mineauz.peculiaritems.exceptions.UnknownPlayerException;

public class Data {
	private Map<UUID, PCRPlayer> players = new HashMap<UUID, PCRPlayer>();
	private Map<Integer, String> ranks = new HashMap<Integer, String>();
	
	/**
	 * Adds a {@link PCRPlayer} for this plugin. This is automated by player join event.
	 * @param player The bukkit {@link Player}.
	 * @throws DuplicatePlayerException if this player has already been added.
	 */
	public void addPlayer(Player player) throws DuplicatePlayerException{
		if(!players.containsKey(player.getUniqueId()))
			players.put(player.getUniqueId(), new PCRPlayer(player));
		else
			throw new DuplicatePlayerException(player);
	}
	
	/**
	 * Removes a {@link PCRPlayer} from this plugin. This is automated by player quit event.
	 * @param player The bukkit {@link Player}.
	 * @throws UnknownPlayerException if the player has not been added.
	 */
	public void removePlayer(Player player) throws UnknownPlayerException{
		if(players.containsKey(player.getUniqueId()))
			players.remove(player.getUniqueId());
		else 
			throw new UnknownPlayerException(player);
	}
	
	/**
	 * Gets the {@link PCRPlayer} by a Bukkit {@link Player}.
	 * @param player The {@link Player} linked to {@link PCRPlayer}.
	 * @return The {@link PCRPlayer} or <code>null</code> if one doesn't exits.
	 */
	public PCRPlayer getPlayer(Player player){
		if(players.containsKey(player.getUniqueId()))
			return players.get(player.getUniqueId());
		return null;
	}
	
	/**
	 * Gets the {@link PCRPlayer} by their {@link UUID}.
	 * @param player The {@link UUID} of the player.
	 * @return The {@link PCRPlayer} or <code>null</code> if one hasn't been assigned to this ID.
	 */
	public PCRPlayer getPlayer(UUID player){
		if(players.containsKey(player))
			return players.get(player);
		return null;
	}
	
	/**
	 * Adds or sets a rank value.
	 * @param level The level the rank is set at.
	 * @param rank The rank name.
	 */
	public void setRank(int level, String rank){
		ranks.put(level, rank);
	}
	
	/**
	 * Gets the rank of a predetermined level.
	 * @param level The level to get the rank of.
	 * @return The rank name or <code>null</code> if one is not defined for this level.
	 */
	public String getRank(int level){
		return ranks.get(level);
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
