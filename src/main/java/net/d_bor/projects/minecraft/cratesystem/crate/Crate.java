package net.d_bor.projects.minecraft.cratesystem.crate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.d_bor.library.utils.Random;
import net.d_bor.projects.minecraft.cratesystem.CrateSystem;
import net.d_bor.projects.minecraft.cratesystem.data.Item;
import net.d_bor.projects.minecraft.cratesystem.data.SchedulerRunnable;
import net.d_bor.projects.minecraft.cratesystem.data.Values;

public class Crate {

	private static final transient Random r = new Random();
	private static final transient int minCrateItems = 60;
	private static final transient int maxCrateItems = 80;

	private final String name;
	private final ArrayList<Item> items = new ArrayList<>();
	private final ItemStack key;

	public Crate(String name) {

		this.name = name;

		ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);

		ItemMeta meta = key.getItemMeta();

		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(name + " §6key");
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§8-------------------------------------------");
		lore.add("§aOeffne eine §6" + name + " §amit diesem key");
		meta.setLore(lore);
		key.setItemMeta(meta);
		
		this.key = key;

	}

	public Crate() {

		this("Crate");

	}

	public boolean addItem(ItemStack item, Float probability) {

		if (item == null || getProbability() + probability > 100) {

			return false;

		}

		items.add(new Item(item, probability));

		return true;

	}

	public float getProbability() {

		float probability = 0;

		for (Item item : this.items) {

			probability += item.getProbability();

		}

		return probability;

	}

	private static Inventory crateInventory(String title) {

		CrateInvHolder invHolder = new CrateInvHolder();
		Inventory inv = Bukkit.createInventory(invHolder, 9 * 3, title);
		invHolder.setInventory(inv);

		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f");
		item.setItemMeta(meta);

		for (int i = 0; i < inv.getSize(); i++) {

			inv.setItem(i, item);

		}

		item.setType(Material.HOPPER);
		inv.setItem(4, item);

		item.setType(Material.GUNPOWDER);
		inv.setItem(22, item);

		return inv;
	}

	private LinkedList<Item> getRandomItems(Inventory inv) {

		LinkedList<Item> items = new LinkedList<>();

		// Calculate size pseudo random so the win pick doesn't always needs the same
		// time
		int size = r.nextInt(minCrateItems, maxCrateItems);
		Item winItem = getWinItem(inv);

		for (int i = 0; i < size; i++) {

			if (i == size - 5) {

				items.add(items.size(), winItem);

			} else {

				Item item = this.items.get(r.nextInt(0, this.items.size() - 1));

				if (items.size() > 0) {
					while (item == items.getLast()) {

						item = this.items.get(r.nextInt(0, this.items.size() - 1));

					}
				}

				items.add(items.size(), item);

			}
		}

		return items;

	}

	private Item getWinItem(Inventory inv) {

		float winPercent = r.nextFloat(0, 100);
		float currentPercent = 0;
		for (Item item : this.items) {

			currentPercent += item.getProbability();

			if (winPercent <= currentPercent) {

				Values.setWinItem(inv, item);
				return item;

			}

		}

		return null;

	}

	private static int calcTicks(int itemsSize) {

		int x = maxCrateItems - itemsSize;

		if (0 <= x && x <= maxCrateItems - 9) {

			return 1;

		} else if (maxCrateItems - 9 <= x && x <= maxCrateItems) {

			return (int) ((4.915973568162e-10 * Math.pow(x, 6)) - (5.3217262802508 * x) + 315.86880491484);

		} else {

			return -1;

		}

	}

	public boolean openCrate(Player player) {

		if (player.getInventory().containsAtLeast(key, 1)) {

			boolean deletedItem = false;
			for (ItemStack item : player.getInventory()) {

				if (key.isSimilar(item) && !deletedItem) {

					item.setAmount(item.getAmount() - 1);
					deletedItem = true;

				}

			}

			Inventory inv = crateInventory(name);

			LinkedList<Item> items = getRandomItems(inv);
			LinkedList<Item> buffer = new LinkedList<>();

			for (int i = 0; i < 9; i++) {

				buffer.add(items.remove(0));

			}

			SchedulerRunnable runnable = new SchedulerRunnable() {
				@Override
				public void task() {

					int i = 0;

					if (!items.isEmpty() && getCounter() == calcTicks(items.size())) {

						buffer.remove(0);
						buffer.add(items.remove(0));

						for (Item item : buffer) {
							inv.setItem(9 + 8 - i, new ItemStack(item.getItem()));
							i++;
						}

						resetCounter();
					}

					if (items.isEmpty()) {

						if (getCounter() % 10 == 0 && getCounter() != 0) {

							inv.setItem((int) (9 + 4 - (getCounter() / 10)), null);
							inv.setItem((int) (9 + 4 + (getCounter() / 10)), null);

							if (getCounter() == 40) {

								HashMap<Integer, ItemStack> drops = player.getInventory()
										.addItem(new ItemStack(Values.getWinItem(inv).getItem()));
								for (ItemStack drop : drops.values()) {

									player.getWorld().dropItemNaturally(player.getLocation(), drop);

								}

								player.updateInventory();
								stop();

							}
						}
					}
				}
			};

			runnable.setID(Bukkit.getScheduler().scheduleSyncRepeatingTask(CrateSystem.getInstance(), runnable, 1, 1));

			player.openInventory(inv);
			return true;

		}

		return false;
	}

	public void getPossibleWins(Player player, boolean showPercentages) {

		int invSize = 9 * 3;

		CrateInvHolder holder = new CrateInvHolder();
		Inventory inv = Bukkit.createInventory(holder, invSize, name);
		holder.setInventory(inv);

		ItemStack placeholder = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta placeholderMeta = placeholder.getItemMeta();
		placeholderMeta.setDisplayName("§f");
		placeholder.setItemMeta(placeholderMeta);

		for (int i = 0; i < invSize; i++) {

			ItemStack item = (i < items.size()) ? new ItemStack(items.get(i).getItem()) : placeholder;
			if (showPercentages && i < items.size()) {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<>();
				if (meta.hasLore()) {
					lore = meta.getLore();
				}
				lore.add("§8-----------------------------------");
				lore.add("§bGewinnwahrscheinlichkeit: §6" + items.get(i).getProbability() + "%");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}

			inv.setItem(i, item);

		}

		player.openInventory(inv);
	}

	public ItemStack getKey() {

		return key;

	}
}
