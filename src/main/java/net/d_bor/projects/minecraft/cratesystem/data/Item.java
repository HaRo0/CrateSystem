package net.d_bor.projects.minecraft.cratesystem.data;

import org.bukkit.inventory.ItemStack;

public class Item {

	private final ItemStack item;
	private final float probability;

	public Item(ItemStack item, float probability) {

		this.item = item;
		this.probability = probability;

	}

	public float getProbability() {
		return probability;
	}

	public ItemStack getItem() {
		return item;
	}
}
