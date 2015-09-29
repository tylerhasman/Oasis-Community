package com.nirvana.oasis.community.craftbook;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.database.ResultSetList;
import com.nirvana.oasis.mc.Chat;

public class ICraftBook implements CraftBook {

	@Override
	public void follow(Player player, String target, UUID targetId) {
		
		if(isFollowing(player.getUniqueId(), targetId)){
			return;
		}
		
		try {
			OasisCore.getDatabaseManager().execute("INSERT INTO `socialmedia` (`player`, `targetuuid`) VALUES(?, ?) ", player.getUniqueId().toString(), targetId.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OasisCore.getNetworkUtilities().sendMessage(target, Chat.GREEN+player.getName()+" just followed you on CraftBook! Use /cb to follow them back!");
		
	}

	@Override
	public List<Follow> getFollowers(UUID uuid) {
		
		List<Follow> follows = new ArrayList<>();
		
		ResultSetList result = null;
		
		try {
			result = OasisCore.getDatabaseManager().query("SELECT * FROM `socialmedia` WHERE `targetuuid`=?", uuid.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result != null){
			while(result.next()){
				
				Follow follow = null;
				
				UUID player = UUID.fromString(result.getValue("player"));
				
				follow = new IFollow(uuid, isFollowing(uuid, player), player);
				
				follows.add(follow);
				
			}
		}
		
		return follows;
	}

	@Override
	public List<Follow> getFollows(UUID uuid) {
		List<Follow> follows = new ArrayList<>();
		
		ResultSetList result = null;
		
		try {
			result = OasisCore.getDatabaseManager().query("SELECT * FROM `socialmedia` WHERE `player`=?", uuid.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(result != null){
			while(result.next()){
				
				Follow follow = null;
				
				UUID player = UUID.fromString(result.getValue("targetuuid"));
				
				follow = new IFollow(player, isFollowing(player, uuid), uuid);
				
				follows.add(follow);
				
			}
		}
		
		return follows;
	}

	@Override
	public boolean isFollowMutal(UUID player1, UUID player2) {
		return isFollowing(player1, player2) && isFollowMutal(player2, player1);
	}

	@Override
	public boolean isFollowing(UUID player, UUID target) {
		
		try {
			ResultSetList data = OasisCore.getDatabaseManager().query("SELECT COUNT(*) FROM `socialmedia` WHERE `player`=? AND `targetuuid`=?", player.toString(), target.toString());
			
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

	@Override
	public PlayerProfile getProfile(UUID player) {
		PlayerProfile profile = new IPlayerProfile(player);
		
		return profile;
	}

}
