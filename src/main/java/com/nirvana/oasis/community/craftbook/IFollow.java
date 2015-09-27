package com.nirvana.oasis.community.craftbook;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.nirvana.oasis.core.OasisCore;

public class IFollow implements Follow {

	private final UUID player;
	private final boolean followingBack;
	private final UUID owner;

	protected IFollow(UUID player, boolean followingBack, UUID owner) {
		this.player = player;
		this.followingBack = followingBack;
		this.owner = owner;
	}
	
	@Override
	public UUID getPlayer() {
		return player;
	}
	
	@Override
	public String getPlayerName() {
		return OasisCore.getDatabaseManager().getName(player);
	}

	@Override
	public boolean isFollowingBack() {
		return followingBack;
	}

	@Override
	public Player getOwner() {
		return Bukkit.getPlayer(owner);
	}

}
