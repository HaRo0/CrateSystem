package net.d_bor.projects.minecraft.cratesystem.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.d_bor.library.files.ConfigFileSaver;
import net.d_bor.projects.minecraft.cratesystem.crate.Crate;

public class Values {

	private static transient Values values;
	private static transient final String fileName = "Data.json";
	private static transient File path;
	private static transient final HashMap<Inventory, Item> invWinItems = new HashMap<>();

	private HashMap<String, Crate> crates = new HashMap<>();
	private HashMap<String, String> crateLocs = new HashMap<>();

	public static boolean setWinItem(Inventory inv, Item item) {

		if (!invWinItems.containsKey(inv)) {

			invWinItems.put(inv, item);

			return true;
		}

		return false;
	}

	public static Item getWinItem(Inventory inv) {

		Item winItem = invWinItems.get(inv);
		invWinItems.remove(inv);
		return winItem;

	}

	public static void givePlayerKey(String name, Player player) {

		HashMap<Integer, ItemStack> drops = player.getInventory().addItem(values.crates.get(name).getKey());

		for (ItemStack drop : drops.values()) {

			player.getWorld().dropItemNaturally(player.getLocation(), drop);

		}

		player.updateInventory();

	}

	public static boolean addCrate(String name) {

		if (!values.crates.containsKey(name)) {

			values.crates.put(name, new Crate(name));
			
			return true;

		}

		return false;
	}

	public static void placeCrate(String name, Location loc) {

		loc.getBlock().setType(Material.CHEST);
		values.crateLocs.put(loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ(), name);

	}

	public static void unplaceCrate(String name, Location loc) {

		loc.getBlock().setType(Material.AIR);
		values.crateLocs.remove(loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ(), name);

	}

	public static Crate placedCrate(Block block) {

		return getCrate(values.crateLocs.get(block.getX() + ":" + block.getY() + ":" + block.getZ()));

	}

	public static boolean removeCrate(String name) {

		if (values.crates.containsKey(name)) {

			values.crates.remove(name);
			
			return true;

		}

		return false;
	}

	public static final Crate getCrate(String name) {

		return values.crates.get(name);

	}

	private static final boolean exists() {

		return values != null;

	}

	public static boolean crateExists(String name) {

		return values.crates.containsKey(name);

	}

	public static final boolean saveValues() {

		if (exists()) {

			try {

				ConfigFileSaver.saveAsFile(path.toString(), fileName, values);
				return true;

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		return false;

	}

	public static final boolean loadValues(File path) {

		if (!exists()) {

			try {

				Values.path = path;
				values = ConfigFileSaver.loadFromFile(path.toString(), fileName, Values.class);

			} catch (FileNotFoundException e) {

				values = new Values();
				saveValues();

			}

			return true;

		}

		return false;

	}

}
