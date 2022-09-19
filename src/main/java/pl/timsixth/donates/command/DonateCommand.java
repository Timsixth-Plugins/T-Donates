package pl.timsixth.donates.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.timsixth.donates.DonatesPlugin;
import pl.timsixth.donates.config.ConfigFile;
import pl.timsixth.donates.manager.DonateManager;
import pl.timsixth.donates.model.Donate;
import pl.timsixth.donates.util.ChatUtil;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class DonateCommand implements CommandExecutor {

    private final ConfigFile configFile;
    private final DonatesPlugin donatesPlugin;

    private final DonateManager donateManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(configFile.getPermission())) {
            sender.sendMessage(configFile.getDontHavePermission());
            return true;
        }
        if (!(sender instanceof Player)) {
            System.out.println("Only players can use this command");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            configFile.getListOfCommands().forEach(player::sendMessage);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("top")) {
                Optional<Donate> donateOptional = donateManager.getDonatesByReceiver(player.getUniqueId());
                if (!donateOptional.isPresent()) {
                    player.sendMessage(configFile.getDonatesListIsEmpty());
                    return true;
                }
                Donate donate = donateOptional.get();

                Map<UUID, Double> topDonors = donate.getDonors().entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(configFile.getMaxTopPlayers())
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));

                player.sendMessage(configFile.getTopPlayers().replace("{MAX_TOP_PLAYERS}", String.valueOf(configFile.getMaxTopPlayers())));
                topDonors.forEach((uuid, coins) -> player.sendMessage(ChatUtil.chatColor("&a" + Bukkit.getOfflinePlayer(uuid).getName() + " &e " + coins)));
            }
        } else if (args.length == 2) {
            Player other = Bukkit.getPlayer(args[0]);
            if (other == null) {
                player.sendMessage(configFile.getPlayerIsOffline());
                return true;
            }
            if (!isNumber(args[1])) {
                player.sendMessage(configFile.getIsNotANumber());
                return true;
            }
            double coins = Double.parseDouble(args[1]);

            if (!donatesPlugin.getEconomy().has(other, coins)) {
                player.sendMessage(configFile.getDontHaveMoney());
                return true;
            }
            if (coins <= 0.01) {
                player.sendMessage(configFile.getNumberMustGreaterThenZero());
                return true;
            }
            try {
                donateManager.donate(other, player, coins);
                player.sendMessage(configFile.getSendDonate().replace("{RECEIVER_NAME}", other.getDisplayName()));
                other.sendMessage(configFile.getReceiveDonate().replace("{SENDER_NAME}", player.getDisplayName())
                        .replace("{COINS}", String.valueOf(coins)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isNumber(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
