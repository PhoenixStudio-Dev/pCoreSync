package com.phoenixstudio.pcoresync.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class PlayerData {
    private String discordName;
    private String discordId;

    public PlayerData(String discordName, String discordId) {
        this.discordName = discordName;
        this.discordId = discordId;
    }
}
