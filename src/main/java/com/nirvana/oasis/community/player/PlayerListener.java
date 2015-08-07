package com.nirvana.oasis.community.player;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.nirvana.oasis.community.OasisCommunity;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerLoginAsync(AsyncPlayerPreLoginEvent event){
		
		if(event.getLoginResult() == Result.ALLOWED){
			UUID id = event.getUniqueId();
			
			OasisCommunity.getFriendManager().loadPlayer(id);
			
		}
		
	}
	
}
