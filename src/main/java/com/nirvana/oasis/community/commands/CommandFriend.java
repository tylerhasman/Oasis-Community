package com.nirvana.oasis.community.commands;

import org.bukkit.entity.Player;

import com.nirvana.oasis.community.craftbook.CraftBookMenu;
import com.nirvana.oasis.core.commands.CommandResult;
import com.nirvana.oasis.core.commands.OasisCommand;

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
		
		/*pl.sendMessage(Chat.YELLOW+Chat.BOLD+"LOADING FRIENDS LIST...");
		
		Bukkit.getScheduler().runTaskAsynchronously(OasisCommunity.getInstance(), () -> {
			PacketMenu menu = FriendListMenuBuilder.getFriendMenu(pl, null, null);
			
			menu.open(pl);
		});*/
		
		CraftBookMenu menu = new CraftBookMenu(pl);
		
		menu.open(pl);
		
		return CommandResult.SUCCESS;
	}

	@Override
	public String getUsage() {
		return "/friends";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"friend", "craftbook"};
	}

}
