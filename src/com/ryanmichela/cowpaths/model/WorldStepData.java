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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * The WorldStepData class manages the loading and unloading of ChunkStepData objects for a given World.
 * @author Ryan
 *
 */
public class WorldStepData {
	private File worldPageDirectory;
	private HashMap<String, ChunkStepData> chunks = new HashMap<String, ChunkStepData>(512);
	
	public WorldStepData(File pageDirectory, World world) {
		if(!pageDirectory.isDirectory()) throw new IllegalArgumentException("Page directory must be a directory");
		worldPageDirectory = new File(pageDirectory, world.getName());
		if(!worldPageDirectory.exists()) worldPageDirectory.mkdir();
	}
	
	/**
	 * Retrieves the BlockStepData for a given Block. Pages in the block's chunk if not already loaded.
	 * @param block
	 * @return
	 * @throws Exception
	 */
	public StepData getStepData(Block block) throws Exception{
		String chunkKey = ChunkStepData.makeKey(block.getChunk());
		
		if(!chunks.containsKey(chunkKey)) {
			pageInChunk(block.getChunk());
		}
		
		ChunkStepData csd = chunks.get(chunkKey);
		return csd.getStepData(block);
	}	
	
	/**
	 * Pages a given chunk in from disk and adds it to the loaded chunks collection.
	 * @param chunk
	 * @throws Exception
	 */
	public void pageInChunk(Chunk chunk) throws Exception{
		String chunkKey = ChunkStepData.makeKey(chunk);
		ChunkStepData page;
		
		// Check the disk to see if the ChunkStepData for this block has been
		// paged out. If so, load it.
		File csdPageFile = new File(worldPageDirectory, chunkKey);
		try {
			ObjectInputStream iStream = new ObjectInputStream(new FileInputStream(csdPageFile));
			page = (ChunkStepData) iStream.readObject();
			iStream.close();
			
		} catch(FileNotFoundException e) {		
			// If the ChunkStepData cannot be found on the disk, create a new ChunkStepData
			page = new ChunkStepData(chunk);
		}
		
		chunks.put(chunkKey, page);
	}
	
	/**
	 * Pages a given chunk out to disk and removes it from the loaded chunks collection.
	 * @param chunk
	 * @throws Exception
	 */
	public void pageOutChunk(Chunk chunk) throws Exception {
		String chunkKey = ChunkStepData.makeKey(chunk);
		
		if(!chunks.containsKey(ChunkStepData.makeKey(chunk))) {
			return;
		}
		
		// Write the ChunkStepData to disk
		File csdPageFile = new File(worldPageDirectory, chunkKey);
		ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(csdPageFile));
		oStream.writeObject(chunks.get(chunkKey));
		oStream.close();
		
		chunks.remove(chunkKey);
	}
	
	/**
	 * Flushes all loaded chunks to disk
	 * @throws Exception
	 */
	public void flush() throws Exception {
		for(ChunkStepData csd : chunks.values()) {
			// Write the ChunkStepData to disk
			File csdPageFile = new File(worldPageDirectory, csd.chunkKey());
			ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(csdPageFile));
			oStream.writeObject(csd);
			oStream.close();
		}
	}
}
