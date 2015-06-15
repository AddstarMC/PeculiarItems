package au.com.mineauz.peculiaritems.exceptions;

import org.bukkit.entity.Player;

public class UnknownPlayerException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownPlayerException(Player player){
		super("Unknown Player: " + player.getName());
	}
}
