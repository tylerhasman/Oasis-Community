package com.nirvana.oasis.community.craftbook;

import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

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
	public void updateStatus(String newStatus);
	
	/**
	 * Get this profiles status
	 * @return the profiles status
	 */
	public String getStatus();
	
	/**
	 * Inserts this profile into the database but only if the player doesnt already have a profile
	 * @return true if the profile was created
	 */
	public boolean createIfNotExists();
	
}
