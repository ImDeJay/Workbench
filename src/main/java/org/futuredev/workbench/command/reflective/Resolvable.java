package org.futuredev.workbench.command.reflective;

import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.reflective.builder.ParametricCompiler;

/**
 * Represents a resolvable class.
 * A class can implement this for
 * the parametric compiler to automatically
 * detect and resolve objects based on
 * a @Default string.
 *
 * For example, let's say this method resolves a string into a
 * Vector a certain distance from the player along his line of
 * sight.
 *
 * public class Vector implements Resolvable<Vector> {
 *
 *     Vector resolve (ParametricCompiler data, String value) {
 *         if (...) {
 *            ...;
 *         }
 *     }
 * }
 *
 * Now, this class can be used with the parametric compiler:
 *
 * public void example (Session user, Arguments args, @Default("->5") Vector target) {
 *
 * }
 *
 * @author afistofirony
 *
 * @param <T> The data type this method will return.
 */
public interface Resolvable<T> {

    /**
     * Resolves a class.
     * @param data
     * @param value
     * @return
     * @throws CommandException
     */
    T resolve (ParametricCompiler data, String value) throws CommandException;

}