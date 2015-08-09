package com.nirvana.oasis.community.whisper;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.ranks.Rank;
import com.nirvana.oasis.mc.Chat;

public class IWhisperManager implements WhisperManager {

	private static final String format = "%s %s "+Chat.GRAY+"says %s";
	
	@Override
	public boolean sendWhisper(Player from, String to, String message) {
		
		UUID fromid = null;
		UUID toid = null;
		
		fromid = from.getUniqueId();
		
		Player pl2 = Bukkit.getPlayer(to);
		
		if(pl2 == null){
			toid = OasisCore.getDatabaseManager().getUUID(to);
		}else{
			toid = pl2.getUniqueId();
		}
		
		return sendWhisper(from.getName(), fromid, to, toid, message);
		
	}

	@Override
	public boolean reply(Player from, String message) {
		UUID id = from.getUniqueId();
		
		String lastWhisper = OasisCore.getJedisManager().hget("player:"+id, "lastwhisper");
		
		if(lastWhisper != null){
			return sendWhisper(from, lastWhisper, message);
		}else{
			return false;
		}
		
	}

	public boolean sendWhisper(String fromName, UUID from, String toName, UUID to, String message) {
		
		if(to == null){
			return false;
		}
		
		Rank rank = OasisCore.getRankManager().getRank(from);
		
		String formattedMessage = String.format(format, rank.getPrefix(), fromName, message);
		
		OasisCore.getNetworkUtilities().sendMessage(toName, formattedMessage);
		OasisCore.getNetworkUtilities().sendMessage(fromName, formattedMessage);
		
		OasisCore.getJedisManager().hset("player:"+from, "lastwhisper", toName);
		OasisCore.getJedisManager().hset("player:"+to, "lastwhisper", fromName);
		
		return true;
	}

}
