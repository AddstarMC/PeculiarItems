package au.com.mineauz.preculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.preculiaritems.peculiarstats.PeculiarStats;

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
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null){
			Player ply = (Player)sender;
			if(ply.getItemInHand() == null)
				ply.sendMessage(ChatColor.RED + "You must have an item in your hand!");
			if(!args[0].equalsIgnoreCase("random")){
				String arg = args[0].toUpperCase();
				if(PeculiarStats.getStat(arg) != null){
					PCRUtils.setPeculiarItem(ply.getItemInHand());
					PeculiarStats.getStat(arg).incrementStat(ply, ply.getItemInHand(), 0);
				}
				else{
					ply.sendMessage(ChatColor.RED + "No stat by the name " + arg);
				}
			}
			else{
				PCRUtils.setPeculiarItem(ply.getItemInHand());
				PeculiarStats.getRandomStat().incrementStat(ply, ply.getItemInHand(), 0);
			}
			return true;
		}
		return false;
	}

}
