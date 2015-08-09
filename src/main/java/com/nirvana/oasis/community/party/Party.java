package com.nirvana.oasis.community.party;

import java.util.Set;

import org.bukkit.Location;

public interface Party {

	/**
	 * Get an array of all members in the party, including the leader
	 * @return a set of the party members
	 */
	public Set<String> getMembers();
	
	/**
	 * Get the party leaders name
	 * @return the party leader
	 */
	public String getLeader();
	
	/**
	 * Add a player to the party
	 * @param name the players name
	 */
	public void addPlayer(String name);
	
	/**
	 * Teleport the entire party to a location
	 * @param location the location
	 */
	public void teleportParty(Location location);
	
	/**
	 * Connect all party members to a server
	 * @param server the server
	 */
	public void connectParty(String server);
	
	/**
	 * Send a message to all players in the party
	 * @param message the players
	 */
	public void sendPartyMessage(String message);
	
	/**
	 * Get the maximum capacity
	 * @return the parties capacity
	 */
	public int getCapacity();
	
	/**
	 * Destroy the party
	 */
	public void disband();

	/**
	 * Check to see if a player is in this party
	 * @param player the player
	 * @return true if they are in the party
	 */
	public boolean isPlayerInParty(String player);
	
	/**
	 * Invite a player to the party
	 * @param player the invited person
	 */
	public void addInvite(String player);
	
	/**
	 * 
	 * @param player the player name
	 * @return true if they have been invited to the party
	 */
	public boolean isInvited(String player);

	/**
	 * Remove a player from the party
	 * @param name the player
	 */
	public void removePlayer(String name);
	
	/**
	 * 
	 * @param name the players name
	 * @return true if they are the party leader
	 */
	public boolean isLeader(String name);
	
}
