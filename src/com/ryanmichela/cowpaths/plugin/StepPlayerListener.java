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

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.ryanmichela.cowpaths.controller.StepController;

public class StepPlayerListener extends PlayerListener {

	private StepController controller;
	
	public StepPlayerListener(StepController controller) {
		this.controller = controller;
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		controller.stepOnBlock(event.getTo().getBlock());
	}

	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		onPlayerMove((PlayerMoveEvent)event);
	}

}
