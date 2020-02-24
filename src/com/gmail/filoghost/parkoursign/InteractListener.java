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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import wild.api.sound.SoundEnum;
import wild.api.world.RayTrace;

import com.google.common.collect.Sets;

public class InteractListener implements Listener {
	
	private Collection<String> parkourWinnerCooldown = Sets.newHashSet();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CAKE_BLOCK) {
			
			if (Settings.getParkourCake() != null && event.getClickedBlock().equals(Settings.getParkourCake())) {
					
				event.setCancelled(true);
				
				Location sight = RayTrace.getSight(event.getPlayer());
				if (sight.getBlock().getType() != Material.CAKE_BLOCK) {
					event.getPlayer().sendMessage(ChatColor.RED + "Devi guardare la torta, senza altri blocchi in mezzo.");
					return;
				}
				
				if (parkourWinnerCooldown.contains(event.getPlayer().getName())) {
					event.getPlayer().sendMessage(ChatColor.YELLOW + "Hai già cliccato, attendi.");
					return;
				}

				if (Settings.getParkourSign() != null) {

					final String winnerName = event.getPlayer().getName();

					parkourWinnerCooldown.add(winnerName);
					event.getClickedBlock().setType(Material.AIR);
					event.getPlayer().playSound(event.getPlayer().getLocation(), SoundEnum.get("ENTITY_PLAYER_BURP"), 1F, 1F);
					event.getPlayer().sendMessage(ChatColor.GREEN + "Il tuo nome è stato salvato come ultimo vincitore del parkour.");
					setSign(Settings.getParkourSign(), "Vincitore", "parkour:", winnerName, ChatColor.DARK_GRAY + "Il " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

					final Location loc = event.getClickedBlock().getLocation();

					Bukkit.getScheduler().scheduleSyncDelayedTask(ParkourSign.plugin, new Runnable() {
						
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							Block block = loc.getBlock();
							block.setType(Material.CAKE_BLOCK);
							block.setData((byte) 0);
							parkourWinnerCooldown.remove(winnerName);
						}
					}, 200L);

				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "Il tuo nome non è stato salvato, perché non è stato impostato un cartello.");
				}
			}
		}
	}
	
	public static void setSign(Block block, String first, String second, String third, String fourth) {
		if (block == null) {
			return;
		}
		
		BlockState state = block.getState();
		if (state instanceof Sign) {
			Sign sign = (Sign) state;
			sign.setLine(0, first);
			sign.setLine(1, second);
			sign.setLine(2, third);
			sign.setLine(3, fourth);
			state.update();
		}
	}

}
