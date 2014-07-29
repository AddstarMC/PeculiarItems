package au.com.mineauz.peculiaritems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.preculiaritems.commands.CommandDispatcher;

public class Main extends JavaPlugin{
	
	private List<Integer> rankValues = new ArrayList<Integer>();
	
	@Override
	public void onEnable(){
		
		File conf = new File(getDataFolder() + "/" + "config.yml");
		if(!conf.exists()){
			saveResource("config.yml", false);
		}
		try{
			getConfig().load(getDataFolder() + "/config.yml");
		}catch(Exception e){
			getLogger().severe("Unable to load config.yml!");
			e.printStackTrace();
		}
		
		for(String rank : getConfig().getStringList("rankValues")){
			if(!rank.matches("[0-9]+")){
				rankValues.clear();
				for(String rank2 : getConfig().getDefaults().getStringList("rankValues")){
					rankValues.add(Integer.valueOf(rank2));
				}
				break;
			}
			else
				rankValues.add(Integer.valueOf(rank));
		}

		CommandDispatcher comd = new CommandDispatcher();
		getCommand("peculiar").setExecutor(comd);
		getCommand("peculiar").setTabCompleter(comd);
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getLogger().info("Peculiar Items successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		rankValues.clear();
		getLogger().info("Peculiar Items successfully disabled!");
	}

}
