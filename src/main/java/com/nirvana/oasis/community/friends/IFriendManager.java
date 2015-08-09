package com.nirvana.oasis.community.friends;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;

public class IFriendManager implements FriendManager {
	
	public List<UUID> getFriends(UUID id) {
		
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
			int result = OasisCore.getDatabaseManager().execute("INSERT INTO `Friends` (`Player 1`, `Player 2`) VALUES (?, ?)", reciever.toString(), sender.toString());

			return result > 0;//If we didnt change a line that must mean something happened incorrectly!
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public boolean acceptRequest(UUID sender, UUID receiver) {
		try {
			OasisCore.getDatabaseManager().execute("UPDATE `Friends` SET `Valid`='1' WHERE `Player 1`=? AND `Player 2`=?", receiver.toString(), sender.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean declineRequest(UUID sender, UUID receiver) {
		try {
			OasisCore.getDatabaseManager().execute("DELETE FROM `Friends` WHERE `Player 1`=? AND `Player 2`=?", receiver.toString(), sender.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void deleteFriend(UUID friend, UUID deleter) {
		try {
			OasisCore.getDatabaseManager().execute("DELETE FROM `Friends` WHERE `Player 1`=? AND `Player 2`=?", deleter.toString(), friend.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
