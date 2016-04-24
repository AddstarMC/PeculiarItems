package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.mineauz.peculiaritems.PCRUtils.ArmorType;
import au.com.mineauz.peculiaritems.PCRPlayer;
import au.com.mineauz.peculiaritems.PCRUtils;

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
	public boolean isCompatibleItem(ItemStack item) {
		ArmorType type = PCRUtils.getArmorType(item);
		
		return (type == ArmorType.Helmet || type == ArmorType.Chestplate || type == ArmorType.Leggings || type == ArmorType.Boots);
	}
	
	@EventHandler(ignoreCancelled = true)
	private void playerHurt(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Player){
			PCRPlayer ply = PeculiarItemsPlugin.getPlugin().getData().getPlayer((Player)event.getEntity());
			if(ply == null) return;
			
			ply.incrementActiveArmorStat(getName(), 1, 
					PCRUtils.capitalize(event.getDamager().getType().toString().replace("_", " ")));
		}
	}

}
