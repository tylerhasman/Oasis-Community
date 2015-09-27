package com.nirvana.oasis.community;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.nirvana.oasis.community.commands.CommandFriend;
import com.nirvana.oasis.community.commands.CommandParty;
import com.nirvana.oasis.community.craftbook.CraftBook;
import com.nirvana.oasis.community.craftbook.ICraftBook;
import com.nirvana.oasis.community.friends.IFriendManager;
import com.nirvana.oasis.community.friends.FriendManager;
import com.nirvana.oasis.community.ignore.IgnoreManager;
import com.nirvana.oasis.community.party.IPartyManager;
import com.nirvana.oasis.community.party.PartyManager;
import com.nirvana.oasis.community.player.PlayerListener;
import com.nirvana.oasis.core.OasisCore;

public class OasisCommunity extends JavaPlugin {

	private FriendManager friendManager;
	private PartyManager partyManager;
	private CraftBook craftBook;
	private IgnoreManager ignoreManager;
	
	@Override
	public void onEnable() {
		friendManager = new IFriendManager();
		partyManager = new IPartyManager();
		craftBook = new ICraftBook();
		
		OasisCore.getCommandManager().registerCommand(new CommandFriend());
		OasisCore.getCommandManager().registerCommand(new CommandParty());
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
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

	public static IgnoreManager getIgnoreManager(){
		return getInstance().ignoreManager;
	}
	
	public static CraftBook getSocialMedia(){
		return getInstance().craftBook;
	}
	
}
