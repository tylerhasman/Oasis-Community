package com.nirvana.oasis.community.friends;

import java.util.List;
import java.util.UUID;

public interface FriendManager {
	
	/**
	 * Get a list of friends for a user
	 * @param id the users id
	 * @return the list of friends or null if something went wrong
	 */
	public List<UUID> getFriends(UUID id);
	
	/**
	 * Get a list of friend requests for a user
	 * @param id the users id
	 * @return the list of friend requests or null if something went wrong
	 */
	public List<UUID> getRequests(UUID id);
	
	/**
	 * Create a friend request between two people
	 * @param sender the request sender
	 * @param reciever the request receiver
	 * @return true if the request was created
	 */
	public boolean addRequest(UUID sender, UUID reciever);
	
}
