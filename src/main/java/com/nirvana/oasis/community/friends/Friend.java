package com.nirvana.oasis.community.friends;

import java.util.UUID;

import com.nirvana.oasis.core.OasisCore;

public class Friend {

	private UUID id;
	private String name;
	private boolean isOnline;
	private String server;
	
	public Friend(UUID id) {
		this.id = id;
	}
	
	public void load(){
		name = OasisCore.getDatabaseManager().getName(id);
		server = OasisCore.getJedisManager().getCurrentServer(id);
		
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
	
}
