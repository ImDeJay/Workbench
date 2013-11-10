package org.futuredev.workbench.command.annotation;

/**
 * Represents a command which is purely a link to another
 * command.
 *
 * For example:
 *
 * * @Command (aliases = {"sun"}, console = false,
 *             info = "Brings out the sun.")
 * * @CommandAlias ("weather sun %args%")
 *   public void sun (Session user, Arguments args) {}
 *
 * means that when a player uses /sun, they will actually be sending /weather sun.
 * Handy, right?
 *
 *
 * @author afistofirony
 */
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention (RetentionPolicy.RUNTIME)
public @interface CommandAlias {

    String value ();

}
