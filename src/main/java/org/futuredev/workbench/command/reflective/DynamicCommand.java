package org.futuredev.workbench.command.reflective;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.futuredev.workbench.AcceleratedPlugin;
import org.futuredev.workbench.command.reflective.builder.ParametricRequirements;
import org.futuredev.workbench.session.Session;
import org.futuredev.workbench.command.Arguments;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.doc.DynamicDocumentation;

import java.util.Arrays;
import java.util.List;

public abstract class DynamicCommand extends Command {

    boolean deprecated;
    String title, description;
    DynamicDocumentation docs;
    ParametricRequirements defaultArgs;
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

    // TODO: Move
    public boolean execute (CommandSender sender, String label, String[] args) {
        if (this.deprecated) {
            sender.sendMessage("Unknown command. Use \"" + (sender instanceof ConsoleCommandSender ? "" : "/")
                    + "help\" for help.");
            return true;
        }

        try {
            Session session = AcceleratedPlugin.getInstance().getSessions().getSession(sender.getName());
            this.handle(session, defaultArgs.fulfill(session, args));
      } catch (final CommandException e) {

        }

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