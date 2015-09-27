package com.nirvana.oasis.community.craftbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;
import com.nirvana.oasis.core.exception.OasisRuntimeException;

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
			serializedInfo += str+"|||||";
		}
		
		try {
			
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bytes);
			
			oos.writeObject(displayItem.serialize());
			
			ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(inputBytes);
			
			OasisCore.getDatabaseManager().execute("INSERT INTO `playerfeeds` (`playeruuid`, 'title`, `item`, `info`, `timestamp`) VALUES (?, ?, ?, ?, ?)", owner.toString(), title, ois, serializedInfo, System.currentTimeMillis());
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
				String[] info = infoSerialized.split("|||||");
				long timestamp = result.getValue("timestamp");
				Blob blob = result.getValue("item");
				Map<String, Object> serializedItem = new HashMap<>();
				ItemStack item = null;
				try {
					ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
					
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
	public void updateStatus(String newStatus) {
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

}
