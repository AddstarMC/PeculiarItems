package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PCRUtils.ArmorType;

public class DamageTakenStat extends PeculiarStat {
	@Override
	public String getName() {
		return "DAMAGE_TAKEN";
	}

	@Override
	public ChatColor getDisplayColor() {
		return ChatColor.DARK_PURPLE;
	}

	@Override
	public String getDisplayName() {
		return "Damage Taken";
	}

	@Override
	public boolean isCompatibleItem(ItemStack item) {
		ArmorType type = PCRUtils.getArmorType(item);
		
		return (type == ArmorType.Helmet || type == ArmorType.Chestplate || type == ArmorType.Leggings || type == ArmorType.Boots);
	}
}
