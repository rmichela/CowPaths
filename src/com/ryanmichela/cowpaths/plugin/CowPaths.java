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

package com.ryanmichela.cowpaths.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

import com.ryanmichela.cowpaths.api.CowPathsApi;
import com.ryanmichela.cowpaths.controller.StepConfiguration;
import com.ryanmichela.cowpaths.controller.StepController;
import com.ryanmichela.cowpaths.controller.StepConfiguration.WearPattern;

public class CowPaths extends JavaPlugin {

	private StepController controller;
	private StepConfiguration config;
	private CowPathsApi api;
	private Logger log;
	private boolean loadError;
	
	@Override
	public void onLoad() {
		try {
			log = getServer().getLogger();
			
			// Initialize configuration
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
				
				log.info("[Cow Paths] Populating initial config file");
				OutputStreamWriter out = new OutputStreamWriter(
											new FileOutputStream(
												new File(getDataFolder(), "config.yml")));
				out.write(StepConfiguration.initialConfigFile());
				out.close();
				
				getConfiguration().load();
			}
	
			config = new StepConfiguration(this);
			controller = new StepController(this, config);
			api = new CowPathsApi(controller);
			
			for(WearPattern wp: config.getWearPatterns()) {
				log.info("[Cow Paths] " + wp);
			}
		
		} catch (Exception e) {
			log.log(Level.SEVERE, "[Cow Paths] Error in initialization.", e);
			loadError = true;
		}
	}
	
	@Override
	public void onEnable() {
		if (!loadError) {
			// Wire up event listeners
			StepPlayerListener spl = new StepPlayerListener(controller);
			getServer().getPluginManager().registerEvent(Type.PLAYER_MOVE, spl, Priority.Normal, this);
			getServer().getPluginManager().registerEvent(Type.PLAYER_TELEPORT, spl, Priority.Normal, this);
			StepWorldListener swl = new StepWorldListener(controller);
			getServer().getPluginManager().registerEvent(Type.CHUNK_LOAD, swl, Priority.Normal, this);
			getServer().getPluginManager().registerEvent(Type.CHUNK_UNLOAD, swl, Priority.Normal, this);
			
			// Prime the active chunk data
			for(World world : getServer().getWorlds()) {
				controller.prime(world);
			}
			
			log.info("[Cow Paths] Now paving cow paths. Happy trails!");
		}
	}

	@Override
	public void onDisable() {
		if(!loadError) {
			log.info("[Cow Paths] Flushing block data.");
			controller.flush();
		}
	}
	
	public CowPathsApi api() {
		return api;
	}
}
