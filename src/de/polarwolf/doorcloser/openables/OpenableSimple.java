package de.polarwolf.doorcloser.openables;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import de.polarwolf.doorcloser.materials.Material;

public abstract class OpenableSimple implements Openable {
	
	protected Position position;
	protected int id;
	
	
	protected OpenableSimple(Block block) {
		this.position = block;
		this.id = block.getId();		
	}
	

	public static Openable fromBlock(Block block) {
		if (block instanceof BlockDoor) {
			BlockDoor blockDoor = (BlockDoor) block;
			return new OpenableDoor (blockDoor);
		}
		if (block instanceof BlockFenceGate) {
			BlockFenceGate blockFenceGate = (BlockFenceGate) block;
			return new OpenableFenceGate (blockFenceGate);
		}
		if (block instanceof BlockTrapdoor) {
			BlockTrapdoor blockTrapdoor = (BlockTrapdoor) block;
			return new OpenableTrapdoor (blockTrapdoor);
		}
		return null;
	}
	
	
	@Override
	public Block getBlock() {
		Block myBlock = position.getLevelBlock();
		if (myBlock.getId() != id) {
			return null;
		}
		return myBlock;
	}
	
	
	@Override
	public boolean hasIdChanged() {
		return (getBlock() == null);
	}
	

	protected abstract boolean toggleOpenState();

	
	@Override
	public boolean open() {
		if (isOpen()) {
			return false;
		}
		return toggleOpenState();
	}
		

	@Override
	public boolean close() {
		if (!isOpen()) {
			return false;
		}
		return toggleOpenState();
	}

	
	@Override
	public void playCloseSound() {
		Block myBlock = getBlock();
		myBlock.getLevel().addSound(myBlock, Sound.RANDOM_DOOR_CLOSE);
	}
	
	@Override
	public Material getMaterial() {
		return new Material (getBlock().getId());
	}
		

}