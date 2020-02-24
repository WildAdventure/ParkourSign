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

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import wild.api.command.CommandFramework;
import wild.api.command.CommandFramework.Permission;

@Permission("parkoursign.use")
public class CommandHandler extends CommandFramework {

	public CommandHandler(JavaPlugin plugin, String label) {
		super(plugin, label);
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(ChatColor.YELLOW + "/" + label + " setCake");
			sender.sendMessage(ChatColor.YELLOW + "/" + label + " setSign");
			return;
		}
		
		if (args[0].equalsIgnoreCase("setsign")) {
			
			Block block = CommandValidate.getPlayerSender(sender).getTargetBlock((Set<Material>) null, 64);
			CommandValidate.isTrue(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST, "Non stai guardando un cartello.");
			
			Settings.setParkourSign(block);
			sender.sendMessage(ChatColor.GREEN + "Impostato il cartello parkour.");
			return;
		}
		
		if (args[0].equalsIgnoreCase("setcake")) {
			Block block = CommandValidate.getPlayerSender(sender).getTargetBlock((Set<Material>) null, 64);
			CommandValidate.isTrue(block.getType() == Material.CAKE_BLOCK, "Non stai guardando una torta.");
			Settings.setParkourCake(block);
			sender.sendMessage(ChatColor.GREEN + "Impostata la torta parkour.");
			return;
		}

		sender.sendMessage(ChatColor.RED + "Sub-comando non trovato. /" + label + " per la lista dei comandi.");
	}

}
