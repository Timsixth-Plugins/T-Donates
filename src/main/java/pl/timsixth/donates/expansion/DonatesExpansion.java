package pl.timsixth.donates.expansion;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.timsixth.donates.manager.DonateManager;
import pl.timsixth.donates.model.Donate;

import java.util.Optional;

@RequiredArgsConstructor
public class DonatesExpansion extends PlaceholderExpansion {

    private final DonateManager donateManager;

    @Override
    public @NotNull String getIdentifier() {
        return "t-donates";
    }

    @Override
    public @NotNull String getAuthor() {
        return "timsixth";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("number_of_donations")) {
            Optional<Donate> donateOptional = donateManager.getDonatesByReceiver(player.getUniqueId());
            if (!donateOptional.isPresent()) return null;
            Donate donate = donateOptional.get();

            return String.valueOf(donate.numberOfDonations());
        } else if (params.equalsIgnoreCase("total_donated_money")) {
            Optional<Donate> donateOptional = donateManager.getDonatesByReceiver(player.getUniqueId());
            if (!donateOptional.isPresent()) return null;
            Donate donate = donateOptional.get();

            return String.valueOf(donate.sumDonates());
        }

        return null;
    }
}
