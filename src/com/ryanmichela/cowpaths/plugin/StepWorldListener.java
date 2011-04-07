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

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

import com.ryanmichela.cowpaths.controller.StepController;

public class StepWorldListener extends WorldListener {

	private StepController controller;
	
	public StepWorldListener(StepController controller) {
		this.controller = controller;
	}
	
	@Override
	public void onChunkLoad(ChunkLoadEvent event) {
		controller.loadChunk(event.getChunk());
	}

	@Override
	public void onChunkUnload(ChunkUnloadEvent event) {
		controller.unloadChunk(event.getChunk());
	}

}
