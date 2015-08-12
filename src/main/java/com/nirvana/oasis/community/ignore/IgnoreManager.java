package com.nirvana.oasis.community.ignore;

import org.bukkit.entity.Player;

public interface IgnoreManager {

	/**
	 * Check if player 1 is ignoring player 2
	 * @param pl1 player 1
	 * @param pl2 player 2
	 * @return true if player 1 is ignoring player 2
	 */
	public boolean isIgnoring(Player pl1, Player pl2);
	
	/**
	 * Ignore player 2 for player 1
	 * @param pl1 player 1
	 * @param pl2 player 2
	 */
	public void ignorePlayer(Player pl1, Player pl2);
	
	/**
	 * Un-ignore player 2 for player 1
	 * @param pl1 player 1
	 * @param pl2 player 2
	 */
	public void unignorePlayer(Player pl1, Player pl2);
	
}
