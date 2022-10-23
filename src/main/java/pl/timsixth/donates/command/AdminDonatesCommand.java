package pl.timsixth.donates.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.timsixth.donates.config.ConfigFile;

@RequiredArgsConstructor
public class AdminDonatesCommand implements CommandExecutor {

    private final ConfigFile configFile;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(configFile.getAdminPermission())) {
            sender.sendMessage(configFile.getDontHavePermission());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(configFile.getCorrectUseAdminCommand());
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                configFile.reloadConfig();
                sender.sendMessage(configFile.getFilesReloaded());
            }
        } else {
            sender.sendMessage(configFile.getCorrectUseAdminCommand());
        }
        return false;
    }
}
