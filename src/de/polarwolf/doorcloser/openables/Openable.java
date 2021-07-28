package de.polarwolf.doorcloser.openables;

import cn.nukkit.block.Block;
import cn.nukkit.math.BlockFace;
import de.polarwolf.doorcloser.materials.Material;

public interface Openable {
	
	public abstract boolean hasIdChanged();
		
	public abstract Block getBlock();
	
	public abstract boolean isOpen();
	
	public abstract boolean open();

	public abstract boolean close();

	public abstract void playCloseSound();
	
	public abstract BlockFace getBlockFace();
	
	public abstract boolean isRightHinged();
	
	public abstract Material getMaterial();

}
