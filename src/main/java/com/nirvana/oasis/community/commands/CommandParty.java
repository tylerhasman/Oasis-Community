package com.nirvana.oasis.community.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.party.Party;
import com.nirvana.oasis.community.party.IParty;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.commands.CommandResult;
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
	public CommandResult execute(Player pl, String[] args) {
		if(args.length == 0)
		{
			return CommandResult.BAD_USAGE;
		}
		
		String cmd = args[0];
		
		if(cmd.equalsIgnoreCase("create")){
			if(OasisCommunity.getPartyManager().isInParty(pl.getName())){
				pl.sendMessage(Chat.RED+"You are already in a party!");
			}else{
				
				IParty party = new IParty(pl.getName());
				
				party.insertIntoDatabase();
				
				OasisCommunity.getPartyManager().addParty(party);
				
				pl.sendMessage(Chat.GREEN+"Your party has been created! Invite players using '/party invite [player name]'");
			}
		}else if(cmd.equalsIgnoreCase("join")){
			
			if(args.length < 2){
				return CommandResult.BAD_USAGE;
			}
			
			String target = args[1];
			
			Party party = OasisCommunity.getPartyManager().getParty(target);
			
			if(party == null){
				pl.sendMessage(Chat.RED+target+" is not in a party!");
			}
			
			if(party.isInvited(pl.getName()))
			{
				if(party.getMembers().size() >= party.getCapacity()){
					pl.sendMessage(Chat.RED+"That party is full!");
				}else{
					party.addPlayer(pl.getName());
					party.sendPartyMessage(Chat.GREEN+pl.getName()+" has joined the party!");
				}
			}else{
				pl.sendMessage(Chat.RED+"You are not invited to "+target+"'s party!");
			}
			
		}else if(cmd.equalsIgnoreCase("invite")){
			if(args.length < 2){
				return CommandResult.BAD_USAGE;
			}
			
			String target = args[1];
			
			Player invited = Bukkit.getPlayer(target);
			
			if(invited == null){
				OasisCore.getLocaleManager().sendPlayerNotFound(pl, target);
				return CommandResult.SUCCESS;
			}
			
			Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
			
			if(party == null){
				pl.sendMessage(Chat.RED+"You're not in a party!");
			}else if(!party.isLeader(pl.getName())){
				pl.sendMessage(Chat.RED+"You're not the leader of your party!");
			}else if(party.isInvited(target)){
				pl.sendMessage(Chat.RED+target+" is already invited to your party!");
			}else{
				if(party.getMembers().size() >= party.getCapacity()){
					pl.sendMessage(Chat.RED+"Your party is at maximum capacity!");
				}else{
					party.addInvite(target);	
					OasisCore.getNetworkUtilities().sendMessage(target, Chat.GREEN+"You have been invited to "+pl.getName()+"'s party! Do '/party join "+pl.getName()+"' to join!");
					pl.sendMessage(Chat.GREEN+"Invite sent!");
				}
			}
		}else if(cmd.equalsIgnoreCase("disband")){
			Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
			
			if(party != null){
				if(party.isLeader(pl.getName())){
					party.disband();
				}else{
					pl.sendMessage(Chat.RED+"Only the party leader may disband the party!");
				}
			}else{
				pl.sendMessage(Chat.RED+"You're not in a party!");
			}
		}else if(cmd.equalsIgnoreCase("leave")){
			Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
			
			if(party != null){
				if(party.isLeader(pl.getName())){
					party.disband();
				}else{
					party.removePlayer(pl.getName());
					party.sendPartyMessage(Chat.RED+pl.getName()+" has quit the party!");
				}
			}else{
				pl.sendMessage(Chat.RED+"You're not in a party!");
			}
		}else if(cmd.equalsIgnoreCase("list")){
			Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
			
			if(party != null){
				pl.sendMessage(Chat.MESSAGE_LINE);
				pl.sendMessage(Chat.GREEN+Chat.centerText("<Leader>"));
				pl.sendMessage(Chat.GREEN+Chat.centerText(party.getLeader()));
				
				if(party.getMembers().size() > 0){
					pl.sendMessage("");
					pl.sendMessage(Chat.GREEN+Chat.centerText("<Member>"));
				}
				
				for(String member : party.getMembers()){
					if(party.isLeader(member)){
						continue;
					}
					
					pl.sendMessage(Chat.GREEN+Chat.centerText(member));
				}
				pl.sendMessage(Chat.MESSAGE_LINE);
			}else{
				pl.sendMessage(Chat.RED+"You're not in a party!");
			}
		}else if(cmd.equalsIgnoreCase("warp")){
			Party party = OasisCommunity.getPartyManager().getParty(pl.getName());
			
			if(party != null){
				if(party.isLeader(pl.getName())){
					
					party.sendPartyMessage(Chat.GREEN+"The party has been warped to "+Chat.GOLD+OasisCore.getNetworkUtilities().getBungeeId());
					party.connectParty(OasisCore.getNetworkUtilities().getBungeeId());
					
				}else{
					pl.sendMessage(Chat.RED+"Only the party leader may warp!");
				}
			}else{
				pl.sendMessage(Chat.RED+"You're not in a party!");
			}
		}else{
			
			String[] messages = new String[]{
				"/party create - Create a new party",
				"/party join [name] - Join someones party",
				"/party invite [name] - Invite someone to your party",
				"/party disband - Destroy the your party",
				"/party leave - Leave your party, if your the leader this disbands it",
				"/party list - List all members in your party",
				"/party warp - Warp all members of the party to your server"
			};
			
			pl.sendMessage(messages);
			
			return CommandResult.SUCCESS;
		}
		
		return CommandResult.SUCCESS;
	}

	@Override
	public String getUsage() {
		return "/"+getName()+" [ create | join | invite | disband | list | leave | warp ] <player name>";
	}

}
