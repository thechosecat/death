package cat.death.command;

import cat.death.event.GuiEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenGuiCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Bukkit.getServer().getPluginManager().callEvent(new GuiEvent(player));

            return true;
        }
        else
            sender.sendMessage("Only player can run this command.");

        return false;
    }
}