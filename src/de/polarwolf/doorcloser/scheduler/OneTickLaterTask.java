// This work contains code from the original DoorCloser-Plugin written by Psychlist1972
// He has released his work using the Apache 2.0 license
// Please see https://github.com/Psychlist1972/Minecraft-DoorCloser for reference

package de.polarwolf.doorcloser.scheduler;


import cn.nukkit.scheduler.NukkitRunnable;
import de.polarwolf.doorcloser.butler.ButlerManager;
import de.polarwolf.doorcloser.config.ConfigManager;
import de.polarwolf.doorcloser.materials.Material;
import de.polarwolf.doorcloser.openables.Openable;

public class OneTickLaterTask extends NukkitRunnable {

	protected final ConfigManager configManager;
	protected final ButlerManager butlerManager;
	protected final Openable clickedOpenable;
	protected final Openable pairedOpenable;
	protected final boolean oldOpenState;
	

	public OneTickLaterTask(ConfigManager configManager, ButlerManager butlerManager, Openable clickedOpenable, Openable pairedOpenable, boolean oldOpenState) {
		this.configManager = configManager;
		this.butlerManager = butlerManager;
		this.clickedOpenable = clickedOpenable;
		this.pairedOpenable = pairedOpenable;
		this.oldOpenState = oldOpenState;
	}
	
	
	public void handleInteraction(boolean isCancel) {
		
		// First check if block has changed just before
		if (clickedOpenable.hasIdChanged()) {
			butlerManager.printDebug("DEBUG: Material has changed - ignoring.");			
			return;
		}

		Material clickedMaterial = clickedOpenable.getMaterial();
		
		// Please remember: synchronizing doors is only active if doorCloser is active,
		// but does not check about the material
		boolean newOpenState = clickedOpenable.isOpen();
		butlerManager.synchronizePairedDoor(pairedOpenable, newOpenState);
		
		// Check if state has changed since last tick, which means the open/close was accepted by the server
		if (oldOpenState == newOpenState) {
			butlerManager.printDebug("DEBUG: Open status was not changed - ignoring.");
			return;			
		}
		
		// Check if door was closed by the player
		if (!newOpenState) {
			butlerManager.printDebug("DEBUG: " + clickedMaterial.getName() + " was closed - ignoring.");
			return;
		}

		// check to see if it's a type of block we want to close. 
		if (!configManager.getConfigData().getOpenablesInScope().contains(clickedMaterial)) {
			butlerManager.printDebug("DEBUG: Material is not in scope: " + clickedMaterial.getName());
			return;			
		}
		
		// All prerequisites are fulfilled, so lets trigger the lateron close
		// We expect the paired block is null if type is not door (e.g. gate or trapdoor)
		if (isCancel) {
			clickedOpenable.close();
			butlerManager.synchronizePairedDoor(pairedOpenable, false);
			butlerManager.printDebug("DEBUG: Closer for " + clickedMaterial.getName() + " cancelled.");
		} else {
			butlerManager.scheduleCloseTask(clickedOpenable, pairedOpenable, configManager.getConfigData().getSecondsToRemainOpen());
			butlerManager.printDebug("DEBUG: Closer for " + clickedMaterial.getName() + " scheduled.");
		}
	}


	@Override
	public void run() {
		try {
			butlerManager.printDebug("DEBUG: one-tick-later triggered.");
			butlerManager.removeOneTickLaterTask(this);
			handleInteraction(false);
		} catch (Exception e) {
			butlerManager.printException(e);
		}
	}

}
