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

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author Ryan
 * StepData represents the step count metadata for a specific block in the world
 */
public class StepData implements Serializable{
	private static final long serialVersionUID = -5561589225979464484L;
	private int x;
	private int y;
	private int z;
	
	public StepData(Block block) {
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}
	
	public int x() {return x;}
	public int y() {return y;}
	public int z() {return z;}
	
	public Material lastKnownMaterial;
	
	public int stepCount;
	public int totalSteps;

	/**
	 * Gets this block's unique block key
	 * @return
	 */
	public String key() {
		return makeKey(x, y, z);
	}
	
	/**
	 * Creates a unique block key for a given set of coordiantes
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private static String makeKey(int x, int y, int z) {
		return Integer.toString(x) + "." + Integer.toString(z) + "." + Integer.toString(y);
	}
	
	/**
	 * Creates a unique block key for a given block
	 * @param block
	 * @return
	 */
	public static String makeKey(Block block) {
		return makeKey(block.getX(), block.getY(), block.getZ());
	}
}
