package com.nirvana.oasis.community.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;

public class RequestListData implements Iterable<Request>{

	private List<Request> requests;
	private UUID id;
	
	public RequestListData(Player pl) {
		this.id = pl.getUniqueId();
		requests = new ArrayList<>();
	}
	
	public void load(){
		List<UUID> list = OasisCommunity.getFriendManager().getRequests(id);
		
		for(UUID id : list){
			Request request = new Request(id);
			
			request.load();
			
			requests.add(request);
		}
		
	}
	
	public UUID getId() {
		return id;
	}

	@Override
	public Iterator<Request> iterator() {
		return requests.iterator();
	}
	
	public int size(){
		return requests.size();
	}
	
}
