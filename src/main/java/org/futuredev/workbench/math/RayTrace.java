package org.futuredev.workbench.math;

import org.bukkit.block.Block;
import org.futuredev.workbench.command.CommandException;
import org.futuredev.workbench.command.reflective.Resolvable;
import org.futuredev.workbench.command.reflective.builder.ParametricCompiler;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used for basic ray tracing.
 *
 * @author afistofirony
 */
public class RayTrace implements Iterator<Vector>, Resolvable<Block> {

    int distance, index;
    Vector start, dir, current;
    ArrayList<Vector> matches;

    public RayTrace (Vector start, Vector dir, int distance) {

        this.distance = distance;
        this.index    = 0;
        this.start    = start;
        this.dir      = dir;
        this.matches  = new ArrayList<Vector>();

    }

    public RayTrace (RemoteZeroVector vector, int distance) {
        this (vector, vector.getDirection(), distance);
    }

    public Vector vectorAt (int index) {
        return matches.get(index);
    }

    public Vector next () {
        do {
            current.add(dir.getX(), dir.getY(), dir.getZ());
      } while (current.floor().equals(matches.get(matches.size() - 1).floor()) && index <= distance);

        ++index;
        matches.add(current);
        return current;
    }

    public void remove () {
        throw new UnsupportedOperationException("Removal is not supported");
    }

    public boolean hasNext () {
        return index <= distance;
    }

    public Block resolve (ParametricCompiler data, String value) throws CommandException {
        return null;
    }

}