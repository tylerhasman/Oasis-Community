package com.nirvana.oasis.community.player;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.nirvana.oasis.community.OasisCommunity;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinAsync(PlayerLoginEvent event){
		
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			OasisCommunity.getPartyManager().loadPartyWithLeader(event.getPlayer());
		});
		
	}
	
}
