package com.nirvana.oasis.community.craftbook;

import org.bukkit.inventory.ItemStack;

import com.nirvana.oasis.mc.Item;

public class IFeedEntry implements FeedEntry {

	private String title;
	private String[] info;
	private ItemStack item;
	private long timestamp;

	protected IFeedEntry(String title, String[] info, ItemStack item, long timestamp) {
		this.title = title;
		this.info = info;
		this.item = item;
		this.timestamp = timestamp;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	@Override
	public String[] getInfo() {
		return info;
	}

	@Override
	public ItemStack buildItem() {
		
		for(String s : info){
			System.out.println(s);
		}
		
		return new Item(item).setTitle(title).setLore(info).build();
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int compareTo(Long other) {
		if(timestamp > other){
			return 1;
		}else if(timestamp < other){
			return -1;
		}
		return 0;
	}

}
