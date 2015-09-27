package com.nirvana.oasis.community.craftbook;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public interface CraftBook {

	/**
	 * Get a list of players following this player
	 * @param player the player
	 * @return a list of players following the player
	 */
	public List<Follow> getFollowers(UUID uuid);
	
	/**
	 * Get a list of players this player is following
	 * @param player the player
	 * @return the list of players this player is following
	 */
	public List<Follow> getFollows(UUID uuid);
	
	/**
	 * Follow a user
	 * @param player the player
	 * @param target the target to follow
	 * @param targetUuid the targets uuid
	 */
	public void follow(Player player, String target, UUID targetUuid);
	
	/**
	 * Check if a player is following another player
	 * @param player the player
	 * @param target the target to check
	 * @return true if player is following target
	 */
	public boolean isFollowing(UUID player, UUID target);
	
	/**
	 * Check if two players are following each other
	 * @param player1 player 1 uuid
	 * @param player2 player 2 uuid
	 * @return true if they are both following each other
	 */
	public boolean isFollowMutal(UUID player1, UUID player2);
	
	/**
	 * Get a players profile
	 * @param player the players uuid
	 * @return the players profile
	 */
	public PlayerProfile getProfile(UUID player);
}
