package org.futuredev.workbench.session;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@InGame
public class PlayerSession extends Session {

    public PlayerSession (String holder) {
        super(holder);
    }

    public final Player getSender () { return Bukkit.getPlayerExact(holder); }

    public final boolean hasPermission (String... permissions) {
        for (String permission : permissions) {
            if (getSender().hasPermission(permission))
                return true;
        }

        return false;
    }

    public String getName () {
        return holder;
    }

    public PlayerSession clone () {
        return new PlayerSession(holder);
    }

}