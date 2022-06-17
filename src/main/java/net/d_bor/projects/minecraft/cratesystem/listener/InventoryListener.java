package net.d_bor.projects.minecraft.cratesystem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.d_bor.projects.minecraft.cratesystem.crate.CrateInvHolder;

public class InventoryListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof CrateInvHolder) {

			e.setCancelled(true);

		}
	}
}
