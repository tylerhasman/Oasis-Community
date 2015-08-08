package com.nirvana.oasis.community;

import org.bukkit.plugin.java.JavaPlugin;

import com.nirvana.oasis.community.commands.CommandFriend;
import com.nirvana.oasis.community.commands.CommandParty;
import com.nirvana.oasis.community.friends.BasicFriendManager;
import com.nirvana.oasis.community.friends.FriendManager;
import com.nirvana.oasis.community.party.BasicPartyManager;
import com.nirvana.oasis.community.party.PartyManager;
import com.nirvana.oasis.core.OasisCore;

public class OasisCommunity extends JavaPlugin {

	private FriendManager friendManager;
	private PartyManager partyManager;
	
	@Override
	public void onEnable() {
		friendManager = new BasicFriendManager();
		partyManager = new BasicPartyManager();
		
		OasisCore.getCommandManager().registerCommand(new CommandFriend());
		OasisCore.getCommandManager().registerCommand(new CommandParty());
	}
	
	public static OasisCommunity getInstance(){
		return getPlugin(OasisCommunity.class);
	}
	
	public static FriendManager getFriendManager(){
		return getInstance().friendManager;
	}
	
	public static PartyManager getPartyManager(){
		return getInstance().partyManager;
	}
	
}
