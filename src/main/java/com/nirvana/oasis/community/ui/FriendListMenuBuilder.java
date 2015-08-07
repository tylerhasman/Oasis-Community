package com.nirvana.oasis.community.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.friends.Friend;
import com.nirvana.oasis.community.friends.FriendListData;
import com.nirvana.oasis.community.friends.Request;
import com.nirvana.oasis.community.friends.RequestListData;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.menu.AnvilPacketMenu;
import com.nirvana.oasis.core.menu.BasicPacketMenu;
import com.nirvana.oasis.core.menu.PacketMenu;
import com.nirvana.oasis.core.menu.PacketMenuSlotHandler;
import com.nirvana.oasis.mc.Chat;
import com.nirvana.oasis.mc.Item;

public class FriendListMenuBuilder {
	
	/**
	 * Open a friends menu. To save time we cache the friend list and request list between menus opening.
	 * This means a player will have to completely close the friends list to request an update from the database
	 * @param pl the player to customize the menu for
	 * @param data leave null to retrieve data from database
	 * @param requests leave null to retrieve data from database
	 * @return the packet menu
	 */
	public static PacketMenu getFriendMenu(Player pl, FriendListData data, RequestListData requests){
		
		PacketMenu menu = new BasicPacketMenu(9 * 6, Chat.GREEN+"Friend List", pl);
		
		if(data == null){
			data = new FriendListData(pl);
			data.load();
		}
	
		if(requests == null){
			requests = new RequestListData(pl);
			requests.load();
		}
		
		final FriendListData finalData = data;
		final RequestListData finalRequests = requests;
		
		int x = 1, y = 3;
		
		menu.addItem(2, 1, getFriendOnlineItem(pl, data.getOnlineFriends().size()));
		
		menu.addItem(4, 1, getRequestItem(requests.getPendingRequests()), (ply, it, pm, ct, slot) ->{
			getRequestMenu(ply, finalData, finalRequests).open(ply);
		});
		
		menu.addItem(6, 1, getAddFriendItem(), getHandlerForAddFriend(finalData));
		
		menu.addItem(8, 1, new Item(Material.BARRIER)
		.setTitle(Chat.GREEN+Chat.BOLD+"Remove Friends")
		.setLore("", Chat.GRAY+"Click to open.")
		.build());
		
		
		for(Friend friend : data.getOnlineFriends()){
			ItemStack item = getItem(friend);
			
			menu.addItem(x, y, item, (ply, it, pm, ct, slot) -> {
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

	/**
	 * Open a friend request menu. To save time we cache the friend list and request list between menus opening.
	 * This means a player will have to completely close the friends list to request an update from the database
	 * @param pl the player to customize the menu for
	 * @param data leave null to retrieve data from database
	 * @param requests leave null to retrieve data from database
	 * @return the packet menu
	 */
	public static PacketMenu getRequestMenu(Player pl, FriendListData data, RequestListData requests){
		PacketMenu menu = new BasicPacketMenu(9 * 6, Chat.GREEN+"Friend List", pl);
		
		if(data == null){
			data = new FriendListData(pl);
			data.load();
		}
	
		if(requests == null){
			requests = new RequestListData(pl);
			requests.load();
		}
		
		final FriendListData finalData = data;
		final RequestListData finalRequests = requests;
		
		int x = 1, y = 3;
		
		menu.addItem(2, 1, getFriendOnlineItem(pl, finalData.getOnlineFriends().size()), (ply, it, pm, ct, slot) ->{
			getFriendMenu(ply, finalData, finalRequests).open(ply);
		});
		
		menu.addItem(4, 1, getRequestItem(finalRequests.getPendingRequests()));
		
		menu.addItem(6, 1, getAddFriendItem(), getHandlerForAddFriend(finalData));
		
		menu.addItem(8, 1, new Item(Material.BARRIER)
		.setTitle(Chat.GREEN+Chat.BOLD+"Remove Friends")
		.setLore("", Chat.GRAY+"Click to open.")
		.build());
		
		menu.addItem(menu.getSize()-1, new Item(Material.MAGMA_CREAM)
		.setTitle(ChatColor.YELLOW+"Refresh").build(), (ply, item, pm, ct, slot) -> {
			getRequestMenu(ply, finalData, null).open(ply);
		});
		
		for(Request request : finalRequests)
		{
			
			if(request.isFulfilled()){
				continue;
			}
			
			Item item = new Item(Material.SIGN)
			.setTitle(Chat.GREEN+Chat.BOLD+request.getName())
			.setLore("", Chat.GREEN+"Left click to accept the request",
					"",
					Chat.RED+"Right click to decline the request");
			
			menu.addItem(x, y, item.build(), (ply, it, pm, ct, slot) -> {
				if(ct.isLeftClick()){
					request.accept(ply.getUniqueId());
					Friend friend = new Friend(request.getSender(), request.getName());
					friend.load();
					finalData.addFriend(friend);
					request.setFulfilled(true);
					OasisCore.getNetworkUtilities().sendMessage(request.getName(), Chat.GOLD+"Your friend request to "+Chat.GREEN+ply.getName()+Chat.GOLD+" was accepted!");
					getRequestMenu(ply, finalData, finalRequests).open(ply);;
				}else if(ct.isRightClick()){
					request.decline(ply.getUniqueId());
					request.setFulfilled(true);
					getRequestMenu(pl, finalData, finalRequests).open(ply);
				}
			});
			
			x++;
			if(x > 9){
				x = 1;
				y++;
			}
			
		}
		
		return menu;
		
	}
	
	private static ItemStack getAddFriendItem() {
		return new Item(Material.BOOK_AND_QUILL)
		.setTitle(Chat.GREEN+Chat.BOLD+"Add Friend")
		.setLore("", Chat.GRAY+"Click to add a friend.")
		.build();
	}

	private static PacketMenuSlotHandler getHandlerForAddFriend(final FriendListData finalData){
		return (ply, it, pm, ct, slot) -> {
			AnvilPacketMenu anvil = new AnvilPacketMenu(ply);
			
			anvil.setResult(new ItemStack(Material.BOOK, 1));
			anvil.setDefaultText("Enter name here");
			anvil.setHandler((str, aply) -> {
				
				if(str == null){
					getRequestMenu(aply, finalData, null).open(aply);
				}
				
				UUID id = OasisCore.getDatabaseManager().getUUID(str);
				
				if(id == null){
					aply.sendMessage(Chat.RED+"No player named "+str+" could be found!");
				}else{
					aply.sendMessage(Chat.GREEN+Chat.BOLD+"Sending friend request....");
					Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
						OasisCommunity.getFriendManager().addRequest(aply.getUniqueId(), id);
						getRequestMenu(aply, finalData, null).open(aply);
					});
					
				}
				
			});
			
			pm.close(ply);
			anvil.open(ply);
		};
	}
	
	private static ItemStack getFriendOnlineItem(Player pl, int online) {
		return new Item(Material.SKULL_ITEM)
		.setOwner(pl.getName())
		.setTitle(Chat.GREEN+Chat.BOLD+"Friends")
		.setLore(Chat.GRAY+"You have "+Chat.BOLD+online+Chat.RESET+Chat.GRAY+" online friends.",
				"",
				Chat.GRAY+"Click to view your friends.")
		.build();
	}
	
	private static ItemStack getRequestItem(int requests){
		return new Item(Material.SIGN)
		.setTitle(Chat.GREEN+Chat.BOLD+"Friend Requests")
		.setLore(Chat.GRAY+"You have "+Chat.BOLD+requests+Chat.RESET+Chat.GRAY+" pending friend requests.",
				"",
				Chat.GRAY+"Click to view your friend requests")
		.build();
	}
	
	private static ItemStack getItem(Friend friend){
		Item stack = new Item(Material.SKULL_ITEM);
		stack.setOwner(friend.getName());
		
		if(friend.isOnline()){
			stack.setTitle(Chat.GREEN+friend.getName());
			stack.setLore(
					Chat.GRAY+Chat.BOLD+"Status: "+Chat.GREEN+Chat.BOLD+"Online",
					Chat.GRAY+"Currently on "+Chat.RESET+Chat.GOLD+friend.getServer(), 
					"",
					Chat.WHITE+Chat.UNDERLINE+"Click to join");
		}else{
			
			long time = System.currentTimeMillis() - friend.getLastSeen();
			
			stack.setTitle(Chat.RED+friend.getName());
			stack.setLore(Chat.GRAY+Chat.BOLD+"Status: "+Chat.RED+Chat.BOLD+"Offline",
					Chat.GRAY+"Last seen "+getLastSeenMessage(time));
			
		}
		
		return stack.build();
	}
	
	public static String getLastSeenMessage(long time){
		
		long seconds = time / 1000;
		long minutes = seconds /= 60;
		long hours = minutes /= 60;
		long days = hours /= 24;
		
		if(days > 0){
			return days+" days ago";
		}else if(hours > 0){
			return hours+" hours ago";
		}else if(minutes > 0){
			return minutes+" minutes ago";
		}else if(seconds > 0){
			return seconds+" seconds ago";
		}
		
		return "error";
		
	}
	
}
