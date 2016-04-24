package au.com.mineauz.peculiaritems.peculiarstats;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PCRUtils.ArmorType;
import au.com.mineauz.peculiaritems.PCRUtils;

public class TimesProtectedStat extends PeculiarStat {

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
}
