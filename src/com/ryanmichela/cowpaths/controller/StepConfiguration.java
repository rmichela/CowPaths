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
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

public class StepConfiguration {

	private Plugin plugin;
	private Configuration bkConfig;
	
	public StepConfiguration(Plugin plugin) {
		this.plugin = plugin;
		this.bkConfig = plugin.getConfiguration();
	}

	/**
	 * Constructs a set of WearPattern objects from the configuration
	 * @return
	 */
	public List<WearPattern> getWearPatterns() {		
		List<String> wearStrings = bkConfig.getStringList("wearPatterns", null);
		List<WearPattern> wearPatterns = new ArrayList<WearPattern>(wearStrings.size());
		
		for(String wearString : wearStrings) {
			try {
				wearPatterns.add(new WearPattern(wearString));
			} catch (IllegalArgumentException e) {
				plugin.getServer().getLogger().warning("[Cow Paths] " + e.getMessage());
			}
		}
		return wearPatterns;
	}
	
	/**
	 * Returns the contents of the default config file
	 * @return
	 */
	public static String initialConfigFile() {
		return 
		       "#Format: fromMaterial toMaterial stepThreshhold\n" +
		       "wearPatterns:\n" +
		       "    - Grass Dirt 10\n" +
		       "    - Dirt Gravel 50\n" +
		       "    - Gravel Cobblestone 200\n" +
		       "    - Cobblestone Stone 800\n" +
		       "    - Sand Sandstone 100";
	}
	
	public File getPageFileDirectory() {
		return new File(plugin.getDataFolder(), "pagefile");
	}
	
	public class WearPattern {
		private Material fromMaterial;
		private Material toMaterial;
		private int stepThreshhold;
		
		public WearPattern(String wearString) {
			try {
				String[] splits = wearString.split(" ");
				fromMaterial = Material.matchMaterial(splits[0]);
				toMaterial = Material.matchMaterial(splits[1]);
				stepThreshhold = Integer.parseInt(splits[2]);
				if(fromMaterial == null || toMaterial == null) throw new Exception();
			} catch (Exception e) {
				throw new IllegalArgumentException("Malformed wearPattern in config - " + wearString);
			}
		}
		
		public Material fromMaterial() { return fromMaterial; }
		public Material toMaterial() { return toMaterial; }
		public int stepThreshhold() { return stepThreshhold; }
		
		public String toString() {
			return "Wear Pattern: " + fromMaterial + " => " + toMaterial + " (" + stepThreshhold + " steps)";
		}
	}
}
