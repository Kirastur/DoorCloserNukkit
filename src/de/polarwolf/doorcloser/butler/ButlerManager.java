// This work contains code from the original DoorCloser-Plugin written by Psychlist1972
// He has released his work using the Apache 2.0 license
// Please see https://github.com/Psychlist1972/Minecraft-DoorCloser for reference

package de.polarwolf.doorcloser.butler;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.block.Block;
import cn.nukkit.plugin.Plugin;
import de.polarwolf.doorcloser.config.ConfigManager;
import de.polarwolf.doorcloser.openables.Openable;
import de.polarwolf.doorcloser.openables.OpenableSimple;
import de.polarwolf.doorcloser.scheduler.DoorCloseTask;
import de.polarwolf.doorcloser.scheduler.OneTickLaterTask;

public class ButlerManager {
	
	public static final int TICKS_PER_SECOND = 20;
	
	protected final Plugin plugin;
	protected final ConfigManager configManager;
	
	protected List<OneTickLaterTask> oneTickLaterTasks = new ArrayList<>();
	protected List<DoorCloseTask> doorCloseTasks = new ArrayList<>();
	
	
	public ButlerManager(Plugin plugin, ConfigManager configManager) {
		this.plugin = plugin;
		this.configManager = configManager;
	}
	

	public void printDebug(String messageText) {
		if (configManager.isDebug()) {
			plugin.getLogger().info(messageText);
		}
	}
	

	public void printWarning(String messageText) {
		plugin.getLogger().warning(messageText);
	}
	
	
	public void printException(Exception e) {
		plugin.getServer().getLogger().logException(e);
	}
	
	
	public Openable findPairedOpenable(Openable clickedOpenable) {
		Block clickedBlock = clickedOpenable.getBlock();
		Block pairedBlock = null;
		switch (clickedOpenable.getBlockFace())
		{
			case NORTH:
				if (clickedOpenable.isRightHinged()) {
					pairedBlock = clickedBlock.south();	
				} else {
					pairedBlock = clickedBlock.north();	
				}
				break;

			case SOUTH:
				if (clickedOpenable.isRightHinged()) {
					pairedBlock = clickedBlock.north();	
				} else {
					pairedBlock = clickedBlock.south();	
				}
				break;

			case EAST:
				if (clickedOpenable.isRightHinged()) {
					pairedBlock = clickedBlock.west();
				} else {
					pairedBlock = clickedBlock.east();	
				}
				break;
				
			case WEST:
				if (clickedOpenable.isRightHinged()) {
					pairedBlock = clickedBlock.east();	
				} else {
					pairedBlock = clickedBlock.west();	
				}
				break;

			default:
				pairedBlock = null;
				break;
		}
		
		if (pairedBlock == null) {
			printDebug ("DEBUG: Paired block not found");			
		} else {
			printDebug ("DEBUG: Paired material is " + pairedBlock.getName());
		}
		return OpenableSimple.fromBlock(pairedBlock);
	}

	
	// Synchronize both wings of a double-door
	public boolean synchronizePairedDoor(Openable pairedOpenable, boolean destinationState) {
		// we expect that pairedOpenable is only set if synchronization is enabled in config
		if (pairedOpenable== null) {
			return false;
		}
		
		if (pairedOpenable.hasIdChanged()) {
			printDebug("DEBUG: Paired door material has changed - ignoring.");			
			return false;
		}

		boolean isPairedDoorOpen = pairedOpenable.isOpen();
		if (isPairedDoorOpen == destinationState) {
			printDebug("DEBUG: Paired door is already in destination state.");			
			return false;
		}

		boolean doorChanged;
		if (destinationState) {
			printDebug("DEBUG: Synchronizing paired door to open.");			
			doorChanged = pairedOpenable.open();
		} else {
			printDebug("DEBUG: Synchronizing paired door to close.");
			doorChanged = pairedOpenable.close();
		}
		if (!doorChanged) {
			printDebug("DEBUG: Can not change door state. Something must be happened outside this plugin");
		}
		return true;
	}
	
	
	public void scheduleOneTickLaterTask(Openable clickedOpenable, Openable pairedOpenable, boolean openState) {
		OneTickLaterTask task = new OneTickLaterTask(configManager, this, clickedOpenable, pairedOpenable, openState);
		task.runTask(plugin);
		oneTickLaterTasks.add(task);
	}


	public void scheduleCloseTask(Openable clickedOpenable, Openable pairedOpenable, int seconds) {
		DoorCloseTask task = new DoorCloseTask(configManager, this, clickedOpenable, pairedOpenable);
		task.runTaskLater(plugin, seconds * TICKS_PER_SECOND);
		doorCloseTasks.add(task);
	} 
	
	
	public void removeOneTickLaterTask (OneTickLaterTask task) {
		oneTickLaterTasks.remove(task);
	}
	
	
	public void removeDoorCloseTask (DoorCloseTask task) {
		doorCloseTasks.remove(task);
	}
	
	
	protected void cancelOneTickLaterTask() {
		List<OneTickLaterTask> myList = new ArrayList<>(oneTickLaterTasks);
		for (OneTickLaterTask myTask : myList) {
			myTask.cancel();
			myTask.handleInteraction(true);
		}
	}
	
	
	protected void cancelDoorCloseTask() {
		List<DoorCloseTask> myList = new ArrayList<>(doorCloseTasks);
		for (DoorCloseTask myTask : myList) {
			myTask.cancel();
			myTask.handleClose(true);
		}
	}
	
	
	public void cancelAll() {
		printDebug("Cancelling all");
		cancelOneTickLaterTask();
		cancelDoorCloseTask();
	}
	
	
	public int getTaskCount() {
		return oneTickLaterTasks.size() + doorCloseTasks.size();
	}

}
