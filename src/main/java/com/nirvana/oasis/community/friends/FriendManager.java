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

	/**
	 * Accept a friend request
	 * @param sender the sender
	 * @param receiver the receiver
	 * @return true if accepting the request was successful
	 */
	public boolean acceptRequest(UUID sender, UUID receiver);

	/**
	 * Decline a friend request
	 * @param sender thesender
	 * @param receiver the receiver
	 * @return true if declining the request was successful
	 */
	public boolean declineRequest(UUID sender, UUID receiver);

	/**
	 * Delete a friend from a friends list
	 * @param friend the friend
	 * @param deleter the person who wants the friend deleted ;(
	 */
	public void deleteFriend(UUID friend, UUID deleter);
	
}
