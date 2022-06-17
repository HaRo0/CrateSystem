package net.d_bor.projects.minecraft.cratesystem.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.d_bor.projects.minecraft.cratesystem.crate.Crate;
import net.d_bor.projects.minecraft.cratesystem.data.Messages;
import net.d_bor.projects.minecraft.cratesystem.data.Values;

public class CrateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (player.hasPermission("cratesystem.admin")) {
				if (args.length == 2) {

					if (args[0].equalsIgnoreCase("add")) {

						if (Values.addCrate(args[1])) {

							player.sendMessage(Messages.getMessage("cratecreated").replaceFirst("&Name", args[1]));

						} else {

							player.sendMessage(Messages.getMessage("cratenotcreated").replaceFirst("&Name", args[1]));

						}

						return true;

					} else if (args[0].equalsIgnoreCase("remove")) {

						if (Values.removeCrate(args[1])) {

							player.sendMessage(Messages.getMessage("cratedeleted").replaceFirst("&Name", args[1]));

						} else {

							player.sendMessage(Messages.getMessage("cratenotdeleted").replaceFirst("&Name", args[1]));

						}

						return true;

					} else if (args[0].equalsIgnoreCase("setcrate")) {

						if (Values.crateExists(args[1])) {

							Values.placeCrate(args[1], player.getLocation());

							player.sendMessage(Messages.getMessage("crateplaced").replaceFirst("&Name", args[1]));

						} else {

							player.sendMessage(Messages.getMessage("cratedoesntexist").replaceFirst("&Name", args[1]));

						}

						return true;

					} else if (args[0].equalsIgnoreCase("removecrate")) {

						if (Values.crateExists(args[1])) {

							Values.unplaceCrate(args[1], player.getLocation());

							player.sendMessage(Messages.getMessage("crateunplaced").replaceFirst("&Name", args[1]));

						} else {

							player.sendMessage(Messages.getMessage("cratedoesntexist").replaceFirst("&Name", args[1]));

						}

						return true;

					} else if (args[0].equalsIgnoreCase("getkey")) {

						if (Values.crateExists(args[1])) {

							Values.givePlayerKey(args[1], player);

							player.sendMessage(Messages.getMessage("gotkey"));

						} else {

							player.sendMessage(Messages.getMessage("cratedoesntexist").replaceFirst("&Name", args[1]));

						}

						return true;

					} else {

						player.sendMessage(Messages.getMessage("unknownarg"));

					}
				} else if ((args.length == 3)) {

					if (args[0].equalsIgnoreCase("additem")) {

						if (Values.crateExists(args[1])) {

							ItemStack item = player.getInventory().getItemInMainHand();

							Crate crate = Values.getCrate(args[1]);

							float probability;

							if (args[2].equalsIgnoreCase("all")) {

								probability = 100 - crate.getProbability();

							} else {

								try {

									probability = Float.parseFloat(args[2]);

								} catch (Exception e) {

									player.sendMessage("onlypossibleprobabilitys");

									return false;

								}
							}

							if (item.getType() != Material.AIR) {

								if (Values.getCrate(args[1]).addItem(item, probability)) {

									player.sendMessage(Messages.getMessage("itemadded").replaceFirst("&Name", args[1]));

								} else {

									player.sendMessage(Messages.getMessage("tohighprobability"));

								}

							} else {

								player.sendMessage(Messages.getMessage("noiteminhand"));

							}

						} else {

							player.sendMessage(Messages.getMessage("cratedoesntexist"));

						}

						return true;

					} else {

						player.sendMessage(Messages.getMessage("unknownarg"));

					}

				} else {

					player.sendMessage(Messages.getMessage("unknownarglength"));

				}
			} else {

				player.sendMessage(Messages.getMessage("noperms"));

			}
		} else {

			sender.sendMessage(Messages.getMessage("onlyplayer"));

		}

		return false;
	}

}
