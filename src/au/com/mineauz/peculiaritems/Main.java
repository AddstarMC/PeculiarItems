package au.com.mineauz.peculiaritems;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.preculiaritems.commands.CommandDispatcher;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable(){

		CommandDispatcher comd = new CommandDispatcher();
		getCommand("peculiar").setExecutor(comd);
		getCommand("peculiar").setTabCompleter(comd);
		
		getServer().getPluginManager().registerEvents(new Events(), this);
	}
	
	@Override
	public void onDisable(){
		
	}

}
