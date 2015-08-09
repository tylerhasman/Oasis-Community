package com.nirvana.oasis.community;

import org.bukkit.plugin.java.JavaPlugin;

import com.nirvana.oasis.community.commands.CommandFriend;
import com.nirvana.oasis.community.commands.CommandParty;
import com.nirvana.oasis.community.commands.CommandReply;
import com.nirvana.oasis.community.commands.CommandWhisper;
import com.nirvana.oasis.community.friends.IFriendManager;
import com.nirvana.oasis.community.friends.FriendManager;
import com.nirvana.oasis.community.party.IPartyManager;
import com.nirvana.oasis.community.party.PartyManager;
import com.nirvana.oasis.community.whisper.IWhisperManager;
import com.nirvana.oasis.community.whisper.WhisperManager;
import com.nirvana.oasis.core.OasisCore;

public class OasisCommunity extends JavaPlugin {

	private FriendManager friendManager;
	private PartyManager partyManager;
	private WhisperManager whisperManager;
	
	@Override
	public void onEnable() {
		friendManager = new IFriendManager();
		partyManager = new IPartyManager();
		whisperManager = new IWhisperManager();
		
		OasisCore.getCommandManager().registerCommand(new CommandFriend());
		OasisCore.getCommandManager().registerCommand(new CommandParty());
		OasisCore.getCommandManager().registerCommand(new CommandWhisper());
		OasisCore.getCommandManager().registerCommand(new CommandReply());
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

	public static WhisperManager getWhisperManager() {
		return getInstance().whisperManager;
	}
	
}
