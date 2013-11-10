package org.futuredev.workbench;

import org.futuredev.workbench.localisation.MessageAssist;
import org.bukkit.command.CommandSender;

public abstract class Session {

    String holder;
    MessageAssist language;

    public Session (String holder) {
        this.holder = holder;
    }

    public abstract CommandSender getSender();
    public abstract boolean hasPermission (String... permissions);

    public void print (String... messages) {
        for (String message : messages) {
            getSender().sendMessage(message);
        }
    }

    public MessageAssist getLanguage () {
        return language;
    }

}