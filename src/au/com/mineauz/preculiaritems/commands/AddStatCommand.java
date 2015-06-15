package au.com.mineauz.preculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;
import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PeculiarItem;
import au.com.mineauz.peculiaritems.PeculiarObject;

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
		if(args.length == 1){
			List<String> values = Main.getPlugin().getStats().getAllStatNames();
			values.add("random");
			return PCRUtils.tabCompleteMatch(values, args[0]);
		}
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args.length > 0){
			Main plugin = Main.getPlugin();
			PCRPlayer ply = plugin.getData().getPlayer((Player)sender);
			if(ply.getItemInHand() == null)
				ply.sendMessage(ChatColor.RED + "You must have an item in your hand!");
			if(!args[0].equalsIgnoreCase("random")){
				String arg = args[0].toUpperCase();
				if(plugin.getStats().getStat(arg) != null){
					PeculiarObject po = new PeculiarObject(ply.getItemInHand());
					if(!PCRUtils.isPeculiarItem(ply.getItemInHand()) && 
							!PCRUtils.isPeculiarModifier(ply.getItemInHand())){
						po = new PeculiarItem(ply.getItemInHand());
					}
					po.addStat(plugin.getStats().getStat(arg));
					
					if(PCRUtils.isPeculiarItem(ply.getItemInHand()))
						ply.setActiveItem((PeculiarItem)po);
				}
				else{
					ply.sendMessage(ChatColor.RED + "No stat by the name " + arg);
				}
			}
			else{
				PeculiarObject po = null;
				if(!PCRUtils.isPeculiarItem(ply.getItemInHand()) && 
						!PCRUtils.isPeculiarModifier(ply.getItemInHand())){
					po = new PeculiarItem(ply.getItemInHand());
				}
				po.addStat(plugin.getStats().getRandomStat());
			}
			return true;
		}
		return false;
	}

}
