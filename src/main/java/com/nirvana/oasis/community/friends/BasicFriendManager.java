package com.nirvana.oasis.community.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;

public class BasicFriendManager implements FriendManager {
	
	private Map<UUID, FriendList> friendLists;
	
	public BasicFriendManager() {
		friendLists = new HashMap<>();
	}
	
	public List<UUID> getFriends(UUID id) {
		
		if(friendLists.containsKey(id))
			return friendLists.get(id).getFriends();
		
		List<UUID> friends = new ArrayList<UUID>();
		
		try {
			ResultSetList data = OasisCore.getDatabaseManager().query("SELECT `Player 2` FROM `Friends` WHERE `Player 1`=? AND `Valid`='1'", id.toString());
		
			while(data.next()){
				friends.add(UUID.fromString((String) data.getValue("Player 2")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return friends;
	}

	public List<UUID> getRequests(UUID id) {
		
		if(friendLists.containsKey(id))
			return friendLists.get(id).getRequests();
		
		List<UUID> requests = new ArrayList<UUID>();
		
		try {
			ResultSetList data = OasisCore.getDatabaseManager().query("SELECT `Player 2` FROM `Friends` WHERE `Player 1`=? AND `Valid`='0'", id.toString());
		
			while(data.next()){
				requests.add(UUID.fromString((String) data.getValue("Player 2")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return requests;
	}

	public boolean addRequest(UUID sender, UUID reciever) {
		
		try {
			int result = OasisCore.getDatabaseManager().execute("INSERT INTO `Friends` (`Player 1`, `Player 2`) VALUES (?, ?)", sender.toString(), reciever.toString());
			friendLists.get(reciever).addRequest(sender);
			return result > 0;//If we didnt change a line that must mean something happened incorrectly!
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public void loadPlayer(UUID id) {
		
		FriendList list = new FriendList();
		
		list.addFriends(getFriends(id));
		list.addRequests(getRequests(id));
		
		friendLists.put(id, list);
	}

}
