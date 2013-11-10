package org.futuredev.workbench.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Pretty much a var-args identifier.
 *
 * Can be combined with the @Range
 * annotation to define only a specific
 * subset of arguments. Otherwise, this
 * will compile all subsequent arguments.
 *
 * NOTE: The effect of this setting can be
 * achieved manually by a user by using
 * quotes around the desired arguments.
 *
 * @author afistofirony
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Compound {}
