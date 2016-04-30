package au.com.mineauz.peculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.addstar.monolith.lookup.Lookup;
import au.com.addstar.monolith.lookup.MaterialDefinition;
import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PeculiarItem;
import au.com.mineauz.peculiaritems.PeculiarModifier;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;

public class CreateModifierCommand implements ICommand {

	@Override
	public String getName() {
		return "createmodifier";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"crmod", "createmod", "cmod"};
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean canBeCommandBlock() {
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {"<Material> <Stat>", "<Material> random", "HAND <Stat>", "HAND random"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 2) {
			List<String> values = Lists.newArrayList();
			for (PeculiarStat stat : PeculiarItemsPlugin.getPlugin().getStats().getAllStats()) {
				values.add(stat.getName().toLowerCase());
			}
			values.add("random");
			return PCRUtils.tabCompleteMatch(values, args[1]);
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return false;
		}
		
		PeculiarItemsPlugin plugin = PeculiarItemsPlugin.getPlugin();
		Player player = (Player)sender;
		
		// Get the target item
		ItemStack target;
		boolean isHand = false;
		
		if (args[0].equals("HAND")) {
			target = player.getInventory().getItemInMainHand();
			isHand = true;
			
			if (target == null || target.getType() == Material.AIR) {
				player.sendMessage(ChatColor.RED + "You are not holding an item");
				return true;
			}
		} else {
			// Parse a material
			MaterialDefinition def = Lookup.findItemByName(args[0]);
			if (def == null) {
				Material type = Material.getMaterial(args[0]);
				if (type == null) {
					player.sendMessage(ChatColor.RED + "Unknown material " + args[0]);
					return true;
				}
				
				target = new ItemStack(type, 1);
			} else {
				target = def.asItemStack(1);
			}
		}
		
		// Make sure its not a peculiar item
		if (new PeculiarItem(target).isPeculiar()) {
			player.sendMessage(ChatColor.RED + "You cannot turn this item in a modifier");
			return true;
		}
		
		// Determine the stat to add
		PeculiarStat stat;
		
		if (args[1].equalsIgnoreCase("random")) {
			stat = plugin.getStats().getRandomStat(); 
		} else {
			stat = plugin.getStats().loadStat(args[1]);
			
			if (stat == null) {
				player.sendMessage(ChatColor.RED + "No stat by the name " + args[1]);
				return true;
			}
		}
		
		PeculiarModifier modifier = new PeculiarModifier(target);
		modifier.addStat(stat);
		
		if (isHand) {
			player.sendMessage(ChatColor.GREEN + "This item is now a peculiar modifier");
			player.getInventory().setItemInMainHand(modifier.getItemStack());
		} else {
			player.sendMessage(ChatColor.GREEN + "Given you a peculiar modifier");
			player.getInventory().addItem(modifier.getItemStack());
		}
		
		player.updateInventory();
		
		return true;
	}

}
