package org.futuredev.workbench;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.futuredev.workbench.command.reflective.DynamicCommand;

/**
 * Represents a class that handles commands for the server.
 *
 * @author afistofirony
 */
public class CommandManager implements Listener, CommandExecutor {

    private final CommandMap commandMap;

    public CommandManager () {
        commandMap = new CommandMap();
    }

    public CommandMap getMap () {
        return commandMap;
    }

    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent command) {
        command.setCancelled(true);
    }

    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args) {
        return this.handle(null, label, args);
    }

    public boolean injectionSuccessful () {
        return getMap().isInjected();
    }


    public boolean handle (Session user, String label, String[] array) {
        DynamicCommand command = this.commandMap.getCommand(label);
        try {
          //  Arguments args = CommandProcessor.buildArgs(command, array);
            if (false) {
                DynamicCommand docs = this.commandMap.getCommandDocs(label);
                if (docs == null) {
                    user.print(ChatColor.RED + "Sorry, no extra documentation available.");
              } else {

                }

                return true;
            }

          // TODO  command.invoke(user, args, CommandProcessor.parse(args, command));
      } // catch (final CommandException e) {
        //    user.print(e.getMessage(user.getLanguage()));
      //}
    catch (final Throwable t) {
            user.print(ChatColor.RED + "Something went very wrong while trying to process your request! :(",
                    ChatColor.RED + "Please report this issue to an administrator or developer: ",
                    ChatColor.GREEN + t.getClass().getName() + ": " + t.getMessage());
        }


        return true;
    }

}