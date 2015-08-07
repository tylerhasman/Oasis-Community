package com.nirvana.oasis.community.ui;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.community.friends.Friend;
import com.nirvana.oasis.community.friends.FriendListData;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.menu.BasicPacketMenu;
import com.nirvana.oasis.core.menu.PacketMenu;
import com.nirvana.oasis.mc.Chat;
import com.nirvana.oasis.mc.IStack;

public class FriendListMenuBuilder {

	public static PacketMenu getFriendMenu(Player pl){
		
		PacketMenu menu = new BasicPacketMenu(9 * 6, Chat.GREEN+"Friend List");
		
		FriendListData data = new FriendListData(pl);
		
		data.load();
		
		int x = 1, y = 3;
		
		menu.addItem(2, 1, new IStack(Material.SKULL_ITEM)
		.setOwner(pl.getName())
		.setTitle(Chat.GREEN+Chat.BOLD+"Friends")
		.build(), null);
		
		for(Friend friend : data.getOnlineFriends()){
			ItemStack item = getItem(friend);
			
			menu.addItem(x, y, item, (ply, it, pm) -> {
				pm.close(ply);
				ply.sendMessage(Chat.GREEN+"Warping to "+friend.getServer());
				OasisCore.getNetworkUtilities().connect(ply, friend.getServer());
			});
			
			x++;
			if(x > 9){
				x = 1;
				y++;
			}
		}
		
		for(Friend friend : data.getOfflineFriends()){
			ItemStack item = getItem(friend);
			
			menu.addItem(x, y, item);
			
			x++;
			if(x > 9){
				x = 1;
				y++;
			}
		}
		
		return menu;
		
	}
	
	private static ItemStack getItem(Friend friend){
		IStack stack = new IStack(Material.SKULL_ITEM);
		stack.setOwner(friend.getName());
		
		if(friend.isOnline()){
			stack.setTitle(Chat.GREEN+friend.getName());
			stack.setLore(Chat.GREEN+ChatColor.BOLD+"Current Server", Chat.AQUA+friend.getServer(), Chat.YELLOW+"Click to Warp");
		}else{
			stack.setTitle(Chat.RED+friend.getName());
			stack.setLore(Chat.GRAY+"Offline");
		}
		
		return stack.build();
	}
	
}
