package com.nirvana.oasis.community.party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;

public class IPartyManager implements PartyManager {

	private List<Party> parties;
	
	public IPartyManager() {
		parties = new ArrayList<Party>();
	}
	
	@Override
	public List<Party> getParties() {
		return parties;
	}

	@Override
	public boolean loadPartyWithLeader(Player leader) {
		ResultSetList set;
		try {
			set = OasisCore.getDatabaseManager().query("SELECT * FROM `Party` WHERE `Leader`=?", leader.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		if(set.next()){
			String otherString = set.getValue("Members");
			
			String[] splits = otherString.split("/");
			
			Party party = new IParty(leader.getName(), splits);
			
			addParty(party);
			
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public void addParty(Party party) {
		parties.add(party);
	}

	@Override
	public void removeParty(Party party) {
		parties.remove(party);
	}

	@Override
	public Party getParty(String player) {
		for(Party party : parties){
			if(party.isPlayerInParty(player)){
				return party;
			}
		}
		return null;
	}

	@Override
	public boolean isInParty(String player) {
		return getParty(player) != null;
	}

}
