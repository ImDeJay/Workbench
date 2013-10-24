package com.futuredev.utilities.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInputStream implements Listener {

    String player;
    InputListening listening;
    String next = null;
    boolean active = true;

    public PlayerInputStream (JavaPlugin master, Player player, InputListening mode) {
        this.player = player.getName();
        this.listening = mode;
        Bukkit.getServer().getPluginManager().registerEvents(this, master);
    }

    /**
     * Waits for the next command or chat message matching the specified
     * input. Optionally supports a regex match, which returns the next
     * String matching the given expression.
     * @param match An optional regular expression to match to. Ignore-case.
     * @return The next string (matching the given expression if available).
     */
    public synchronized String next (String... match) {
        new ListeningThread().run(); // This cannot complete until next != null

        if (next == null)
            throw new NullPointerException("Am I missing something here? Next cannot be null... :/");

        if (match != null && !next.matches("(i?)" + match[0]))
            return this.next();  // Not the droid we're looking for.

        return this.next;
    }

    public int nextInt () { return Integer.parseInt(this.next("[0-9]+")); }
    public double nextDouble () { return Double.parseDouble(this.next("([0-9]+|([0-9])?.[0-9]+)")); }
    public char nextChar () { return this.next("[.]").charAt(0); }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChat (AsyncPlayerChatEvent event) {
        if (active && this.listening != InputListening.COMMANDS && event.getPlayer().getName().equals(player))
            this.next = event.getMessage();
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCommand (PlayerCommandPreprocessEvent event) {
        if (active && this.listening != InputListening.CHAT  && event.getPlayer().getName().equals(player))
            this.next = event.getMessage();
    }

    /**
     * The dumbest class ever.
     *
     * @author afistofirony
     */
    private class ListeningThread extends Thread {

        public void run () {
            while (next == null) {}
        }

    }

    public void close () {
        this.active = false;
    }

    private static class Test implements Runnable {
        public void run () {

        }
    }

}