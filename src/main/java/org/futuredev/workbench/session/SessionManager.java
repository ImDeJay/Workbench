package org.futuredev.workbench.session;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.futuredev.workbench.AcceleratedPlugin;

import java.util.HashMap;

/**
 * Represents a session manager.
 *
 * @author afistofirony
 */
public class SessionManager implements Listener {

    HashMap<String, Session> sessions = new HashMap<String, Session>();
    PlayerSession template = new PlayerSession(null);

    public SessionManager (AcceleratedPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    public Session getSession (String name) {
        return this.sessions.get(name);
    }

    public SessionManager setTemplate (PlayerSession template) {
        this.template = template;
        return this;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        if (!this.sessions.containsKey(event.getPlayer().getName()))
            sessions.put(event.getPlayer().getName(),
                    template.clone().setName(event.getPlayer().getName()));
    }

}