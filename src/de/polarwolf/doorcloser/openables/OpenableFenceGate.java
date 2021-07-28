package de.polarwolf.doorcloser.openables;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.math.BlockFace;
import de.polarwolf.doorcloser.materials.Material;

public class OpenableFenceGate extends OpenableSimple {
	
	protected OpenableFenceGate(BlockFenceGate blockFenceGate) {
		super(blockFenceGate);
	}
	

	public static Set<Material> enumAllFenceGateMaterials() {
		Set<Material> materialSet = new HashSet<>();
		for (Material myMaterial : Material.enumAllMaterials()) {
			if (Block.get(myMaterial.getId()) instanceof BlockFenceGate) {
				materialSet.add(myMaterial);
			}
		}
		return materialSet;
	}
	
	
	public BlockFenceGate getBlockFenceGate() {
		return (BlockFenceGate) getBlock();
	}

	
	@Override
	public boolean isOpen() {
		return getBlockFenceGate().isOpen();
	}
	
	
	@Override
	public BlockFace getBlockFace() {
		return getBlockFenceGate().getBlockFace();
	}


	@Override
	public boolean isRightHinged() {
		return false;
	}
		
	
	@Override
	protected boolean toggleOpenState() {
		return getBlockFenceGate().toggle(null);
	}
		
}