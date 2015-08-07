package com.nirvana.oasis.community.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;

public class FriendListData implements Iterable<Friend>{

	private UUID owner;
	private List<Friend> friends;
	
	public FriendListData(Player owner)
	{
		this.owner = owner.getUniqueId();
		friends = new ArrayList<>();
	}
	
	/**
	 * Load the friends data from the redis and database
	 */
	public void load(){
		
		List<UUID> ids = OasisCommunity.getFriendManager().getFriends(owner);
		
		for(UUID id : ids){
			
			Friend friend = new Friend(id);
			friend.load();
			friends.add(friend);
			
		}
		
	}
	
	/**
	 * 
	 * @return a list of offline friends, sorted alphabetically
	 */
	public List<Friend> getOfflineFriends(){
		return friends.stream().sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).filter(f -> !f.isOnline()).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @return a list of online friends, sorted alphabetically
	 */
	public List<Friend> getOnlineFriends(){
		return friends.stream().sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).filter(f -> f.isOnline()).collect(Collectors.toList());
	}

	@Override
	public Iterator<Friend> iterator() {
		return friends.iterator();
	}
	
	public int size(){
		return friends.size();
	}

	public Friend get(int i) {
		return friends.get(i);
	}
	
}
