package com.nirvana.oasis.community.craftbook;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.core.menu.PacketMenu;

public interface PlayerProfile {

	/**
	 * Get the uuid of the owner of this profile
	 * @return the players uuid
	 */
	public UUID getOwner();
	
	/**
	 * Get the name of the owner of this profile
	 * @return the players name
	 */
	public String getOwnerName();
	
	/**
	 * Add a new post to the players feed
	 * @param title The posts title
	 * @param displayItem the item to display as the post
	 * @param info This will be tacked onto the item as its lore
	 */
	public void addToFeed(String title, ItemStack displayItem, String... info);
	
	/**
	 * Get the players feed sorted by the newest entries first
	 * @return the players feed
	 */
	public List<FeedEntry> getFeed();
	
	/**
	 * Change this profiles status
	 * @param newStatus the new status
	 */
	public void setStatus(String newStatus);
	
	/**
	 * Get this profiles status
	 * @return the profiles status
	 */
	public String getStatus();
	
	/**
	 * Inserts this profile into the database but only if the player doesnt already have a profile
	 * @return true if the profile was created
	 */
	boolean createIfNotExists();
	

	/**
	 * Get a list of players following this player
	 * @param player the player
	 * @return a list of players following the player
	 */
	public List<Follow> getFollowers();
	
	/**
	 * Get a list of players this player is following
	 * @param player the player
	 * @return the list of players this player is following
	 */
	public List<Follow> getFollows();
	
	/**
	 * Follow a user for this profile
	 * @param player the player
	 * @param target the target to follow
	 * @param targetUuid the targets uuid
	 */
	public void follow(UUID targetUuid);
	
	/**
	 * Unfollow a user from this profile
	 * @param uuid the player to unfollows uuid
	 */
	public void unfollow(UUID uuid);
	
	/**
	 * Check if a player is following another player
	 * @param player the player
	 * @param target the target to check
	 * @return true if player is following target
	 */
	public boolean isFollowing(UUID target);
	
	/**
	 * Get a GUI for players
	 * @param player the player the packet menu will be shown to
	 * @return the packet menu
	 */
	public PacketMenu getMenu(Player player);
	
}
