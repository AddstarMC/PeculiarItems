package au.com.mineauz.peculiaritems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.exceptions.DuplicatePlayerException;
import au.com.mineauz.peculiaritems.exceptions.UnknownPlayerException;

public class Data {
	private Map<UUID, PCRPlayer> players = new HashMap<UUID, PCRPlayer>();
	
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
}
