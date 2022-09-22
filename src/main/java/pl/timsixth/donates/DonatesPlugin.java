package pl.timsixth.donates;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.donates.command.DonateCommand;
import pl.timsixth.donates.config.ConfigFile;
import pl.timsixth.donates.manager.DonateManager;
import pl.timsixth.donates.tabcomplter.DonateCommandTabCompleter;

import java.util.logging.Logger;

public final class DonatesPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        if (!initEconomy()) {
            LOGGER.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigFile configFile = new ConfigFile(this);
        DonateManager donateManager = new DonateManager(this,configFile);
        donateManager.load();

        getCommand("donate").setExecutor(new DonateCommand(configFile,this,donateManager));
        getCommand("donate").setTabCompleter(new DonateCommandTabCompleter());
    }

    private boolean initEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            return false;
        }
        economy = registeredServiceProvider.getProvider();
        return economy != null;
    }
}
