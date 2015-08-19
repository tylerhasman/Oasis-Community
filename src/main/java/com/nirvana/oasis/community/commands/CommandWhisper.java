package com.nirvana.oasis.community.commands;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.commands.CommandResult;
import com.nirvana.oasis.core.commands.OasisCommand;

public class CommandWhisper implements OasisCommand {

	@Override
	public String getName() {
		return "tell";
	}

	@Override
	public String getPermission() {
		return "oasis.community.tell";
	}

	@Override
	public String getDescription() {
		return "Whisper to players across servers!";
	}

	@Override
	public CommandResult execute(Player pl, String[] args) {
		
		if(args.length < 2){
			return CommandResult.BAD_USAGE;
		}
		
		String name = args[0];
		
		String message = "";
		
		for(int i = 1; i < args.length;i++){
			message += args[i] + " ";
		}
		
		boolean result = OasisCommunity.getWhisperManager().sendWhisper(pl, name, message);
		
		if(!result){
			OasisCore.getLocaleManager().sendPlayerNotFound(pl, name);
		}
		
		return CommandResult.SUCCESS;
	}

	@Override
	public String getUsage() {
		return "/"+getName()+" [player] [message]";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "whisper", "msg", "w" };
	}

}
