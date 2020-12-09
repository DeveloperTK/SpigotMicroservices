package de.christianschliz.spigotms.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MicroserviceCommand implements CommandExecutor {

    private final SpigotMS pluginInstance;

    public MicroserviceCommand(SpigotMS instance) {
        this.pluginInstance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("list")
                && commandSender.hasPermission("spigotms.list")) {
            StringBuilder list = new StringBuilder();
            this.pluginInstance.getServiceManager().getServices().forEach((name, service) -> {
                list.append("\n[SpigotMS] ").append(name).append("; loaded: true; enabled: ").append(service.isEnabled());
            });
            commandSender.sendMessage(list.toString());

            return true;
        } else if (args.length > 1 && args[0].equalsIgnoreCase("enable")
                && commandSender.hasPermission("spigotms.manage")) {
            if (this.pluginInstance.getServiceManager().getServices().containsKey(args[1])) {
                this.pluginInstance.getServiceManager().getServices().get(args[1]).doEnable();
                commandSender.sendMessage("[SpigotMS] Service " + args[1] + " enabled!");
            } else {
                commandSender.sendMessage("[SpigotMS] No such service: " + args[1]);
            }

            return true;
        } else if (args.length > 1 && args[0].equalsIgnoreCase("disable")
                && commandSender.hasPermission("spigotms.manage")) {
            if (this.pluginInstance.getServiceManager().getServices().containsKey(args[1])) {
                this.pluginInstance.getServiceManager().getServices().get(args[1]).doDisable();
                commandSender.sendMessage("[SpigotMS] Service " + args[1] + " disabled!");
            } else {
                commandSender.sendMessage("[SpigotMS] No such service: " + args[1]);
            }

            return true;
        } else if (args.length > 1 && args[0].equalsIgnoreCase("reload")
                && commandSender.hasPermission("spigotms.manage")) {
            commandSender.sendMessage("[SpigotMS] disabling loaded services");
            this.pluginInstance.getServiceManager().disableLoadedServices();
            commandSender.sendMessage("[SpigotMS] loading services");
            this.pluginInstance.getServiceManager().loadServices();
            commandSender.sendMessage("[SpigotMS] enabling loaded services");
            this.pluginInstance.getServiceManager().enableLoadedServices();

            return true;
        } else return false;
    }
}
