// This work contains code from the original DoorCloser-Plugin written by Psychlist1972
// He has released his work using the Apache 2.0 license
// Please see https://github.com/Psychlist1972/Minecraft-DoorCloser for reference

package de.polarwolf.doorcloser.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cn.nukkit.block.Block;
import cn.nukkit.utils.ConfigSection;
import de.polarwolf.doorcloser.exception.DoorCloserException;
import de.polarwolf.doorcloser.materials.Material;
import de.polarwolf.doorcloser.openables.OpenableDoor;
import de.polarwolf.doorcloser.openables.OpenableFenceGate;
import de.polarwolf.doorcloser.openables.OpenableSimple;
import de.polarwolf.doorcloser.openables.OpenableTrapdoor;

public class ConfigData {
	
	protected static final String SECONDSTOREMAINOPEN_KEY = "Time";
	protected static final int SECONDSTOREMAINOPEN_DEFAULT = 5;
	protected int secondsToRemainOpenData = SECONDSTOREMAINOPEN_DEFAULT;

	protected static final String SYNCHRONIZEDOUBLEDOOR_KEY = "SynchronizeDoubleDoor";
	protected static final boolean SYNCHRONIZEDOUBLEDOOR_DEFAULT = false;
	protected boolean synchronizeDoubleDoorData = SYNCHRONIZEDOUBLEDOOR_DEFAULT;

	protected static final String OPTIMISTICDOUBLEDOORSYNC_KEY = "OptimisticDoubleDoorSync";
	protected static final boolean OPTIMISTICDOUBLEDOORSYNC_DEFAULT = false;
	protected boolean optimisticDoubleDoorSync = OPTIMISTICDOUBLEDOORSYNC_DEFAULT;
	
	protected static final String PLAYSOUND_KEY = "PlaySound";
	protected static final boolean PLAYSOUND_DEFAULT = true;
	protected boolean playSoundData = PLAYSOUND_DEFAULT;

	protected static final String IGNOREIFINCREATIVE_KEY = "IgnoreIfInCreative";
	protected static final boolean IGNOREIFINCREATIVE_DEFAULT = true;
	protected boolean ignoreIfInCreativeData = IGNOREIFINCREATIVE_DEFAULT;

	protected static final String IGNOREIFSNEAKING_KEY = "IgnoreIfSneaking";
	protected static final boolean IGNOREIFSNEAKING_DEFAULT = false;
	protected boolean ignoreIfSneakingData = IGNOREIFSNEAKING_DEFAULT;
	
	protected static final String INCLUDEALLDOORS_KEY = "IncludeAllDoors";
	protected static final boolean INCLUDEALLDOORS_DEFAULT = false;
	protected boolean includeAllDoors = INCLUDEALLDOORS_DEFAULT;
	
	protected static final String INCLUDEALLGATES_KEY = "IncludeAllGates";
	protected static final boolean INCLUDEALLGATES_DEFAULT = false;
	protected boolean includeAllGates = INCLUDEALLGATES_DEFAULT;

	protected static final String INCLUDEALLTRAPDOORS_KEY = "IncludeAllTrapDoors";
	protected static final boolean INCLUDEALLTRAPDOORS_DEFAULT = false;
	protected boolean includeAllTrapDoors = INCLUDEALLTRAPDOORS_DEFAULT;

	protected static final String INCLUDE_KEY = "Include";	
	protected static final String EXCLUDE_KEY = "Exclude";

	protected Set<Material> openablesInScope = new HashSet<>();
	

	public int getSecondsToRemainOpen() {
		return secondsToRemainOpenData;
	}


	public boolean isSynchronizeDoubleDoor() {
		return synchronizeDoubleDoorData;
	}


	public boolean isOptimisticDoubleDoorSync() {
		return optimisticDoubleDoorSync;
	}


	public boolean isPlaySound() {
		return playSoundData;
	}
	

	public boolean isIgnoreIfInCreative() {
		return ignoreIfInCreativeData;
	}
	

	public boolean isIgnoreIfSneaking() {
		return ignoreIfSneakingData;
	}
	

	public Set<Material> getOpenablesInScope() {
		return new HashSet<>(openablesInScope);
	}
		
	
	protected Set<Material> parseOpenableList(String listName, List<String> materialNames) throws DoorCloserException {
		Set<Material> mySet = new HashSet<>();
		for (String myName : materialNames) {
			 Material  myMaterial = Material.fromNameOrID(myName);
			 if (myMaterial == null) {
				 throw new DoorCloserException(listName, "Unknown Material", myName);
			} 
			 if (OpenableSimple.fromBlock(Block.get(myMaterial.getId())) == null) {
				 throw new DoorCloserException(listName, "Material is not an openable", myName);				 
			 }
			 mySet.add(myMaterial);
		}
		return mySet;
	}
	
	
	public void load(ConfigSection config) throws DoorCloserException {

		try {
			openablesInScope.clear();
			Set<Material> excludesInScope = new HashSet<>();

			secondsToRemainOpenData = config.getInt(SECONDSTOREMAINOPEN_KEY, SECONDSTOREMAINOPEN_DEFAULT);
			synchronizeDoubleDoorData = config.getBoolean(SYNCHRONIZEDOUBLEDOOR_KEY, SYNCHRONIZEDOUBLEDOOR_DEFAULT);
			playSoundData = config.getBoolean(PLAYSOUND_KEY, PLAYSOUND_DEFAULT);
			optimisticDoubleDoorSync = config.getBoolean(OPTIMISTICDOUBLEDOORSYNC_KEY, OPTIMISTICDOUBLEDOORSYNC_DEFAULT);
			ignoreIfInCreativeData = config.getBoolean(IGNOREIFINCREATIVE_KEY, IGNOREIFINCREATIVE_DEFAULT);
			ignoreIfSneakingData = config.getBoolean(IGNOREIFSNEAKING_KEY, IGNOREIFSNEAKING_DEFAULT);
		
			includeAllDoors = config.getBoolean(INCLUDEALLDOORS_KEY, INCLUDEALLDOORS_DEFAULT);
			includeAllGates = config.getBoolean(INCLUDEALLGATES_KEY, INCLUDEALLGATES_DEFAULT);
			includeAllTrapDoors = config.getBoolean(INCLUDEALLTRAPDOORS_KEY, INCLUDEALLTRAPDOORS_DEFAULT);
			
			openablesInScope.addAll(parseOpenableList(INCLUDE_KEY, config.getStringList(INCLUDE_KEY)));
			excludesInScope.addAll(parseOpenableList(EXCLUDE_KEY, config.getStringList(EXCLUDE_KEY)));

			if (includeAllDoors) {
				openablesInScope.addAll(OpenableDoor.enumAllDoorMaterials());			
			}
			if (includeAllGates) {
				openablesInScope.addAll(OpenableFenceGate.enumAllFenceGateMaterials());			
			}
			if (includeAllTrapDoors) {
				openablesInScope.addAll(OpenableTrapdoor.enumAllTrapdoorMaterials());			
			}
		
			openablesInScope.removeAll(excludesInScope);		

		} catch (DoorCloserException e) {
			throw e;
		} catch (Exception e) {
			throw new DoorCloserException (null, "Error loading config", e.getMessage(), e);
		}		
	}
	
}
