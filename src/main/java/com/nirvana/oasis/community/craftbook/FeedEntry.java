package com.nirvana.oasis.community.craftbook;

import org.bukkit.inventory.ItemStack;

public interface FeedEntry extends Comparable<Long>{

	/**
	 * Get the title of this feed entry
	 * @return the feeds title
	 */
	public String getTitle();
	
	/**
	 * <div>Get the item stack that will represent this feed entry</div>
	 * This item should not be formatted with its title and info
	 * 
	 * @return the item stack
	 */
	public ItemStack getItem();
	
	/**
	 * Get this feed entries info
	 * @return the info
	 */
	public String[] getInfo();
	
	/**
	 * Get this item that should be displayed to the player.
	 * This item should have its title and lore
	 * @return the item
	 */
	public ItemStack buildItem();
	
	/**
	 * Get the time that this feed entry was added
	 * @return the timestamp
	 */
	public long getTimestamp();
	
}
