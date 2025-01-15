//Just a small Plugin for fun


package org.zmdblocker.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.zmdblocker.ZmdBlocker;
import org.zmdblocker.utils.ColorUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandListener implements Listener {

    public void removeUnusedPermissions() {
        List<String> blockedCommands = ZmdBlocker.config.getStringList("CommandBlocker.Commands");
        Set<String> activePermissions = blockedCommands.stream()
                .map(cmd -> "zmdblocker.allow." + cmd)
                .collect(Collectors.toSet());

        JavaPlugin.getPlugin(ZmdBlocker.class).getServer().getPluginManager().getPermissions().stream()
                .filter(permission -> permission.getName().startsWith("zmdblocker.allow.") && !activePermissions.contains(permission.getName()))
                .forEach(permission -> JavaPlugin.getPlugin(ZmdBlocker.class).getServer().getPluginManager().removePermission(permission));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getMessage().startsWith("/")) {

            boolean colonsAllowed = ZmdBlocker.config.getBoolean("CommandBlocker.ColonsAllowed", true);
            boolean containsColonOrSemicolon = event.getMessage().substring(1).split(" ")[0].contains(":") || event.getMessage().substring(1).split(" ")[0].contains(";");

            if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission("zmdblocker.allow.*")) {
                if ((!colonsAllowed && containsColonOrSemicolon) ||
                        (ZmdBlocker.config.getStringList("CommandBlocker.Commands").contains(event.getMessage().substring(1).split(" ")[0]) &&
                                !event.getPlayer().hasPermission("zmdblocker.allow." + event.getMessage().substring(1).split(" ")[0]))) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ColorUtil.parseMessage(ZmdBlocker.config.getString("Messages.BlockedCommands")));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().isOp() || event.getPlayer().hasPermission("zencore.allow.*")) {
            return;
        }

        event.getCommands().removeIf(command ->
                (!ZmdBlocker.config.getBoolean("CommandBlocker.ColonsAllowed", true) && (command.contains(":") || command.contains(";"))) ||
                        (ZmdBlocker.config.getStringList("CommandBlocker.Commands").contains(command) && !event.getPlayer().hasPermission("zmdblocker.allow." + command))
        );
    }
}
