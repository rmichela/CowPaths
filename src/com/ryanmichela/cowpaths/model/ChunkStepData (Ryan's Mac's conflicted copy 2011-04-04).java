package com.ryanmichela.cowpaths.model;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.block.BlockState;

public class ChunkStepData implements Serializable{

	private static final long serialVersionUID = -4255215246847257480L;
	private int chunkX;
	private int chunkZ;
	private HashMap<String, StepData> stepDatas = new HashMap<String, StepData>(256);
	
	public ChunkStepData(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}
	
	public int x() {
		return chunkX;
	}
	
	public int z() {
		return chunkZ;
	}
	
	public void addStepData(StepData sd) {
		stepDatas.put(sd.key(), sd);
	}
	
	public StepData getStepData(int x, int y, int z) {
		return stepDatas.get(StepData.makeKey(x, y, z));
	}
	
	public String chunkKey() {
		return makeKey(chunkX, chunkZ);
	}
	
	public static String makeKey(int x, int z) {
		return Integer.toString(x) + ":" + Integer.toString(z);
	}
	
	public static String makeKey(BlockState block) {
		int x = block.getChunk().getX();
		int z = block.getChunk().getZ();
		
		return makeKey(x, z);
	}
}
