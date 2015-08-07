package com.nirvana.oasis.community;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.nirvana.oasis.community.commands.CommandFriend;
import com.nirvana.oasis.community.friends.BasicFriendManager;
import com.nirvana.oasis.community.friends.FriendManager;
import com.nirvana.oasis.community.player.PlayerListener;
import com.nirvana.oasis.core.OasisCore;

public class OasisCommunity extends JavaPlugin {

	private FriendManager friendManager;
	
	@Override
	public void onEnable() {
		friendManager = new BasicFriendManager();
		
		OasisCore.getCommandManager().registerCommand(new CommandFriend());
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
	
	public static OasisCommunity getInstance(){
		return getPlugin(OasisCommunity.class);
	}
	
	public static FriendManager getFriendManager(){
		return getInstance().friendManager;
	}
	
}
