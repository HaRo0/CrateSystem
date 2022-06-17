package net.d_bor.projects.minecraft.cratesystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.d_bor.projects.minecraft.cratesystem.commands.CrateCommand;
import net.d_bor.projects.minecraft.cratesystem.data.*;
import net.d_bor.projects.minecraft.cratesystem.listener.*;

public class CrateSystem extends JavaPlugin {

	private static CrateSystem instance;

	@Override
	public void onEnable() {

		instance = this;

		commands();
		listener();

		Messages.loadMessages(getDataFolder());
		Values.loadValues(getDataFolder());

		test();
	}

	private void test() {

		if (!Values.crateExists("Test")) {
			Values.addCrate("Test");
			Values.getCrate("Test").addItem(new ItemStack(Material.ACACIA_BUTTON), 1.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.BEDROCK), 5.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.BIRCH_SIGN), 10.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.BLUE_BANNER), 20.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.BOW), 4.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.BLUE_CONCRETE), 8.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.WHITE_CONCRETE_POWDER), 2.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.TIPPED_ARROW), 9.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.STONECUTTER), 1.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.STRAY_SPAWN_EGG), 30.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.STONE_BRICK_WALL), 5.0f);
			Values.getCrate("Test").addItem(new ItemStack(Material.SQUID_SPAWN_EGG), 5.0f);

		}
	}

	@Override
	public void onDisable() {

		Values.saveValues();

	}

	private void commands() {

		Bukkit.getPluginCommand("crate").setExecutor(new CrateCommand());
		
	}

	public void listener() {

		Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
		Bukkit.getPluginManager().registerEvents(new InteractionListener(), this);

	}

	public static CrateSystem getInstance() {

		return instance;

	}

}
