package pl.timsixth.donates.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.timsixth.donates.DonatesPlugin;
import pl.timsixth.donates.util.ChatUtil;

import java.io.File;
import java.util.List;

@Getter
public class ConfigFile {

    @Getter(value = AccessLevel.NONE)
    private final DonatesPlugin donatesPlugin;
    private final YamlConfiguration ymlDonates;
    private final File donatesFile;
    private String permission;
    private String dontHavePermission;

    private List<String> listOfCommands;

    private String playerIsOffline;

    private String isNotANumber;
    private String dontHaveMoney;

    private String receiveDonate;

    private String sendDonate;

    private String donatesListIsEmpty;

    private String numberMustGreaterThenZero;

    private String topPlayers;
    private String filesReloaded;
    private int maxTopPlayers;
    private String adminPermission;
    private String correctUseAdminCommand;


    public ConfigFile(DonatesPlugin donatesPlugin) {
        this.donatesPlugin = donatesPlugin;
        createFileByBukkit("donates.yml");
        donatesFile = new File(donatesPlugin.getDataFolder(), "donates.yml");
        this.ymlDonates = YamlConfiguration.loadConfiguration(donatesFile);
        loadConfig();
    }

    private void createFileByBukkit(String name) {
        if (!donatesPlugin.getDataFolder().exists()) {
            donatesPlugin.getDataFolder().mkdir();
        }
        File file = new File(donatesPlugin.getDataFolder(), name);
        if (!file.exists()) {
            donatesPlugin.saveResource(name, true);
        }
    }

    private void loadConfig() {
        this.permission = donatesPlugin.getConfig().getString("permission");
        this.adminPermission = donatesPlugin.getConfig().getString("admin_permission");
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
        this.filesReloaded = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.files_reloaded"));
        this.correctUseAdminCommand = ChatUtil.chatColor(donatesPlugin.getConfig().getString("messages.correct_use_admin_command"));
    }

    public void reloadConfig() {
        donatesPlugin.reloadConfig();
        loadConfig();
    }
}
