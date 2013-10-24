package com.futuredev.utilities.math;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;

/**
 * This class is used for basic ray tracing.
 *
 * @author afistofirony
 */
public class RayTrace {

    int distance;
    Vector start, dir;
    ArrayList<Vector> matches;


    public RayTrace (Vector start, Vector dir, int distance) {

        this.distance = distance;
        this.start    = start;
        this.dir      = dir;
        this.matches  = new ArrayList<Vector>();

        for (int i = 0; i <= distance; ++i) {
            matches.add(new Vector(dir.x * i, dir.y * i, dir.z * i));
        }

    }

    public RayTrace (RemoteZeroVector vector, int distance) {
        this (vector, vector.getDirection(), distance);
    }

    public Vector vectorAt (int index) {
        return matches.get(index);
    }

    public Vector firstBlock (World world) {
        for (Vector vec : matches) {
            Block block = world.getBlockAt(vec.asLocation(world.getName()));
            if (block.getType() != Material.AIR)
                return vec;
        }

        return null;
    }

    public Vector firstSpace (World world) {
        for (int i = 0; i < matches.size(); ++i) {
            Vector vec = matches.get(i);
            Block block = world.getBlockAt(vec.asLocation(world.getName()));
            if (block.getType() != Material.AIR && i < 0)
                return vectorAt(i - 1);
        }

        return null;
    }

}