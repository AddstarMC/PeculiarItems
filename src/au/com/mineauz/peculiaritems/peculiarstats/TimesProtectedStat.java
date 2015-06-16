package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;

public class TimesProtectedStat extends PeculiarStat implements Listener{

	@Override
	public String getName() {
		return "TIMES_PROTECTED";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.GRAY;
	}

	@Override
	public String getDisplayName() {
		return "Times Protected";
	}

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("LEGGINGS") || type.equals("HELMET") || type.equals("CHESTPLATE") || type.equals("BOOTS"))
			return true;
		return false;
	}
	
	@EventHandler(ignoreCancelled = true)
	private void playerHurt(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			PCRPlayer ply = Main.getPlugin().getData().getPlayer((Player)event.getEntity());
			if(ply == null) return;
			
			ply.incrementActiveArmorStat(getName(), 1);
		}
	}

}
