package com.nirvana.oasis.community.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;

public class BasicPartyManager implements PartyManager {

	private List<Party> parties;
	
	public BasicPartyManager() {
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
			
			Party party = new SimpleParty(leader, splits);
			
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
