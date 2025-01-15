package org.zmdblocker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.zmdblocker.listener.CommandListener;
import org.zmdblocker.utils.ColorStrings;

public final class ZmdBlocker extends JavaPlugin {

    public static FileConfiguration config;
    private CommandListener commandBlocker;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        getLogger().info(ColorStrings.CYAN + "_____________________________________________" + ColorStrings.RESET);
        getLogger().info("");
        getLogger().info("");
        getLogger().info(ColorStrings.BLUE + ColorStrings.BOLD + "              ZmdBlocker" + ColorStrings.RESET);
        getLogger().info("");
        getLogger().info(ColorStrings.GREEN + "Author: " + ColorStrings.RESET + "Zeniqzs");
        getLogger().info("");
        getLogger().info(ColorStrings.GREEN + "Website: " + ColorStrings.RESET + "https://discord.zeniqzs.eu/");
        getLogger().info("");
        getLogger().info(ColorStrings.GREEN + "Plugin Version: " + ColorStrings.RESET + "1.0");
        getLogger().info("");
        getLogger().info("");
        getLogger().info(ColorStrings.CYAN + "_____________________________________________" + ColorStrings.RESET);

        getServer().getPluginManager().registerEvents(new CommandListener(), this);



    }

    @Override
    public void onDisable() {

        if (commandBlocker != null) {
            commandBlocker.removeUnusedPermissions();
        }

    }
}
