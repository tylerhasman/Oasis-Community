package com.nirvana.oasis.community.craftbook;

import java.util.UUID;

public interface CraftBook {

	
	/**
	 * Check if two players are following each other
	 * @param player1 player 1 uuid
	 * @param player2 player 2 uuid
	 * @return true if they are both following each other
	 */
	public boolean isFollowMutual(UUID player1, UUID player2);
	
	/**
	 * Check if two players are following each other
	 * @param profile1 player 1 profile
	 * @param profile2 player 2 profile
	 * @return true if they are both following each other
	 */
	public boolean isFollowMutual(PlayerProfile profile1, PlayerProfile profile2);
	
	/**
	 * Get a players profile and will create the profile if it does not exist in the database
	 * @param player the players uuid
	 * @return the players profile
	 * @see #getProfile(UUID, boolean)
	 */
	public PlayerProfile getProfile(UUID player);
	
	/**
	 * Get a players profile and optionally create the profile if it does not exist in the database
	 * @param player the players uuid
	 * @param createIfNotExists if true the profile will be created if it does not exist
	 * @return the players profile
	 */
	public PlayerProfile getProfile(UUID player, boolean createIfNotExists);
	
	public static final class Settings{
		
		/**
		 * Max amount of follows is {@value #MAX_FOLLOWS}
		 */
		public static final int MAX_FOLLOWS = 27;//This is the max amount we can display in the inventory at the moment
		
	}
}
