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

package com.ryanmichela.cowpaths.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import com.ryanmichela.cowpaths.controller.StepConfiguration.WearPattern;
import com.ryanmichela.cowpaths.model.StepData;
import com.ryanmichela.cowpaths.model.WorldStepData;

public class StepController {

	private StepConfiguration config;
	private File dataDir;
	private Logger log;
	
	private Map<World, WorldStepData> worldStepDatas = new HashMap<World, WorldStepData>();
	
	public StepController(Plugin plugin, StepConfiguration config) {
		this.config = config;
		this.log = plugin.getServer().getLogger();
		dataDir = plugin.getDataFolder();
		if(!dataDir.exists()) dataDir.mkdir();
	}
	
	/**
	 * Controls the process of stepping on a block
	 * @param block
	 */
	public void stepOnBlock(Block block) {
		try {
			// Locate the step data
			WorldStepData wsd = getWsd(block.getWorld());
			StepData sd = wsd.getStepData(block);
			
			// Verify the block material is still the same
			// Reset the step count if anything has changed
			// Ex: dirt -> grass
			if(block.getType() != sd.lastKnownMaterial) {
				sd.stepCount = 0;
				sd.lastKnownMaterial = block.getType();
			}
			
			// Increment the step count
			sd.stepCount++;
			sd.totalSteps++;
			
			// Locate the first wear pattern that starts with the current
			// block material and apply it if necessary
			for(WearPattern wp : config.getWearPatterns()) {
				if(sd.lastKnownMaterial == wp.fromMaterial() && sd.stepCount >= wp.stepThreshhold()) {
					block.setType(wp.toMaterial());
				}
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error loading step data.", e);
		}
	}
	
	/**
	 * Populates internal data structures when a chunk is loaded
	 * @param chunk
	 */
	public void loadChunk(Chunk chunk) {
		try {
			WorldStepData wsd = getWsd(chunk.getWorld());
			wsd.pageInChunk(chunk);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error paging in chunk step data.", e);
		}
	}
	
	/**
	 * Prunes internal data structures when a chunk is unloaded
	 * @param chunk
	 */
	public void unloadChunk(Chunk chunk) {
		try {
			WorldStepData wsd = getWsd(chunk.getWorld());
			wsd.pageOutChunk(chunk);
		} catch (Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error paging out chunk step data.", e);
		}
	}
	
	/**
	 * Loads into memory all active chunks in a world
	 * @param world
	 */
	public void prime(World world) {
		for(Chunk chunk : world.getLoadedChunks()) {
			loadChunk(chunk);
		}
	}
	
	/**
	 * Flushes all loaded chunks to disk
	 */
	public void flush() {
		try {
			for(WorldStepData wsd : worldStepDatas.values()) {
				wsd.flush();
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error flushing out chunk step data.", e);
		}
	}
	
	public int getStepCount(Block block) {
		try {
			WorldStepData wsd = getWsd(block.getWorld());
			StepData sd = wsd.getStepData(block);
			return sd.stepCount;
		} catch(Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error loading step data.", e);
			return 0;
		}
	}
	
	public int getTotalSteps(Block block) {
		try {
			WorldStepData wsd = getWsd(block.getWorld());
			StepData sd = wsd.getStepData(block);
			return sd.totalSteps;
		} catch(Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error loading step data.", e);
			return 0;
		}
	}
	
	private WorldStepData getWsd(World world) {
		if(!worldStepDatas.containsKey(world)) {
			WorldStepData wsd = new WorldStepData(dataDir, world);
			worldStepDatas.put(world, wsd);
			return wsd;
		} else {
			return worldStepDatas.get(world);
		}
	}
}
