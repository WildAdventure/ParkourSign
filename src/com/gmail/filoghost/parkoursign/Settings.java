/*
 * Copyright (c) 2020, Wild Adventure
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 4. Redistribution of this software in source or binary forms shall be free
 *    of all charges or fees to the recipient of this software.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gmail.filoghost.parkoursign;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
	
	@Getter
	private static Block parkourCake, parkourSign;

	public static void init() {
		FileConfiguration config = ParkourSign.plugin.getConfig();
		parkourCake = getBlockFromConfigSection(config, "parkour.cake");
		parkourSign = getBlockFromConfigSection(config, "parkour.sign");
	}
	
	public static Block getBlockFromConfigSection(FileConfiguration config, String section) {
		if (config.isConfigurationSection(section)) {
			World world = Bukkit.getWorld(config.getString(section + ".world"));
			if (world != null) {
				return world.getBlockAt(config.getInt(section + ".x"), config.getInt(section + ".y"), config.getInt(section + ".z"));
			}
		}
		
		return null;
	}
	
	public static void setBlockToConfigSection(FileConfiguration config, String section, Block block) {
		config.set(section + ".world", block.getWorld().getName());
		config.set(section + ".x", block.getX());
		config.set(section + ".y", block.getY());
		config.set(section + ".z", block.getZ());
	}

	public static void setParkourSign(Block block) {
		parkourSign = block;
		setBlockToConfigSection(ParkourSign.plugin.getConfig(), "parkour.sign", block);
		ParkourSign.plugin.saveConfig();
	}
	
	public static void setParkourCake(Block block) {
		parkourCake = block;
		setBlockToConfigSection(ParkourSign.plugin.getConfig(), "parkour.cake", block);
		ParkourSign.plugin.saveConfig();
	}
	
	
}
