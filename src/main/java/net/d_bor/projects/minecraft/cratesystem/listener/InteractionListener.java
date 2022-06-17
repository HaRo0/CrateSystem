package net.d_bor.projects.minecraft.cratesystem.listener;

import org.bukkit.event.block.Action;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.d_bor.projects.minecraft.cratesystem.crate.Crate;
import net.d_bor.projects.minecraft.cratesystem.data.Messages;
import net.d_bor.projects.minecraft.cratesystem.data.Values;

public class InteractionListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		Crate crate = Values.placedCrate(e.getClickedBlock());

		if (crate != null) {

			e.setCancelled(true);

			if (e.getAction()==Action.RIGHT_CLICK_BLOCK) {

				if(!crate.openCrate(e.getPlayer())) {
					
					e.getPlayer().sendMessage(Messages.getMessage("nokey"));
					
				}

			} else if (e.getAction()==Action.LEFT_CLICK_BLOCK) {

				crate.getPossibleWins(e.getPlayer(), true);

			}
		}
	}
}
