package au.com.mineauz.peculiaritems;

import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

class PeculiarEnchantment extends Enchantment {
	private PeculiarEnchantment(int id) {
		super(id);
	}

	@Override
	public String getName() {
		return "Peculiar";
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}
	
	private static PeculiarEnchantment enchant;
	
	public static PeculiarEnchantment getEnchantment() {
		return enchant;
	}
	
	public static void registerEnchant(int id) {
		enchant = new PeculiarEnchantment(id);
		
		try {
			// Get the registration maps
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");
			byIdField.setAccessible(true);
			byNameField.setAccessible(true);
			
			Map<Integer,Enchantment> byId = (Map<Integer, Enchantment>)byIdField.get(null);
			Map<String,Enchantment> byName = (Map<String, Enchantment>)byNameField.get(null);
			
			// Register the enchant
			byId.put(id, enchant);
			byName.put(enchant.getName(), enchant);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
