package pl.timsixth.donates.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.donates.DonatesPlugin;
import pl.timsixth.donates.util.ChatUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class ConfigFile {

    private final DonatesPlugin donatesPlugin;
    private final YamlConfiguration ymlDonates;
    private final File donatesFile;
    private final String permission;
    private final String dontHavePermission;

    private final List<String> listOfCommands;

    private final String playerIsOffline;

    private final String isNotANumber;
    private final String dontHaveMoney;

    private final String receiveDonate;

    private final String sendDonate;

    private final String donatesListIsEmpty;

    private final String numberMustGreaterThenZero;

    private final String topPlayers;
    private final int maxTopPlayers;

    public ConfigFile(DonatesPlugin donatesPlugin) {
        this.donatesPlugin = donatesPlugin;
        createFileByBukkit("donates.yml");
        donatesFile = new File(donatesPlugin.getDataFolder(), "donates.yml");
        this.ymlDonates = YamlConfiguration.loadConfiguration(donatesFile);
        this.permission = donatesPlugin.getConfig().getString("permission");
        this.maxTopPlayers = donatesPlugin.getConfig().getInt("max_top_players");
        this.dontHavePermission = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.dont_have_permission"));
        this.listOfCommands = ChatUtil.chatColor(donatesPlugin.getConfig().getStringList("messages.list_of_command"));
        this.playerIsOffline = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.player_is_offline"));
        this.isNotANumber = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.is_not_number"));
        this.dontHaveMoney = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.dont_have_money"));
        this.receiveDonate = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.receive_donate"));
        this.sendDonate = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.send_donate"));
        this.donatesListIsEmpty = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.empty_list"));
        this.numberMustGreaterThenZero = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.number_must_be_greater_then_zero"));
        this.topPlayers = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.top_players"));
    }
    private void createFileByBukkit(String name) {
        if (!donatesPlugin.getDataFolder().exists()) {
            donatesPlugin.getDataFolder().mkdir();
        }
        File file = new File(donatesPlugin.getDataFolder(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
                donatesPlugin.saveResource(name, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
