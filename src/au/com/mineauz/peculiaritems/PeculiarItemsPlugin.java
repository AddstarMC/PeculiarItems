package au.com.mineauz.peculiaritems;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.peculiaritems.commands.CommandDispatcher;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStat;
import au.com.mineauz.peculiaritems.peculiarstats.PeculiarStats;
import au.com.mineauz.peculiaritems.peculiarstats.StatListener;

public class PeculiarItemsPlugin extends JavaPlugin {
	private boolean broadcastRank = true;
	private static PeculiarItemsPlugin plugin;
	private Data data;
	private PeculiarStats stats;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		saveDefaultConfig();
		reloadConfig();
		
		data = new Data();
		
		loadRanks();
		broadcastRank = getConfig().getBoolean("broadcastRankIncrease");

		CommandDispatcher comd = new CommandDispatcher();
		getCommand("peculiar").setExecutor(comd);
		getCommand("peculiar").setTabCompleter(comd);
		
		stats = new PeculiarStats();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new StatListener(), this);
		getLogger().info("Peculiar Items successfully enabled!");
	}
	
	private void loadRanks() {
		ConfigurationSection rankSection = getConfig().getConfigurationSection("ranks");
		if (rankSection == null) {
			return;
		}
		
		data.clearRanks();
		for (String rank : rankSection.getKeys(false)) {
			try {
				int value = Integer.parseInt(rank);
				data.setRank(value, rankSection.getString(rank));
			} catch (NumberFormatException e) {
				getLogger().warning("Invalid rank definition: " + rank);
			}
		}
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
