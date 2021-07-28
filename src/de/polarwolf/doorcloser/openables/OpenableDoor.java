package de.polarwolf.doorcloser.openables;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.math.BlockFace;
import de.polarwolf.doorcloser.materials.Material;

public class OpenableDoor extends OpenableSimple {
	
	protected OpenableDoor(BlockDoor blockDoor) {
		super (blockDoor.isTop() ? blockDoor.down() : blockDoor);
	}
		
	
	public static Set<Material> enumAllDoorMaterials() {
		Set<Material> materialSet = new HashSet<>();
		for (Material myMaterial : Material.enumAllMaterials()) {
			if (Block.get(myMaterial.getId()) instanceof BlockDoor) {
				materialSet.add(myMaterial);
			}
		}
		return materialSet;
	}
	
	
	public BlockDoor getBlockDoor() {
		return (BlockDoor) getBlock();
	}
	
	
	@Override
	public boolean isOpen() {
		return getBlockDoor().isOpen();
	}
	
	
	@Override
	public BlockFace getBlockFace() {
		return getBlockDoor().getBlockFace();
	}


	@Override
	public boolean isRightHinged() {
		return getBlockDoor().isRightHinged();
	}
	
	
	@Override
	protected boolean toggleOpenState() {
		return getBlockDoor().toggle(null);
	}
	
}