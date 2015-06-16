package au.com.mineauz.peculiaritems;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.peculiaritems.commands.CommandDispatcher;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStats;

public class Main extends JavaPlugin{
	
	private List<Integer> rankValues = new ArrayList<Integer>();
	private boolean broadcastRank = true;
	private static Main plugin;
	private Data data;
	private PeculiarStats stats;
	
	@Override
	public void onEnable(){
		
		plugin = this;
		
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
		
		for(String rank : getConfig().getStringList("rankNumbers")){
			if(!rank.matches("[0-9]+")){
				rankValues.clear();
				for(String rank2 : getConfig().getDefaults().getStringList("rankNumbers")){
					rankValues.add(Integer.valueOf(rank2));
				}
				break;
			}
			else
				rankValues.add(Integer.valueOf(rank));
		}
		Collections.sort(rankValues);
		broadcastRank = getConfig().getBoolean("broadcastRankIncrease");

		CommandDispatcher comd = new CommandDispatcher();
		getCommand("peculiar").setExecutor(comd);
		getCommand("peculiar").setTabCompleter(comd);
		
		stats = new PeculiarStats();
		data = new Data();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getLogger().info("Peculiar Items successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		rankValues.clear();
		stats = null;
		data = null;
		getLogger().info("Peculiar Items successfully disabled!");
	}
	
	public List<Integer> getRankValues(){
		return new ArrayList<Integer>(rankValues);
	}
	
	public boolean isBroadcastingRankUp(){
		return broadcastRank;
	}
	
	public static Main getPlugin(){
		return plugin;
	}
	
	/**
	 * Gets all registered {@link PeculiarStat}s.
	 * @return {@link PeculiarStats}
	 */
	public PeculiarStats getStats(){
		return stats;
	}
	
	/**
	 * Gets the data used by this plugin.
	 * @return {@link Data}
	 */
	public Data getData(){
		return data;
	}

}
