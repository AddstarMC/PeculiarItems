package au.com.mineauz.preculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.preculiaritems.peculiarstats.PeculiarStats;

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
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null && args.length == 2){
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
			ItemStack mod = new ItemStack(type);
			if(!args[1].equalsIgnoreCase("random")){
				String arg = args[0].toUpperCase();
				if(PeculiarStats.getStat(arg) != null){
					PCRUtils.setPeculiarModifier(mod);
					PeculiarStats.getStat(arg).addStat(mod, 0);
				}
				else{
					ply.sendMessage(ChatColor.RED + "No stat by the name " + arg);
					return true;
				}
			}
			else{
				PCRUtils.setPeculiarModifier(mod);
				PeculiarStats.getRandomStat().addStat(mod, 0);
			}
			
			ply.getInventory().addItem(mod);
			return true;
		}
		return false;
	}

}
