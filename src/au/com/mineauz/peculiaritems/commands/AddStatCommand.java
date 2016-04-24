package au.com.mineauz.peculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PeculiarItem;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarSubStat;

public class AddStatCommand implements ICommand {

	@Override
	public String getName() {
		return "addstat";
	}

	@Override
	public String[] getAliases() {
		return null;
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
		return new String[] {"<statname>", "random"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 1) {
			List<String> values = Lists.newArrayList();
			for (PeculiarStat stat : PeculiarItemsPlugin.getPlugin().getStats().getAllStats()) {
				values.add(stat.getName().toLowerCase());
			}
			values.add("random");
			return PCRUtils.tabCompleteMatch(values, args[0]);
		}
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		PeculiarItemsPlugin plugin = PeculiarItemsPlugin.getPlugin();
		
		if (args.length == 0) {
			return false;
		}
		
		Player player = (Player)sender;
		ItemStack held = player.getInventory().getItemInMainHand();
		
		if (held == null) {
			player.sendMessage(ChatColor.RED + "You must have an item in your hand!");
			return true;
		}
		
		// Determine the stat to add
		PeculiarStat stat;
		
		if (args[0].equalsIgnoreCase("random")) {
			stat = plugin.getStats().getRandomStat(); 
		} else {
			stat = plugin.getStats().loadStat(args[0]);
			
			if (stat == null) {
				player.sendMessage(ChatColor.RED + "No stat by the name " + args[0]);
				return true;
			}
		}
		
		// Add the stat
		PeculiarItem item = new PeculiarItem(held);
		
		if (stat instanceof PeculiarSubStat) {
			item.addStat(((PeculiarSubStat)stat).getParent());
			item.addStat(stat);
			
			player.sendMessage(ChatColor.GOLD + "Added the parent stat in addition to the specified stat");
		} else {
			item.addStat(stat);
		}
		
		player.getInventory().setItemInMainHand(item.getItemStack());
		player.sendMessage(ChatColor.GREEN + "Successfully added the stat");
		
		return true;
	}
}
