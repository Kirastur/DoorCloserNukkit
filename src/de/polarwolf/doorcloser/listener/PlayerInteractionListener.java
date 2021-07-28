// This work contains code from the original DoorCloser-Plugin written by Psychlist1972
// He has released his work using the Apache 2.0 license
// Please see https://github.com/Psychlist1972/Minecraft-DoorCloser for reference

package de.polarwolf.doorcloser.listener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import de.polarwolf.doorcloser.butler.ButlerManager;
import de.polarwolf.doorcloser.config.ConfigManager;
import de.polarwolf.doorcloser.openables.Openable;
import de.polarwolf.doorcloser.openables.OpenableDoor;
import de.polarwolf.doorcloser.openables.OpenableSimple;

public class PlayerInteractionListener implements Listener {
	
	protected final ConfigManager configManager;
	protected final ButlerManager butlerManager;
	

	public PlayerInteractionListener(ConfigManager configManager, ButlerManager butlerManager) {
		this.configManager = configManager;
		this.butlerManager = butlerManager;
	}
	
	
	protected Openable findOpenableFromInteraction(Block clickedBlock, Player player, Action action) {

		// right clicks only
		if (action != Action.RIGHT_CLICK_BLOCK) {
			return null;
		}

		// check to see if we're ignoring creative mode
		if (player.isCreative() && (configManager.getConfigData().isIgnoreIfInCreative())) {
			return null;
		}
	
		// check to see if we're ignoring sneaking
		if ((player.isSneaking()) && (configManager.getConfigData().isIgnoreIfSneaking())) {
			return null;
		}
		
		// check to see if we care about this type of block. In our case, we want
		// something that implements Openable (gate, trap door, door).
		return OpenableSimple.fromBlock(clickedBlock);
	}
	

	protected Openable findPairedDoor(Openable clickedOpenable) {
		if (!(clickedOpenable instanceof OpenableDoor)) {
			butlerManager.printDebug("DEBUG: Block is not a door - no paired door detection needed.");
			return null;
		}
		
		butlerManager.printDebug("DEBUG: door face=" + clickedOpenable.getBlockFace().name());
		butlerManager.printDebug("DEBUG: door isOpen()=" + clickedOpenable.isOpen());
		butlerManager.printDebug("DEBUG: door isRightHinged=" + clickedOpenable.isRightHinged());

		Openable pairedOpenable = butlerManager.findPairedOpenable(clickedOpenable);
			
		// check to see if that block is actually a door
		if (!(pairedOpenable instanceof OpenableDoor)) {
			// neighbor block is not a door.
			// door is a single door, not a double door
			butlerManager.printDebug("DEBUG: Neighbor block is not a door. So this is not a double door.");
			return null;
		}

		butlerManager.printDebug("DEBUG: Door neighbor is a door.");
		butlerManager.printDebug("DEBUG: paired door face=" + pairedOpenable.getBlockFace().name());
		butlerManager.printDebug("DEBUG: paired door isOpen()=" + pairedOpenable.isOpen());
		butlerManager.printDebug("DEBUG: paired door isRightHingeed=" + pairedOpenable.isRightHinged());
			
		// check the block we found that is opposite the hinge. If it
		// is a door and has a hinge that is opposite this one, then
		// it is our pair
		if (!(clickedOpenable.isRightHinged() ^ pairedOpenable.isRightHinged())) {
			// neighbor block has hinge on same side
			// not the pair for this door
			butlerManager.printDebug("DEBUG: Neighbor has hinge on same side. Not a double door.");
			return null;			
		}
		
		butlerManager.printDebug("DEBUG: Found paired / double door.");
		// we're good!
		return pairedOpenable;
	}
	

	protected void handleOpenable(Openable clickedOpenable) {

		butlerManager.printDebug("DEBUG: Performing DoorCloser Checks.");		
		Openable pairedOpenable = null; 

		// Handling door-specific stuff
		// Please remember: synchronizing doors is only active if doorCloser is active,
		// but does not check about the material
		// Remember: You must use the down-block of a door
		// This is adjusted in OpenableDoor Creator, so no need to check it here
		if (configManager.getConfigData().isSynchronizeDoubleDoor()) {
			pairedOpenable = findPairedDoor (clickedOpenable);
			if (configManager.getConfigData().isOptimisticDoubleDoorSync()) {
				butlerManager.synchronizePairedDoor(pairedOpenable, !clickedOpenable.isOpen());
			}
		}

		// We have checked all, now we can trigger the one-tick-later Task
		butlerManager.printDebug("DEBUG: Preparing one-tick-later Task.");		
		butlerManager.scheduleOneTickLaterTask(clickedOpenable, pairedOpenable, clickedOpenable.isOpen());
	}
		      				
	
	protected void handlePlayerInteractionEvent(PlayerInteractEvent e) {
		Block clickedBlock = e.getBlock();
		Player player = e.getPlayer();
		Action action = e.getAction();
		Openable clickedOpenable = findOpenableFromInteraction(clickedBlock, player, action);
		if (clickedOpenable != null) {
			handleOpenable(clickedOpenable);
		}
	}


	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerInteractionEvent(PlayerInteractEvent event) {
		try {
			handlePlayerInteractionEvent(event);
		} catch (Exception e) {
			butlerManager.printException(e);
		}
	}

}
