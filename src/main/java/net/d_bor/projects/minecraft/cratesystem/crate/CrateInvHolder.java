package net.d_bor.projects.minecraft.cratesystem.crate;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CrateInvHolder implements InventoryHolder {

	private Inventory inv;

	public void setInventory(Inventory inv) {

		this.inv = inv;

	}

	@Override
	public Inventory getInventory() {

		return inv;

	}
}
