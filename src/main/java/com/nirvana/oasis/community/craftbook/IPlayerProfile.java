package com.nirvana.oasis.community.craftbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;
import com.nirvana.oasis.core.exception.OasisRuntimeException;
import com.nirvana.oasis.core.menu.ChestPacketMenu;
import com.nirvana.oasis.core.menu.PacketMenu;
import com.nirvana.oasis.core.menu.PacketMenuSlotHandler;
import com.nirvana.oasis.mc.Chat;
import com.nirvana.oasis.mc.Item;

public class IPlayerProfile implements PlayerProfile {

	private UUID owner;
	
	protected IPlayerProfile(UUID owner) {
		this.owner = owner;
	}
	
	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public String getOwnerName() {
		return OasisCore.getDatabaseManager().getName(owner);
	}

	@Override
	public void addToFeed(String title, ItemStack displayItem, String... info) {
		
		String serializedInfo = "";
		
		for(String str : info){
			serializedInfo += str+"\r\n";
		}
		
		try {
			
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bytes);
			
			oos.writeObject(displayItem.serialize());
			
			OasisCore.getDatabaseManager().execute("INSERT INTO `playerfeeds` (`playeruuid`, `title`, `item`, `info`, `timestamp`) VALUES (?, ?, ?, ?, ?)", owner.toString(), title, bytes.toByteArray(), serializedInfo, System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FeedEntry> getFeed() {
		
		List<FeedEntry> entries = new ArrayList<>();
		
		ResultSetList result = null;
		
		try {
			result = OasisCore.getDatabaseManager().query("SELECT * FROM `playerfeeds` WHERE `playeruuid`=?", owner.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result != null){
			
			while(result.next()){
				String title = result.getValue("title");
				String infoSerialized = result.getValue("info");
				
				String[] info = infoSerialized.split("\r\n");
				
				long timestamp = result.getValue("timestamp");
				byte[] blob = result.getValue("item");
				Map<String, Object> serializedItem = new HashMap<>();
				ItemStack item = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(blob));
					
					serializedItem = (Map<String, Object>) ois.readObject();
					
					item = ItemStack.deserialize(serializedItem);

					if(item == null){
						throw new OasisRuntimeException("Could not deserialize item!");
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				FeedEntry entry = new IFeedEntry(title, info, item, timestamp);
				
				entries.add(entry);
				
			}
			
		}
		
		return entries.stream().sorted((f1, f2) -> f1.compareTo(f2.getTimestamp())).collect(Collectors.toList());
	}

	@Override
	public void setStatus(String newStatus) {
		try {
			OasisCore.getDatabaseManager().execute("UPDATE `playerprofiles` SET `status`=? WHERE `playeruuid`=?", newStatus, owner.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getStatus() {
		
		try {
			ResultSetList data = OasisCore.getDatabaseManager().query("SELECT `status` FROM `playerprofiles` WHERE `playeruuid`=?", owner.toString());
		
			if(data.next()){
				return data.getValue("status");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean createIfNotExists() {
		
		try {
			return OasisCore.getDatabaseManager().execute("INSERT IGNORE INTO `playerprofiles` SET `playeruuid`=?", owner.toString()) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public PacketMenu getMenu(Player player) {
		
		PlayerProfile profileOfViewer = OasisCommunity.getSocialMedia().getProfile(player.getUniqueId());
		
		ChestPacketMenu profilemenu = new ChestPacketMenu(9 * 4, getOwnerName()+"'s Profile", player);
		
		String[] statusChoppedUp = WordUtils.wrap(getStatus(), 35, "\r\n", true).split("\r\n");
		String[] lore = new String[statusChoppedUp.length + 1];
		
		boolean isFollowMutual = OasisCommunity.getSocialMedia().isFollowMutual(player.getUniqueId(), getOwner());
		boolean isFollowing = profileOfViewer.isFollowing(getOwner());
		
		lore[0] = Chat.YELLOW+"Status: ";
		
		for(int i = 0; i < statusChoppedUp.length;i++){
			lore[i + 1] = Chat.GRAY+statusChoppedUp[i];
		}
		
		profilemenu.addItem(1, 1, new Item(Material.SKULL_ITEM, 1, 3).setTitle(Chat.YELLOW+getOwnerName()).setOwner(getOwnerName()).setLore(lore).build());
		
		if(!player.getUniqueId().equals(getOwner())){
			profilemenu.addItem(2, 1, new Item(Material.INK_SACK, 1, isFollowMutual ? 10 : 1).setTitle(Chat.GREEN+"Relationship Status").setLore(Chat.GRAY+"This player is "+(isFollowMutual ? "following you back" : "not following you back")).build());	
		}else{
			profilemenu.addItem(2, 1, new Item(Material.SIGN).setTitle(Chat.YELLOW+"How-To").setLore(Chat.GRAY+"Set your status by using:", Chat.GRAY+Chat.ITALIC+"/craftbook status [new status]").build());
		}
		
		if(isFollowing){
			profilemenu.addItem(8, 1, new Item(Material.BARRIER).setTitle(Chat.DARK_RED+"Unfollow").setLore(Chat.GRAY+"Click to unfollow").build());
		}
		
		profilemenu.addGeneralHandler(new PacketMenuSlotHandler() {
			
			@Override
			public void onClicked(Player player, ItemStack item, PacketMenu menu,
					ClickType clickType, int slot) {
				
				if(item.getType() == Material.BARRIER){
					
				}
				
			}
		});
		
		int i = 0;
		
		for(FeedEntry entry : getFeed()){
			
			profilemenu.addItem(i+1, 4, entry.buildItem());
			
			i++;
			
			if(i > 8){
				break;
			}
		}
		
		return profilemenu;
	}

	@Override
	public List<Follow> getFollowers() {
		List<Follow> follows = new ArrayList<>();
		
		ResultSetList result = null;
		
		try {
			result = OasisCore.getDatabaseManager().query("SELECT * FROM `socialmedia` WHERE `targetuuid`=?", getOwner().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result != null){
			while(result.next()){
				
				Follow follow = null;
				
				UUID player = UUID.fromString(result.getValue("player"));
				
				follow = new IFollow(getOwner(), isFollowing(player), player);
				
				follows.add(follow);
				
			}
		}
		
		return follows;
	}

	@Override
	public List<Follow> getFollows() {
		List<Follow> follows = new ArrayList<>();
		
		ResultSetList result = null;
		
		try {
			result = OasisCore.getDatabaseManager().query("SELECT * FROM `socialmedia` WHERE `player`=?", getOwner().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result != null){
			while(result.next()){
				
				Follow follow = null;
				
				UUID player = UUID.fromString(result.getValue("targetuuid"));
				
				PlayerProfile profile = OasisCommunity.getSocialMedia().getProfile(player);
				
				follow = new IFollow(player, profile.isFollowing(getOwner()), getOwner());
				
				follows.add(follow);
				
			}
		}
		
		return follows;
	}

	@Override
	public void follow(UUID targetUuid) {
		if(isFollowing(targetUuid)){
			return;
		}
		
		String target = OasisCore.getDatabaseManager().getName(targetUuid);
		
		try {
			OasisCore.getDatabaseManager().execute("INSERT INTO `socialmedia` (`player`, `targetuuid`) VALUES(?, ?) ", getOwner().toString(), targetUuid.toString());
			addToFeed("Following "+target, new ItemStack(Material.BOOK, 1), Chat.GREEN+getOwnerName()+" is now following "+target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OasisCore.getNetworkUtilities().sendMessage(target, Chat.GREEN+target+" just followed you on CraftBook! Use /cb to follow them back!");
		
	}
	
	@Override
	public void unfollow(UUID uuid) {
		if(!isFollowing(uuid)){
			return;
		}
		try {
			OasisCore.getDatabaseManager().execute("DELETE FROM `socialmedia` WHERE `player`=? AND `targetuuid`=?", getOwner().toString(), uuid.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isFollowing(UUID target) {
		
		try {
			ResultSetList data = OasisCore.getDatabaseManager().query("SELECT COUNT(*) FROM `socialmedia` WHERE `player`=? AND `targetuuid`=?", getOwner().toString(), target.toString());
			
			long count = 0;
			
			if(data.next()){
				count = data.getValue("COUNT(*)");
			}
			
			return count > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
