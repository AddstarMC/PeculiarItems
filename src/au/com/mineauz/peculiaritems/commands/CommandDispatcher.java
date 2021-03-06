package au.com.mineauz.peculiaritems.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import au.com.mineauz.peculiaritems.PCRUtils;

public class CommandDispatcher implements CommandExecutor, TabCompleter{
	private static Map<String, ICommand> commands = new HashMap<String, ICommand>();
	
	static{
		addCommand(new AddStatCommand());
		addCommand(new CreateModifierCommand());
		addCommand(new AddSubStatCommand());
	}
	
	public static void addCommand(ICommand command){
		commands.put(command.getName().toLowerCase(), command);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String cmd, String[] args) {
		if(args.length == 1){
			return PCRUtils.tabCompleteMatch(new ArrayList<String>(commands.keySet()), args[0]);
		}
		else if(args.length > 1){
			String subcomd = args[0].toLowerCase();
			ICommand icmd = getCommand(subcomd);
			
			if(icmd != null){
				String[] nargs = null;
				if(args.length - 1 > 0){
					nargs = new String[args.length - 1];
					for(int i = 1; i < args.length; i++)
						nargs[i - 1] = args[i];
				}
				else nargs = new String[0];
				
				return icmd.onTabComplete(sender, nargs);
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd,
			String[] args) {
		boolean isConsole = false;
		boolean isCommandBlock = false;
		if(sender instanceof BlockCommandSender)
			isCommandBlock = true;
		else if(!(sender instanceof Player))
			isConsole = true;
		
		if(args.length > 0){
			String subcomd = args[0].toLowerCase();
			ICommand icmd = getCommand(subcomd);
			
			if(icmd != null){
				String[] nargs = null;
				if(args.length - 1 > 0){
					nargs = new String[args.length - 1];
					for(int i = 1; i < args.length; i++)
						nargs[i - 1] = args[i];
				}
				else nargs = new String[0];
				
				if((isConsole && !icmd.canBeConsole()) ||
						(isCommandBlock && !icmd.canBeCommandBlock())){
					sender.sendMessage(ChatColor.RED + "Sorry, this command can't be used under your conditions!");
					return true;
				}
				
				boolean ret = icmd.onCommand(sender, nargs);
				if(!ret){
					sender.sendMessage(ChatColor.GREEN + "------------Command Help------------");
					sender.sendMessage(ChatColor.AQUA + "Command: " + ChatColor.GRAY + icmd.getName());
					if(icmd.getAliases() != null){
						sender.sendMessage(ChatColor.AQUA + "Aliases: " + PCRUtils.arrayToString(icmd.getAliases()));
					}
					if(icmd.getUsage() != null){
						sender.sendMessage(ChatColor.AQUA + "Usage:");
						for(String use : icmd.getUsage()){
							sender.sendMessage(ChatColor.GRAY + "/" + cmd + " " + icmd.getName() + " " + use);
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	private ICommand getCommand(String name){
		ICommand icmd = null;
		if(commands.containsKey(name))
			icmd = commands.get(name);
		else{
loop:			for(ICommand ic : commands.values()){
				if(ic.getAliases() != null){
					for(String al : ic.getAliases()){
						if(al.equalsIgnoreCase(name)){
							icmd = ic;
							break loop;
						}
					}
				}
			}
		}
		
		return icmd;
	}

}
