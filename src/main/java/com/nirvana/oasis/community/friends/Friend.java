package com.nirvana.oasis.community.friends;

import java.util.UUID;

import com.nirvana.oasis.core.OasisCore;

public class Friend {

	private UUID id;
	private String name;
	private boolean isOnline;
	private String server;
	private long lastSeen;
	
	public Friend(UUID id) {
		this.id = id;
	}
	
	public Friend(UUID sender, String name2) {
		id = sender;
		name = name2;
		
	}

	public void load(){
		if(name == null){
			name = OasisCore.getDatabaseManager().getName(id);
		}
		
		server = OasisCore.getNetworkUtilities().getServer(name);
		
		isOnline = server != null;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServer() {
		return server;
	}
	
	public UUID getId() {
		return id;
	}
	
	public boolean isOnline(){
		return isOnline;
	}
	
	public long getLastSeen() {
		return lastSeen;
	}
	
}
