// This work contains code from the original DoorCloser-Plugin written by Psychlist1972
// He has released his work using the Apache 2.0 license
// Please see https://github.com/Psychlist1972/Minecraft-DoorCloser for reference

package de.polarwolf.doorcloser.scheduler;

import cn.nukkit.scheduler.NukkitRunnable;
import de.polarwolf.doorcloser.butler.ButlerManager;
import de.polarwolf.doorcloser.config.ConfigManager;
import de.polarwolf.doorcloser.openables.Openable;

public class DoorCloseTask extends NukkitRunnable {
	
	protected final ConfigManager configManager;
	protected final ButlerManager butlerManager;
	protected final Openable clickedOpenable;
	protected final Openable pairedOpenable;
	

	public DoorCloseTask(ConfigManager configManager, ButlerManager butlerManager, Openable clickedOpenable, Openable pairedOpenable) {
		this.configManager = configManager;
		this.butlerManager = butlerManager;
		this.clickedOpenable = clickedOpenable;
		this.pairedOpenable = pairedOpenable;	
	}
	

	public void handleClose(boolean isCancel) {
		
		// First check if block has changed just before
		if (clickedOpenable.hasIdChanged()) {
			butlerManager.printDebug("DEBUG: Material has changed - ignoring.");			
			return;
		}
				
		boolean changedClickedOpen = false;
		if (clickedOpenable.isOpen()) {
			changedClickedOpen = clickedOpenable.close();
			if (changedClickedOpen) {
				butlerManager.printDebug("DEBUG: The butler has closed the openable.");
			} else {
				butlerManager.printDebug("DEBUG: State was not changed. Something must be happened outside this plugin");
			}
		} else {
			butlerManager.printDebug("DEBUG: Someone was faster than the butler. The openable is already closed.");			
		}
			
		boolean changedPairedOpen = butlerManager.synchronizePairedDoor(pairedOpenable, false);
		if ((changedClickedOpen || changedPairedOpen) && configManager.getConfigData().isPlaySound() && !isCancel) {
			clickedOpenable.playCloseSound();
		}
	}
	

	@Override
	public void run() {
		try {
			butlerManager.printDebug("DEBUG: Schedule started.");
			butlerManager.removeDoorCloseTask(this);
			handleClose(false);
			butlerManager.printDebug("DEBUG: Schedule finished.");
		} catch (Exception e) {
			butlerManager.printException(e);
		}
	}	

}
