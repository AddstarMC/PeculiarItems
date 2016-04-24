package au.com.mineauz.peculiaritems;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.peculiaritems.commands.CommandDispatcher;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStats;
import au.com.mineauz.peculiaritems.peculiarstats.StatListener;

public class PeculiarItemsPlugin extends JavaPlugin{
	
	private boolean broadcastRank = true;
	private static PeculiarItemsPlugin plugin;
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
		
		data = new Data();
		
		for(String rank : getConfig().getConfigurationSection("ranks").getKeys(false)){
			if(!rank.matches("[0-9]+")){
				data.clearRanks();
				for(String rank2 : getConfig().getConfigurationSection("ranks").getKeys(false)){
					data.setRank(Integer.parseInt(rank2), getConfig().getString("ranks." + rank2));
				}
				break;
			}
			else
				data.setRank(Integer.parseInt(rank), getConfig().getString("ranks." + rank));
		}
		broadcastRank = getConfig().getBoolean("broadcastRankIncrease");

		CommandDispatcher comd = new CommandDispatcher();
		getCommand("peculiar").setExecutor(comd);
		getCommand("peculiar").setTabCompleter(comd);
		
		stats = new PeculiarStats();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new StatListener(), this);
		getLogger().info("Peculiar Items successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		stats = null;
		data = null;
		getLogger().info("Peculiar Items successfully disabled!");
	}
	
	public boolean isBroadcastingRankUp(){
		return broadcastRank;
	}
	
	public static PeculiarItemsPlugin getPlugin(){
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
