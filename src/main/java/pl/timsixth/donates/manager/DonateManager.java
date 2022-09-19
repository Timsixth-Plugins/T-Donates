package pl.timsixth.donates.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.timsixth.donates.DonatesPlugin;
import pl.timsixth.donates.config.ConfigFile;
import pl.timsixth.donates.model.Donate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DonateManager {

    private final DonatesPlugin donatesPlugin;
    private final ConfigFile configFile;

    private final List<Donate> donateList = new ArrayList<>();

    public void load() {
        if (configFile.getYmlDonates().getConfigurationSection("donates") == null) {
            configFile.getYmlDonates().createSection("donates");
            return;
        }
        ConfigurationSection donatesSection = configFile.getYmlDonates().getConfigurationSection("donates");
        donatesSection.getKeys(false).forEach(sectionName -> {
            ConfigurationSection receiverUuid = donatesSection.getConfigurationSection(sectionName);
            Donate donate = new Donate(UUID.fromString(sectionName));
            receiverUuid.getKeys(false).forEach(senderUuid -> {
                ConfigurationSection senderUuidSection = receiverUuid.getConfigurationSection(senderUuid);
                donate.getDonors().put(UUID.fromString(senderUuid), senderUuidSection.getDouble("total_money"));
            });
            donateList.add(donate);
        });
    }

    public void donate(Player receiver, Player sender, double coins) throws IOException {
        ConfigurationSection donatesSection = configFile.getYmlDonates().getConfigurationSection("donates");
        Optional<Donate> donateOptional = getDonatesByReceiver(receiver.getUniqueId());
        if (!donateOptional.isPresent()) {
            Donate donate = new Donate(receiver.getUniqueId());
            donate.getDonors().put(sender.getUniqueId(), coins);
            ConfigurationSection receiverUuid = donatesSection.createSection(String.valueOf(receiver.getUniqueId()));
            ConfigurationSection senderUuid = receiverUuid.createSection(String.valueOf(sender.getUniqueId()));
            senderUuid.set("total_money", coins);
            configFile.getYmlDonates().save(configFile.getDonatesFile());
            donateList.add(donate);
        } else {
            Donate donate = donateOptional.get();
            ConfigurationSection receiverUuid = donatesSection.getConfigurationSection(String.valueOf(donate.getReceiverUUID()));
            if (donate.firstDonateForPlayer(sender.getUniqueId())) {
                ConfigurationSection senderUuid = receiverUuid.createSection(String.valueOf(sender.getUniqueId()));
                senderUuid.set("total_money", coins);
                configFile.getYmlDonates().save(configFile.getDonatesFile());
                donate.getDonors().put(sender.getUniqueId(), coins);
            } else {
                double moneyInMemory = donate.getDonors().get(sender.getUniqueId());
                moneyInMemory += coins;
                donate.getDonors().replace(sender.getUniqueId(),moneyInMemory);
                ConfigurationSection senderUuid = receiverUuid.getConfigurationSection(String.valueOf(sender.getUniqueId()));
                double totalMoney = senderUuid.getDouble("total_money");
                totalMoney += coins;
                senderUuid.set("total_money", totalMoney);
                configFile.getYmlDonates().save(configFile.getDonatesFile());
            }
        }
        donatesPlugin.getEconomy().withdrawPlayer(sender,coins);
        donatesPlugin.getEconomy().depositPlayer(receiver, coins);
    }
    public Optional<Donate> getDonatesByReceiver(UUID uuid) {
        return donateList.stream()
                .filter(donate -> donate.getReceiverUUID().equals(uuid))
                .findAny();
    }

}
