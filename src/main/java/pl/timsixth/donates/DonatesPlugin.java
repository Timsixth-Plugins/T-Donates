package pl.timsixth.donates;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.timsixth.donates.command.AdminDonatesCommand;
import pl.timsixth.donates.command.DonateCommand;
import pl.timsixth.donates.config.ConfigFile;
import pl.timsixth.donates.expansion.DonatesExpansion;
import pl.timsixth.donates.manager.DonateManager;
import pl.timsixth.donates.tabcompleter.AdminDonatesTabCompleter;
import pl.timsixth.donates.tabcompleter.DonateCommandTabCompleter;

import java.util.logging.Logger;

public final class DonatesPlugin extends JavaPlugin{

    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    @Getter
    private Economy economy;
    private DonateManager donateManager;

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
        donateManager = new DonateManager(this,configFile);
        donateManager.load();
        registerCommands(configFile);
        registerTabcompleters();

        if (!initPlaceHolderApi()){
            LOGGER.warning("Please download PlaceholderAPI, if you want to use placeholders.");
        }
    }

    private void registerTabcompleters() {
        getCommand("donate").setTabCompleter(new DonateCommandTabCompleter());
        getCommand("adonates").setTabCompleter(new AdminDonatesTabCompleter());
    }

    private void registerCommands(ConfigFile configFile) {
        getCommand("donate").setExecutor(new DonateCommand(configFile,this,donateManager));
        getCommand("adonates").setExecutor(new AdminDonatesCommand(configFile));
    }

    private boolean initPlaceHolderApi(){
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null){
            return false;
        }
        new DonatesExpansion(donateManager).register();

        return true;
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
