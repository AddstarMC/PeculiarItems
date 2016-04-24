package au.com.mineauz.peculiaritems.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PeculiarItemsPlugin;
import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PeculiarModifier;

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
		return new String[] {"<Type> <Stat>", "<Type> random"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length == 1){
			return PCRUtils.tabCompleteMatch(Arrays.asList("iron", "gold", "diamond", "nametag"), args[0]);
		}
		else if(args.length == 2){
			List<String> list = PeculiarItemsPlugin.getPlugin().getStats().getAllStatNames();
			list.add("random");
			return PCRUtils.tabCompleteMatch(list, args[1]);
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args.length == 2){
			PeculiarItemsPlugin plugin = PeculiarItemsPlugin.getPlugin();
			Player ply = (Player)sender;
			Material type = null;
			
			if(args[0].equalsIgnoreCase("iron") || args[0].equalsIgnoreCase("gold")){
				type = Material.getMaterial(args[0].toUpperCase() + "_INGOT");
			}
			else if(args[0].equalsIgnoreCase("diamond")){
				type = Material.DIAMOND;
			}
			else if(args[0].equalsIgnoreCase("nametag")){
				type = Material.NAME_TAG;
			}
			
			PeculiarModifier pm = new PeculiarModifier(new ItemStack(type));
			
			if(!args[1].equalsIgnoreCase("random")){
				String arg = args[1].toUpperCase();
				if(plugin.getStats().getStat(arg) != null){
					pm.addStat(plugin.getStats().getStat(arg));
				}
				else{
					ply.sendMessage(ChatColor.RED + "No stat by the name " + arg);
					return true;
				}
			}
			else{
				pm.addStat(plugin.getStats().getRandomStat());
			}
			
			ply.getInventory().addItem(pm.getItem());
			return true;
		}
		return false;
	}

}
