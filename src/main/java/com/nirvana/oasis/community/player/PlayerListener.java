package com.nirvana.oasis.community.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.craftbook.PlayerProfile;
import com.nirvana.oasis.community.party.Party;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.event.PlayerServerConnectEvent;
import com.nirvana.oasis.mc.Chat;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoinAsync(PlayerLoginEvent event){
		
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			OasisCommunity.getPartyManager().loadPartyWithLeader(event.getPlayer());
			PlayerProfile profile = OasisCommunity.getSocialMedia().getProfile(event.getPlayer().getUniqueId());
			if(profile.createIfNotExists()){
				OasisCore.getInstance().getLogger().info("Created player profile for "+event.getPlayer().getName());
			}
			profile.addToFeed(Chat.YELLOW+"Joined "+OasisCore.getNetworkUtilities().getBungeeId(), new ItemStack(Material.REDSTONE, 1), event.getPlayer().getName() + " joined "+OasisCore.getNetworkUtilities().getBungeeId());
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
				OasisCommunity.getPartyManager().removeParty(party);
			}else{
				pl.sendMessage(Chat.RED+"Only the party leader may switch servers!");
			}
			event.setCancelled(true);
		}
		
	}
	
}
