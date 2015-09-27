package com.nirvana.oasis.community.craftbook;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface Follow {

	/**
	 * Get the players uuid
	 * @return the players uuid
	 */
	public UUID getPlayer();
	
	/**
	 * Get the players name
	 * @return the players name
	 */
	public String getPlayerName();
	
	/**
	 * Get if the player is following back
	 * @return true if the player is following back
	 */
	public boolean isFollowingBack();
	
	/**
	 * Get the person who owns this follow
	 * @return the follows owner
	 */
	public Player getOwner();
	
}
