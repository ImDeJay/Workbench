package org.futuredev.workbench.command.reflective;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.futuredev.workbench.Session;
import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.doc.DynamicDocumentation;

import java.util.Arrays;
import java.util.List;

public abstract class DynamicCommand extends Command {

    String title, description;
    DynamicDocumentation docs;
    List<String> aliases;

    public DynamicCommand () {
        this(null, null);
    }

    public DynamicCommand (String title, String desc, String... aliases) {
        super(title, desc, "/" + title, Arrays.asList(aliases));
    }

    abstract void handle (Session user, Arguments args) throws CommandException;

    abstract void document (Session user);

    public abstract void inject ();

    public boolean execute (CommandSender sender, String label, String[] args) {


        return false;
    }

    public DynamicCommand setUsage (DynamicDocumentation usage) {
        this.docs = usage;
        return this;
    }

    public DynamicCommand setAliases (String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

}