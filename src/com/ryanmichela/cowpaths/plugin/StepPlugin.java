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

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

import com.ryanmichela.cowpaths.controller.StepConfiguration;
import com.ryanmichela.cowpaths.controller.StepController;

public class StepPlugin extends JavaPlugin {

	private StepController controller;
	private StepConfiguration config;
	
	@Override
	public void onLoad() {
		config = new StepConfiguration(this);
		controller = new StepController(this, config);
	}
	
	@Override
	public void onEnable() {
		StepPlayerListener spl = new StepPlayerListener(controller);
		getServer().getPluginManager().registerEvent(Type.PLAYER_MOVE, spl, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_TELEPORT, spl, Priority.Normal, this);
		
		StepWorldListener swl = new StepWorldListener(controller);
		getServer().getPluginManager().registerEvent(Type.CHUNK_LOAD, swl, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.CHUNK_UNLOAD, swl, Priority.Normal, this);
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}
}