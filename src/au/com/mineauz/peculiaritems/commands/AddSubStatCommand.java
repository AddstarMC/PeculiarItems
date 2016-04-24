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
import au.com.mineauz.peculiaritems.peculiarstats.SubStatable;

public class AddSubStatCommand implements ICommand {
	@Override
	public String getName() {
		return "addsubstat";
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
		return new String[] {"<Stat> <Special>"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length == 1) {
			List<String> values = Lists.newArrayList();
			for (PeculiarStat stat : PeculiarItemsPlugin.getPlugin().getStats().getAllStats()) {
				values.add(stat.getName().toLowerCase());
			}
			return PCRUtils.tabCompleteMatch(values, args[0]);
		}
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		PeculiarItemsPlugin plugin = PeculiarItemsPlugin.getPlugin();
		
		if (args.length < 2) {
			return false;
		}
		
		Player player = (Player)sender;
		ItemStack held = player.getInventory().getItemInMainHand();
		
		if (held == null) {
			player.sendMessage(ChatColor.RED + "You must have an item in your hand!");
			return true;
		}
		
		// Determine the stat to add to
		PeculiarStat stat = plugin.getStats().loadStat(args[0]);
		
		if (stat == null || stat instanceof PeculiarSubStat) {
			player.sendMessage(ChatColor.RED + "No stat by the name " + args[0]);
			return true;
		}
		
		// Get the item
		PeculiarItem item = new PeculiarItem(held);
		
		if (!item.hasStat(stat)) {
			player.sendMessage(ChatColor.RED + "The stat " + args[0] + " is not present on this item");
			return true;
		}
		
		// Get the sub stat
		if (!(stat instanceof SubStatable<?>)) {
			player.sendMessage(ChatColor.RED + "The stat " + args[0] + " does not have sub stats");
			return true;
		}
		
		PeculiarSubStat substat = ((SubStatable<?>)stat).ofEncoded(args[1]);
		if (substat == null) {
			player.sendMessage(ChatColor.RED + "Unknown sub stat " + args[1] + " for stat " + args[0]);
			return true;
		}
		
		// Add the stat
		item.addStat(substat);
		
		player.getInventory().setItemInMainHand(item.getItemStack());
		player.sendMessage(ChatColor.GREEN + "Successfully added the sub stat");
		
		return true;
	}

}
