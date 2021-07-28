package de.polarwolf.doorcloser.openables;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.math.BlockFace;
import de.polarwolf.doorcloser.materials.Material;

public class OpenableTrapdoor extends OpenableSimple {
	
	protected OpenableTrapdoor(BlockTrapdoor blockTrapdoor) {
		super(blockTrapdoor);
	}
	
	
	public static Set<Material> enumAllTrapdoorMaterials() {
		Set<Material> materialSet = new HashSet<>();
		for (Material myMaterial : Material.enumAllMaterials()) {
			if (Block.get(myMaterial.getId()) instanceof BlockTrapdoor) {
				materialSet.add(myMaterial);
			}
		}
		return materialSet;
	}


	public BlockTrapdoor getBlockTrapdoor() {
		return (BlockTrapdoor) getBlock();
	}
	
	
	@Override
	public boolean isOpen() {
		return getBlockTrapdoor().isOpen();
	}
	
	
	@Override
	public BlockFace getBlockFace() {
		return getBlockTrapdoor().getBlockFace();
	}


	@Override
	public boolean isRightHinged() {
		return false;
	}
		
	
	@Override
	protected boolean toggleOpenState() {
		return getBlockTrapdoor().toggle(null);
	}

}
