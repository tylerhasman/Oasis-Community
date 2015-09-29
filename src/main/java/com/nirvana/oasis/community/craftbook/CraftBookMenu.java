package com.nirvana.oasis.community.craftbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.menu.AnvilPacketMenu;
import com.nirvana.oasis.core.menu.ChestPacketMenu;
import com.nirvana.oasis.core.menu.PacketMenu;
import com.nirvana.oasis.core.menu.PacketMenuSlotHandler;
import com.nirvana.oasis.mc.Chat;
import com.nirvana.oasis.mc.Item;

public class CraftBookMenu extends ChestPacketMenu {

	private Map<Integer, Follow> followerMappings;
	
	public CraftBookMenu(Player player) {
		super(9 * 6, Chat.DARK_AQUA+Chat.BOLD+"CraftBook Following", player);
	
		followerMappings = new HashMap<>();
		
		List<Follow> follows = OasisCommunity.getSocialMedia().getFollows(player.getUniqueId());
		
		int i = 1;
		int j = 2;
		
		addItem(0, new Item(Material.BOOK_AND_QUILL).setTitle(Chat.YELLOW+"Follow").setLore(Chat.GRAY+"Click to add a new follow").build());
		addItem(1, new Item(Material.SKULL_ITEM, 1, 3).setTitle(Chat.YELLOW+Chat.BOLD+"My Profile").setOwner(player.getName()).setLore(Chat.GRAY+"Click to view your profile").build());
		
		followerMappings.put(1, new IFollow(player.getUniqueId(), true, player.getUniqueId()));
		
		for(Follow follow : follows){
			
			List<String> lore = new ArrayList<String>();
			
			if(follow.isFollowingBack()){
				lore.add(Chat.GREEN+Chat.CHECK_MARK+" Following Back");
			}
			
			String server = OasisCore.getNetworkUtilities().getServer(follow.getPlayerName());
			
			if(server != null){
				lore.add(Chat.GREEN+Chat.BOLD+"Currently Playing: "+Chat.AQUA+server);
				lore.add(Chat.AQUA+Chat.ITALIC+"Right click to Join!");
			}else{
				lore.add(Chat.RED+Chat.ITALIC+"Offline");
			}
			
			lore.add(Chat.GRAY+"Left click to view profile");
			
			addItem(i + 1, j + 1, new Item(Material.SKULL_ITEM, 1, 3).setTitle(Chat.BLUE+follow.getPlayerName()).setLore(lore).setOwner(follow.getPlayerName()).build());
			
			followerMappings.put(translateCoord(i, j) + 10, follow);
			
			i++;
			
			if(i >= 9){
				i = 1;
				j++;
			}
			
		}
		
		addGeneralHandler(new PacketMenuSlotHandler() {
			
			@Override
			public void onClicked(Player player, ItemStack item, PacketMenu menu, ClickType clickType, int slot) {
				if(item.getType() == Material.SKULL_ITEM){
					
					Follow follow = followerMappings.get(slot);
					
					if(clickType == ClickType.RIGHT){
						
						
						
						String owner = follow.getPlayerName();
						
						String server = OasisCore.getNetworkUtilities().getServer(owner);
						
						if(server != null){
							OasisCore.getNetworkUtilities().connect(player, server);
							
							if(follow.isFollowingBack()){
								OasisCore.getNetworkUtilities().sendMessage(owner, Chat.YELLOW+player.getName()+Chat.GREEN+" is joining your game!");
							}
							
						}else{
							menu.close(player);
							player.sendMessage(Chat.RED+"That player is no longer online!");
						}
						
					}else if(clickType == ClickType.LEFT){
						
						PlayerProfile profile = OasisCommunity.getSocialMedia().getProfile(follow.getPlayer());
						
						profile.createIfNotExists();
						
						List<FeedEntry> feed = profile.getFeed();
						String status = profile.getStatus();
						
						ChestPacketMenu profilemenu = new ChestPacketMenu(9 * 4, profile.getOwnerName()+"'s Profile", player);
						
						String[] statusChoppedUp = WordUtils.wrap(status, 35, "\r\n", true).split("\r\n");
						String[] lore = new String[statusChoppedUp.length + 1];
						
						lore[0] = Chat.YELLOW+"Status: ";
						
						for(int i = 0; i < statusChoppedUp.length;i++){
							lore[i + 1] = Chat.GRAY+statusChoppedUp[i];
						}
						
						profilemenu.addItem(1, 1, new Item(Material.SKULL_ITEM, 1, 3).setTitle(Chat.YELLOW+profile.getOwnerName()).setOwner(profile.getOwnerName()).setLore(lore).build());
						
						if(!follow.getPlayer().equals(follow.getOwner().getUniqueId())){
							profilemenu.addItem(2, 1, new Item(Material.INK_SACK, 1, follow.isFollowingBack() ? 10 : 1).setTitle(Chat.GREEN+"Relationship Status").setLore(Chat.GRAY+"This player is "+(follow.isFollowingBack() ? "following you back" : "not following you back")).build());	
						}else{
							profilemenu.addItem(2, 1, new Item(Material.SIGN).setTitle(Chat.YELLOW+"How-To").setLore(Chat.GRAY+"Set your status by using:", Chat.GRAY+Chat.ITALIC+"/craftbook status [new status]").build());
						}
						
						int i = 0;
						
						for(FeedEntry entry : feed){
							
							profilemenu.addItem(i+1, 4, entry.buildItem());
							
							i++;
							
							if(i > 8){
								break;
							}
						}
						
						menu.close(player);
						profilemenu.open(player);
						
					}
					
				}else if(item.getType() == Material.BOOK_AND_QUILL){
					
					AnvilPacketMenu anvil = new AnvilPacketMenu(player);
					
					anvil.setDefaultText("Enter player name");
					anvil.setResult(new ItemStack(Material.SKULL_ITEM, 1, (short) 3));
					anvil.setHandler((str, pl) -> {
						
						UUID uuid = OasisCore.getDatabaseManager().getUUID(str);
						
						if(uuid == null){
							pl.sendMessage(Chat.RED+"No player named "+str+" has ever logged into NirvanaMC");
						}else{
							OasisCommunity.getSocialMedia().follow(pl, str, uuid);
							pl.sendMessage(Chat.GREEN+"You have successfully followed "+str);
						}
						
					});
					
					menu.close(player);
					anvil.open(player);
					
				}
			}
			
		});
	
	}
	
	

}
