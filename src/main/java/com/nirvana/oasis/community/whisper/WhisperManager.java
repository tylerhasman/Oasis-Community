package com.nirvana.oasis.community.whisper;

import java.util.UUID;

import org.bukkit.entity.Player;

public interface WhisperManager {

	/**
	 * Send a whisper between two players
	 * @param from the sender
	 * @param to the receiver
	 * @param message the message
	 * @return true if the whisper was sent, false if the receiver was not found
	 */
	public boolean sendWhisper(Player from, String to, String message);
	
	/**
	 * Reply to the last message
	 * @param from the sender
	 * @param message the message
	 * @return true if the whisper was sent
	 */
	public boolean reply(Player from, String message);
	
}
