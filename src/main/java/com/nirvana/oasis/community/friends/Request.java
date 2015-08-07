package com.nirvana.oasis.community.friends;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;

public class Request {

	private UUID sender;
	private String name;
	private boolean isOnline;
	private String server;
	private boolean fulfilled;
	
	public Request(UUID sender) {
		this.sender = sender;
	}
	
	public void load(){
		name = OasisCore.getDatabaseManager().getName(sender);
		server = OasisCore.getJedisManager().getCurrentServer(sender);
		isOnline = server != null;
	}
	
	public UUID getSender() {
		return sender;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServer() {
		return server;
	}
	
	public boolean isOnline(){
		return isOnline;
	}

	public boolean isFulfilled(){
		return fulfilled;
	}
	
	public void accept(UUID receiver) {
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			OasisCommunity.getFriendManager().acceptRequest(sender, receiver);
		});
		
	}

	public void decline(UUID receiver) {
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			OasisCommunity.getFriendManager().declineRequest(sender, receiver);
		});
	}

	public void setFulfilled(boolean b) {
		fulfilled = b;
	}
	
}
