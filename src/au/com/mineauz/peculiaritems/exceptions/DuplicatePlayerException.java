package au.com.mineauz.peculiaritems.exceptions;

import org.bukkit.entity.Player;

public class DuplicatePlayerException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DuplicatePlayerException(Player player){
		super("Duplicate Player: " + player.getName());
	}
}
