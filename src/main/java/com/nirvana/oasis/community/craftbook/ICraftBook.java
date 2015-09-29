package com.nirvana.oasis.community.craftbook;

import java.util.UUID;

public class ICraftBook implements CraftBook {

	@Override
	public boolean isFollowMutual(PlayerProfile profile1, PlayerProfile profile2) {
		return profile1.isFollowing(profile2.getOwner()) && profile2.isFollowing(profile1.getOwner());
	}

	@Override
	public PlayerProfile getProfile(UUID player) {
		return getProfile(player, true);
	}

	@Override
	public boolean isFollowMutual(UUID player1, UUID player2) {
		return isFollowMutual(getProfile(player1), getProfile(player2));
	}

	@Override
	public PlayerProfile getProfile(UUID player, boolean createIfNotExists) {

		PlayerProfile profile = new IPlayerProfile(player);
		
		if(createIfNotExists){
			profile.createIfNotExists();
		}
		
		return profile;
	}

}
