package com.nirvana.oasis.community.party;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;



import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;


import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.settings.Constants;
import com.nirvana.oasis.mc.Chat;

public class SimpleParty implements Party {
	
	private List<String> members;
	private String leader;
	private List<String> invites;
	
	public SimpleParty(Player leader, String... others) {
		members = new ArrayList<String>(Constants.MAX_PARTY_SIZE);
		invites = new ArrayList<String>();
		
		addPlayer(leader.getName());
		
		for(String other : others){
			addPlayer(other);
		}
	}
	
	@Override
	public Set<String> getMembers() {
		return members.stream().collect(Collectors.toSet());
	}

	@Override
	public String getLeader() {
		return leader;
	}

	@Override
	public void teleportParty(Location location) {
		members.stream().forEach(member -> {
			
			Player pl = Bukkit.getPlayer(member);
			
			if(pl != null){
				pl.teleport(location);
			}
			
		});
	}

	@Override
	public void connectParty(String server) {
		members.stream().forEach(member -> OasisCore.getNetworkUtilities().connect(member, server));
	}

	@Override
	public void sendPartyMessage(String message) {
		members.stream().forEach(member -> OasisCore.getNetworkUtilities().sendMessage(member, message));
	}
	@Override
	public void addPlayer(String name) {
		members.add(name);
	}

	@Override
	public int getCapacity() {
		return Constants.MAX_PARTY_SIZE;
	}
	
	public void insertIntoDatabase(){
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			try {
				OasisCore.getDatabaseManager().execute("INSERT INTO `Party` (`Leader`, `Members`) VALUES (?, ?)", leader.toString(), getMemberString());
			} catch (Exception e) {
				e.printStackTrace();
				sendPartyMessage(Chat.DARK_RED+"An internal error occured when attempting to create the party!");
				disband();
			}
		});
	}
	
	private String getMemberString(){
		String memberString = "";
		
		for(String member : members){
			memberString += member + "/";
		}
		
		return memberString;
	}

	@Override
	public void disband() {
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			try {
				OasisCore.getDatabaseManager().execute("DELETE FROM `Party` WHERE `Leader`=?", leader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		OasisCommunity.getPartyManager().removeParty(this);
		sendPartyMessage(Chat.RED+"The party has disbanded!");
		members.clear();
		leader = "";
	}

	@Override
	public boolean isPlayerInParty(String player) {
		
		for(String member : members){
			if(member.equalsIgnoreCase(player)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void addInvite(String player) {
		invites.add(player);
	}

	@Override
	public boolean isInvited(String player) {
		return invites.contains(player);
	}

	@Override
	public void removePlayer(String name) {
		members.remove(name);
	}

}
