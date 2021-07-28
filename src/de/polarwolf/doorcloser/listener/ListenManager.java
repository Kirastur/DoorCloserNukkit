package de.polarwolf.doorcloser.listener;

import cn.nukkit.plugin.Plugin;
import de.polarwolf.doorcloser.butler.ButlerManager;
import de.polarwolf.doorcloser.config.ConfigManager;

public class ListenManager {

		
	protected final Plugin plugin;
	protected final ConfigManager configManager;
	protected final ButlerManager butlerManager;
	protected final PlayerInteractionListener playerInteractionListener;
		

	public ListenManager(Plugin plugin, ConfigManager configManager, ButlerManager butlerManager) {
		this.plugin = plugin;
		this.configManager = configManager;
		this.butlerManager = butlerManager;
		playerInteractionListener = new PlayerInteractionListener(configManager, butlerManager); 
	}
		
		
	public void registerListener() {
		plugin.getServer().getPluginManager().registerEvents(playerInteractionListener, plugin);
	}
		

}
