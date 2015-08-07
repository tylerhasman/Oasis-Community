package com.nirvana.oasis.community.commands;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.friends.FriendList;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.commands.OasisCommand;
import com.nirvana.oasis.core.menu.BasicPacketMenu;
import com.nirvana.oasis.core.menu.PacketMenu;
import com.nirvana.oasis.mc.Chat;
import com.nirvana.oasis.mc.IStack;

public class CommandFriend implements OasisCommand {

	@Override
	public String getName() {
		return "friends";
	}

	@Override
	public String getPermission() {
		return "oasis.community.friends";
	}

	@Override
	public String getDescription() {
		return "Open a friends list";
	}

	@Override
	public boolean execute(Player pl, String[] args) {		
		PacketMenu menu = new BasicPacketMenu(9 * 8, Chat.YELLOW+Chat.BOLD+"Friends");
		
		List<UUID> friends = OasisCommunity.getFriendManager().getFriends(pl.getUniqueId());
		
		int x = 1, y = 2, offlineX = 1, offlineY = 5;
		
		for(int i = 0; i < friends.size();i++){
			
			UUID id = friends.get(i);
			String name = OasisCore.getDatabaseManager().getName(id);
			String server = OasisCore.getJedisManager().getCurrentServer(name);
			
			IStack stack = new IStack(Material.SKULL_ITEM);
			stack.setOwner(name);
			
			boolean isOnline = isPlayerOnline(name);
			
			if(isOnline){
				stack.setTitle(Chat.RED+name);
				stack.setLore(Chat.GREEN+ChatColor.BOLD+"Current Server", Chat.AQUA+server, Chat.YELLOW+"Click to Warp");
			}else{
				stack.setTitle(Chat.GREEN+Chat.BOLD+name);
			}
			
			if(isOnline){
				menu.addItem(x, y, stack.build(), (ply, item, pm) -> {
					pm.close(ply);
					ply.sendMessage(Chat.GREEN+"Warping to "+server);
					OasisCore.getNetworkUtilities().connect(ply, server);
				});
				
				x++;
				
				if(x > 9){
					x = 1;
					y++;
				}
			}else{
				menu.addItem(offlineX, offlineY, stack.build());
				
				offlineX++;
				
				if(offlineX > 9){
					offlineX = 1;
					offlineY++;
				}
			}
			

		}
		
		
		return true;
	}
	
	private boolean isPlayerOnline(String player){
		return OasisCore.getJedisManager().getCurrentServer(player) != null;
	}

	@Override
	public String getUsage() {
		return "/friends";
	}

}
