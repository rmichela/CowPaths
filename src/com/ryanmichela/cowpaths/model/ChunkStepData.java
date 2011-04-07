//Copyright (C) 2011  Ryan Michela
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.ryanmichela.cowpaths.model;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class ChunkStepData implements Serializable{

	private static final long serialVersionUID = -7982687141234124948L;
	private int chunkX;
	private int chunkZ;
	private HashMap<String, StepData> stepDatas = new HashMap<String, StepData>(256);
	
	public ChunkStepData(Chunk chunk) {
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
	}
	
	public int x() {
		return chunkX;
	}
	
	public int z() {
		return chunkZ;
	}
	
	/**
	 * Gets the StepData for a given block in this chunk. Will create the StepData if necessary.
	 * @param block
	 * @return
	 */
	public StepData getStepData(Block block) {
		if(!isBlockInChunk(block.getX(), block.getZ())) {
			throw new IllegalArgumentException("Block not in ChunkStepData");
		}
		
		String sdKey = StepData.makeKey(block);
		if(!stepDatas.containsKey(sdKey)) {
			// First time this block has been stepped on, create its step data
			stepDatas.put(sdKey, new StepData(block));
		}
		
		return stepDatas.get(sdKey);
	}
	
	/**
	 * Gets the ChunkStepData key that references this object.
	 * @return
	 */
	public String chunkKey() {
		return makeKey(chunkX, chunkZ);
	}
	
	/**
	 * Creates a ChunkStepData key for a given x,z world coordinate.
	 * @param x
	 * @param z
	 * @return
	 */
	public static String makeKey(int x, int z) {
		return Integer.toString(x) + "." + Integer.toString(z) + ".dat";
	}
	
	/**
	 * Creates a ChunkStepData key that references a given Chunk.
	 * @param chunk
	 * @return
	 */
	public static String makeKey(Chunk chunk) {
		int x = chunk.getX();
		int z = chunk.getZ();
		
		return makeKey(x, z);
	}
	
	/**
	 * Tests to see of a block coordinate lies within this chunk.
	 * @param bx
	 * @param bz
	 * @param cx
	 * @param cz
	 * @return
	 */
	private boolean isBlockInChunk(int bx, int bz) {
		return (bx - chunkX*16 < 16) && (bx - chunkX*16 >= 0) &&
		       (bz - chunkZ*16 < 16) && (bz - chunkZ*16 >= 0);
	}
}
