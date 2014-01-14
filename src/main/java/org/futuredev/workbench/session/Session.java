package org.futuredev.workbench.session;

import org.bukkit.entity.Player;
import org.futuredev.workbench.localisation.MessageAssist;
import org.bukkit.command.CommandSender;
import org.futuredev.workbench.localisation.json.SpecialMessage;

public abstract class Session {

    String holder;
    MessageAssist language;

    public Session (String holder) {
        this.holder = holder;
    }

    protected Session setName (String name) {
        this.holder = name;
        return this;
    }

    public abstract CommandSender getSender();
    public abstract boolean hasPermission (String... permissions);
    public abstract String getName ();

    public void print (String... messages) {
        for (String message : messages) {
            getSender().sendMessage(message);
        }
    }

    public void printSpecial (String... messages) {
        if (getSender() instanceof Player)
            for (String message : messages)
                ((Player) getSender()).sendRawMessage(new SpecialMessage(message).toString());

    }

    public MessageAssist getLanguage () {
        return language;
    }

    public String toString () { return getName(); }

}