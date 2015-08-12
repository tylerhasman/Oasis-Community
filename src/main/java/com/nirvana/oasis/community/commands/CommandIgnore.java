package com.nirvana.oasis.community.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.core.OasisCore;
import com.nirvana.oasis.core.commands.CommandResult;
import com.nirvana.oasis.core.commands.OasisCommand;
import com.nirvana.oasis.mc.Chat;

public class CommandIgnore implements OasisCommand {

	@Override
	public String getName() {
		return "ignore";
	}

	@Override
	public String getPermission() {
		return "oasis.community.ignore";
	}

	@Override
	public String getDescription() {
		return "Ignore a user";
	}

	@Override
	public CommandResult execute(Player pl, String[] args) {
		if(args.length > 0){
			
			String target = args[0];
			
			Player plt = Bukkit.getPlayer(target);
			
			if(plt != null){
				
				OasisCommunity.getIgnoreManager().ignorePlayer(pl, plt);
				pl.sendMessage(Chat.GREEN+"Player ignored successfully.");
				
			}else{
				OasisCore.getLocaleManager().sendPlayerNotFound(pl, target);
			}
			
			
			return CommandResult.SUCCESS;
		}
		return CommandResult.BAD_USAGE;
	}

	@Override
	public String getUsage() {
		return "/"+getName()+" [player]";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}

}
