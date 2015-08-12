package com.nirvana.oasis.community.commands;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.commands.CommandResult;
import com.nirvana.oasis.core.commands.OasisCommand;
import com.nirvana.oasis.mc.Chat;

public class CommandReply implements OasisCommand {

	@Override
	public String getName() {
		return "reply";
	}

	@Override
	public String getPermission() {
		return "oasis.community.reply";
	}

	@Override
	public String getDescription() {
		return "Reply to the last whisper you received or sent.";
	}

	@Override
	public CommandResult execute(Player pl, String[] args) {
		if(args.length == 0){
			return CommandResult.BAD_USAGE;
		}
		
		String message = "";
		
		for(String str : args){
			message += str + " ";
		}
		
		boolean result = OasisCommunity.getWhisperManager().reply(pl, message);
		
		if(!result){
			pl.sendMessage(Chat.RED+"You haven't sent or recieved a whisper yet!");
		}
		
		return CommandResult.SUCCESS;
	}

	@Override
	public String getUsage() {
		return "/"+getName()+" [message]";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "r" };
	}

}
