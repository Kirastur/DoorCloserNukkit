package de.polarwolf.doorcloser.materials;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.block.Block;

public class Material {
	
	protected static final String UNKNOWN_BLOCK_NAME = "Unknown";

	protected final int id;
	
	
	public Material(int id) {
		this.id = id;
	}
	

	public static String getMaterialName(int id) {
		Block block = Block.get(id);	
		return block.getName();
	}
	

	public static Integer findMaterialID(String materialName) {
		for (int id = 0; id < 256; id++) {
			String myMaterialName = getMaterialName(id);
			if ((myMaterialName != null) && myMaterialName.equals(materialName)) {
				return id;
			}
		}
		return null;		
	}
	

	public static boolean isValidMaterial(int id) {
		return (id >= 0) && (id < 256) || (!getMaterialName(id).equals(UNKNOWN_BLOCK_NAME));
	}
	
	
	public static Material fromNameOrID(String materialName) {
		Integer id = Integer.getInteger(materialName);
		if (id == null) {
			id = findMaterialID(materialName);
		}
		
		if ((id == null) || !isValidMaterial(id)) {
			return null;
		}
		return new Material(id);
	}
	
	
	public static Set<Material> enumAllMaterials() {
		Set<Material> allMaterials = new HashSet <>();
		for (int id = 0; id < 256; id++) {
			if (isValidMaterial(id)) {
				allMaterials.add(new Material(id));
			}
		}
		return allMaterials;
	}


	public int getId() {
		return id;
	}
	
	
	public String getName() {
		return getMaterialName(id);
	}
	
	
	@Override 
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		Material other = (Material) obj;
		return id == other.getId();
	}
	

	@Override
    public int hashCode() {
    	return id;
    }
	
}
