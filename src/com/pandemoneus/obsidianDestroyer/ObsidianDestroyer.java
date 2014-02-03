package com.pandemoneus.obsidianDestroyer;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The ObsidianDestroyer plugin.
 * 
 * Allows certain explosions to destroy Obsidian.
 * 
 * @author Pandemoneus
 * 
 */
public final class ObsidianDestroyer extends JavaPlugin {
	/**
	 * Plugin related stuff
	 */
	private final ODCommands cmdExecutor = new ODCommands(this);
	private ODConfig config = new ODConfig(this);
	private final ODEntityListener entityListener = new ODEntityListener(this);
	private Permission permissionsHandler;
	private boolean permissionsFound = true;

	private static String version;
	private static final String PLUGIN_NAME = "ObsidianDestroyer";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDisable() {
		config.saveDurabilityToFile();
		Log.info(PLUGIN_NAME + " disabled");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		version = pdfFile.getVersion();

		Log.info(PLUGIN_NAME + " v" + version + " enabled");

		getCommand("obsidiandestroyer").setExecutor(cmdExecutor);
		getCommand("od").setExecutor(cmdExecutor);
		setupPermissions();

		config.loadConfig();
		entityListener.setObsidianDurability(config.loadDurabilityFromFile());

		getServer().getPluginManager().registerEvents(entityListener, this);
	}

	/**
	 * Returns the version of the plugin.
	 * 
	 * @return the version of the plugin
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * Returns the name of the plugin.
	 * 
	 * @return the name of the plugin
	 */
	public static String getPluginName() {
		return PLUGIN_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getPluginName();
	}

	/**
	 * Returns the config of this plugin.
	 * 
	 * @return the config of this plugin
	 */
	public ODConfig getODConfig() {
		return config;
	}

	/**
	 * Returns the entity listener of this plugin.
	 * 
	 * @return the entity listener of this plugin
	 */
	public ODEntityListener getListener() {
		return entityListener;
	}

	/**
	 * Returns whether the permissions plugin could be found.
	 * 
	 * @return true if permissions plugin could be found, otherwise false
	 */
	public boolean getPermissionsFound() {
		return permissionsFound;
	}

	/**
	 * Returns the permissionsHandler of this plugin if it exists.
	 * 
	 * @return the permissionsHandler of this plugin if it exists, otherwise
	 *         null
	 */
	public Permission getPermissionsHandler() {
		Permission ph = null;

		if (getPermissionsFound()) {
			ph = permissionsHandler;
		}

		return ph;
	}

	private boolean setupPermissions() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			permissionsFound = false;
			return false;
		}
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		permissionsHandler = rsp.getProvider();
		return permissionsHandler != null;
	}

	/**
	 * Method that handles what gets reloaded
	 * 
	 * @return true if everything loaded properly, otherwise false
	 */
	public boolean reload() {
		return config.loadConfig();
	}
}
