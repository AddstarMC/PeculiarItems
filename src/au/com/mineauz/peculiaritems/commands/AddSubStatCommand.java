package au.com.mineauz.peculiaritems.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.Main;
import au.com.mineauz.peculiaritems.PCRPlayer;
import au.com.mineauz.peculiaritems.PCRUtils;
import au.com.mineauz.peculiaritems.PeculiarItem;
import au.com.mineauz.peculiaritems.PeculiarObject;

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
		if(args.length == 1){
			List<String> values = Main.getPlugin().getStats().getAllStatNames();
			return PCRUtils.tabCompleteMatch(values, args[0]);
		}
		
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args.length > 1){
			Main plugin = Main.getPlugin();
			PCRPlayer ply = plugin.getData().getPlayer((Player)sender);
			if(ply.getItemInHand() == null)
				ply.sendMessage(ChatColor.RED + "You must have an item in your hand!");
			String stat = args[0].toUpperCase();
			String spec = PCRUtils.capitalize(args[1].replace("_", " "));
			if(plugin.getStats().getStat(stat) != null){
				PeculiarObject po = new PeculiarObject(ply.getItemInHand());
				if(!PCRUtils.isPeculiarItem(ply.getItemInHand()) && 
						!PCRUtils.isPeculiarModifier(ply.getItemInHand())){
					po = new PeculiarItem(ply.getItemInHand());
				}
				if(!po.hasStat(stat))
					po.addStat(plugin.getStats().getStat(stat));
				po.addSubStat(plugin.getStats().getStat(stat), spec);
				
				if(PCRUtils.isPeculiarItem(ply.getItemInHand()))
					ply.setActiveItem(new PeculiarItem(ply.getItemInHand()));
			}
			else{
				ply.sendMessage(ChatColor.RED + "No stat by the name " + stat);
			}
			return true;
		}
		return false;
	}

}
