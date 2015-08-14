package com.nirvana.oasis.community.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.party.Party;
import com.nirvana.oasis.core.event.PlayerServerConnectEvent;
import com.nirvana.oasis.mc.Chat;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinAsync(PlayerLoginEvent event){
		
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			OasisCommunity.getPartyManager().loadPartyWithLeader(event.getPlayer());
		});
		
	}
	
	@EventHandler
	public void onPlayerConnectServer(PlayerServerConnectEvent event){
		Player pl = event.getPlayer();
		
		Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
		
		if(party != null){
			if(party.isLeader(pl.getName())){
				party.connectParty(event.getServer());
				party.sendPartyMessage(Chat.YELLOW+"The party warps to "+Chat.AQUA+event.getServer());
			}else{
				pl.sendMessage(Chat.RED+"Only the party leader may switch servers!");
			}
			event.setCancelled(true);
		}
		
	}
	
}
