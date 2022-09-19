package pl.timsixth.donates.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Getter
public class Donate {

    private final UUID receiverUUID;
    private final Map<UUID, Double> donors;

    public Donate(UUID receiverUUID) {
        this.receiverUUID = receiverUUID;
        donors = new HashMap<>();
    }

    public boolean firstDonateForPlayer(UUID senderUuid){
        return !donors.containsKey(senderUuid);
    }
}
