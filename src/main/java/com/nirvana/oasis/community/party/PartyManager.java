package com.nirvana.oasis.community.party;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public interface PartyManager {

	/**
	 * Get a list of all loaded parties
	 * @return a list of all loaded parties
	 */
	public List<Party> getParties();
	
	/**
	 * Load a party from the database
	 * @param leader the leader of the party
	 * @return true if the party was loaded
	 */
	public boolean loadPartyWithLeader(Player leader);
	
	/**
	 * Add a party to the cache
	 * @param party the party to add
	 */
	public void addParty(Party party);
	
	/**
	 * Remove a party from the cache
	 * @param party the party to remove
	 */
	public void removeParty(Party party);
	
	/**
	 * Get the party a player belongs to
	 * @param player the player (uuid)
	 * @return the party or null if they dont belong to one
	 */
	public Party getParty(String player);
	
	/**
	 * Check if a player is in a party
	 * @param player the player (uuid)
	 * @return true if they are in a party
	 */
	public boolean isInParty(String player);
	
	
	
}
