package life.grass.grassitem;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrassItemCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || !sender.isOp() || args.length <= 0 || !"grassitem".equalsIgnoreCase(command.getName()))
            return false;

        switch (args[0].toUpperCase()) {
            case "RELOAD":
                JsonBucket.getInstance().refillBucket();
                sender.sendMessage(ChatColor.GREEN + "Reloaded.");
                return true;
            case "GET":
                Player player = (Player) sender;
                if(ItemBuilder.buildByConfigString(args[1]) != null) {
                    player.getInventory().addItem(ItemBuilder.buildByConfigString(args[1]));
                    return true;
                }
            default:
                return false;
        }
    }
}
