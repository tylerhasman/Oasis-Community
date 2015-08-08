package com.nirvana.oasis.community.commands;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.party.SimpleParty;
import com.nirvana.oasis.core.commands.OasisCommand;
import com.nirvana.oasis.mc.Chat;

public class CommandParty implements OasisCommand {

	@Override
	public String getName() {
		return "party";
	}

	@Override
	public String getPermission() {
		return "oasis.community.party";
	}

	@Override
	public String getDescription() {
		return "Create or join a party";
	}

	@Override
	public boolean execute(Player pl, String[] args) {
		if(args.length == 0)
		{
			return false;
		}
		
		String cmd = args[0];
		
		if(cmd.equalsIgnoreCase("create")){
			if(OasisCommunity.getPartyManager().isInParty(pl.getName())){
				pl.sendMessage(Chat.RED+"You are already in a party!");
			}else{
				
				SimpleParty party = new SimpleParty(pl);
				
				party.insertIntoDatabase();
				
				OasisCommunity.getPartyManager().addParty(party);
				
				pl.sendMessage(Chat.GREEN+"Your party has been created! Invite players using '/party invite [player name]'");
			}
		}else if(cmd.equalsIgnoreCase("join")){
			
			if(args.length < 2){
				return false;
			}
			
			String leaderName = args[1];
			
			
			
		}else{
			return false;
		}
		
		return true;
	}

	@Override
	public String getUsage() {
		return "/"+getName()+" [ create | join | invite ] <leader>";
	}

}
