package de.polarwolf.doorcloser.api;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.plugin.PluginBase;
import de.polarwolf.doorcloser.butler.ButlerManager;
import de.polarwolf.doorcloser.config.ConfigManager;
import de.polarwolf.doorcloser.exception.DoorCloserException;
import de.polarwolf.doorcloser.materials.Material;

public class DoorCloserAPI {
	
	protected final PluginBase plugin;
	protected final ConfigManager configManager;
	protected final ButlerManager butlerManager;
	
	
	public DoorCloserAPI(PluginBase plugin, ConfigManager configManager, ButlerManager butlerManager) {
		this.plugin = plugin;
		this.configManager = configManager;
		this.butlerManager = butlerManager;
	}
	
	
	public int getTaskCount() {
		return butlerManager.getTaskCount();
	}
	
	
	public boolean isDebug() {
		return configManager.isDebug();
	}
	
	
	public void setDebug(boolean newDebug) {
		configManager.setDebug(newDebug);
	}
	
	
	public Set<Material> getOpenablesInScope() {
		return new HashSet<>(configManager.getConfigData().getOpenablesInScope());
	}
	
	
	public void reload() throws DoorCloserException {
		plugin.reloadConfig();
		configManager.reload();
	}

}
