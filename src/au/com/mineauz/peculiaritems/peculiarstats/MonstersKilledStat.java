package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;

public class MonstersKilledStat extends PeculiarStat implements Listener{

	@Override
	public String getName() {
		return "MONSTERS_KILLED";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.RED;
	}

	@Override
	public String getDisplayName() {
		return "Monsters Killed";
	}

	@Override
	public boolean isCompatibleItem(String type) {
		if(type.equals("SWORD") || type.equals("AXE"))
			return true;
		return false;
	}
	
	@EventHandler(ignoreCancelled = true)
	private void entityKill(EntityDeathEvent event){
		if(event.getEntity() instanceof Monster){
			LivingEntity ent = event.getEntity();
			if(ent.getKiller() != null){
				PCRPlayer ply = Main.getPlugin().getData().getPlayer(ent.getKiller());
				if(ply == null) return;
				
				ply.incrementActiveItemStat(getName(), 1);
			}
		}
	}
}
