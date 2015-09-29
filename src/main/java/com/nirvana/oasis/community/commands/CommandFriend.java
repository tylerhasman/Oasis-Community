package com.nirvana.oasis.community.commands;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.OasisCommunity;
import com.nirvana.oasis.community.craftbook.CraftBookMenu;
import com.nirvana.oasis.core.commands.CommandResult;
import com.nirvana.oasis.core.commands.OasisCommand;
import com.nirvana.oasis.mc.Chat;

public class CommandFriend implements OasisCommand {

	@Override
	public String getName() {
		return "friends";
	}

	@Override
	public String getPermission() {
		return "oasis.community.friends";
	}

	@Override
	public String getDescription() {
		return "Open a friends list";
	}

	@Override
	public CommandResult execute(Player pl, String[] args) {		
		
		if(args.length == 0){
			CraftBookMenu menu = new CraftBookMenu(pl);
			
			menu.open(pl);
		}else{
			if(args[0].equalsIgnoreCase("status")){
				String newStatus = "";
				for(int i = 1; i < args.length;i++){
					newStatus += args[i] + " ";
				}
				
				OasisCommunity.getSocialMedia().getProfile(pl.getUniqueId()).updateStatus(newStatus);
				
				pl.sendMessage(Chat.GREEN+"Your status has been set!");
			}else if(args[0].equalsIgnoreCase("open")){
				CraftBookMenu menu = new CraftBookMenu(pl);
				
				menu.open(pl);
			}else{
				return CommandResult.BAD_USAGE;
			}
		}

		
		return CommandResult.SUCCESS;
	}

	@Override
	public String getUsage() {
		return "/craftbook [help / open]";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"friend", "craftbook", "cb"};
	}

}
