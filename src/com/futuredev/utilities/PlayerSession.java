package com.futuredev.utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerSession extends Session {

    public PlayerSession (String holder) {
        super(holder);
    }

    public Player getSender () { return Bukkit.getPlayerExact(holder); }

    public boolean hasPermission (String... permissions) {
        for (String permission : permissions) {
            if (getSender().hasPermission(permission))
                return true;
        }

        return false;
    }

}