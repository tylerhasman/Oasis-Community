package com.nirvana.oasis.community.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendList {

	private List<UUID> friends;
	private List<UUID> requests;
	
	public FriendList() {
		friends = new ArrayList<>();
		requests = new ArrayList<>();
	}
	
	public List<UUID> getFriends() {
		return friends;
	}
	
	public void addFriend(UUID id){
		friends.add(id);
	}
	
	public void addFriends(List<UUID> friendsToAdd){
		friends.addAll(friendsToAdd);
	}
	
	public List<UUID> getRequests() {
		return requests;
	}
	
	public void addRequest(UUID id){
		requests.add(id);
	}
	
	public void addRequests(List<UUID> requestsToAdd){
		requests.addAll(requestsToAdd);
	}
	
}
