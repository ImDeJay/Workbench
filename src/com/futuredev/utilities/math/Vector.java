package com.futuredev.utilities.math;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Vector {

    double x, y, z;
    boolean mutable;

    /**
     * Constructs a vector with the given components.
     * @param x The x-component.
     * @param y The y-component.
     * @param z The z-component.
     * @param mutable Whether or not methods will return the same vector or new copies.
     */
    public Vector (double x, double y, double z, boolean mutable) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mutable = mutable;
    }

    public Vector (double x, double y, double z) {
        this (x, y, z, true);
    }

    public double getX () { return this.x; }
    public double getY () { return this.y; }
    public double getZ () { return this.z; }

    public int getBlockX () { return (int) this.x; }
    public int getBlockY () { return (int) this.y; }
    public int getBlockZ () { return (int) this.z; }

    /**
     * Adds another vector to this one.
     * @param vec The vector to add to this.
     * @return This, for stacking.
     */
    public Vector add (Vector vec) {
        return this.add(vec.x, vec.y, vec.z);
    }

    /**
     * Adds the given doubles to the Vector.
     * @param x The value to add to x.
     * @param y The value to add to y.
     * @param z The value to add to z.
     * @return This, for stacking.
     */
    public Vector add (double x, double y, double z) {
        if (mutable) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }

        return new Vector(this.x + x, this.y + y, this.z + z, false);
    }

    /**
     * Subtracts a vector from this one.
     * @param vec The vector to subtract.
     * @return This, for stacking.
     */
    public Vector subtract (Vector vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    /**
     * Subtracts the given doubles from the Vector.
     * @param x The value to subtract from x.
     * @param y The value to subtract from y.
     * @param z The value to subtract from z.
     * @return This, for stacking.
     */
    public Vector subtract (double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    public Vector multiply (double by) {
        return this.multiply(new Vector(by, by, by));
    }

    public Vector multiply (Vector other) {
        if (mutable) {
            this.x *= other.getX();
            this.y *= other.getY();
            this.z *= other.getZ();
            return this;
        }

        return new Vector(this.x * other.getX(), this.y * other.getY(), this.z * other.getZ());
    }

    public Vector divide (double by) {
        if (mutable) {
            this.x /= by;
            this.y /= by;
            this.z /= by;
            return this;
        }

        return new Vector(this.x / by, this.y / by, this.z / by);
    }

    public double length () {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public Vector normalise () {
        return divide(length());
    }

    public Vector invert () {
        return multiply(-1);
    }

    public boolean isAxicollinear (Vector other) {
        boolean x = this.getX() == other.getX();
        boolean y = this.getY() == other.getY();
        boolean z = this.getZ() == other.getZ();

        return (x && (y || z)) || (y && z);
    }

    public boolean isCollinearWith (Vector other) {
        if (this.isZero() || other.isZero()) return true;

        double x2 = other.x;
        double y2 = other.y;
        double z2 = other.z;

        if ((x == 0) != (x2 == 0) || (y == 0) != (y2 == 0) || (z == 0) != (z2 == 0))
            return false;

        double x3 = x2 / x;
        if (!Double.isNaN(x3))
            return other.equals(this.multiply(x3));

        double y3 = y2 / y;
        if (!Double.isNaN(y3))
            return other.equals(this.multiply(y3));

        double z3 = z2 / z;
        return !Double.isNaN(z3) && other.equals(this.multiply(z3));
    }

    public double distanceTo (Vector other) {
        return Math.abs(other.length() - this.length());
    }

    public boolean isZero () {
        return (x == 0) && (y == 0) && (z == 0);
    }

    public Vector offset (Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector absoluteOffset (Vector other) {
        return new Vector(Math.abs(x - other.x), Math.abs(y - other.y), Math.abs(z - other.z));
    }

    public RemoteZeroVector toRemoteVector () {
        return new RemoteZeroVector(this);
    }

    public boolean greaterThan (Vector other) {
        return this.x > other.x && this.y > other.y && this.z > other.z;
    }

    public boolean lessThan (Vector other) {
        return this.x < other.x && this.y < other.y && this.z < other.z;
    }

    public boolean isWithin (Vector a, Vector b) {
        return (greaterThan(a) && lessThan(b)) || (lessThan(a) && greaterThan(b));
    }

    public boolean equals (Object other) {
        if (other instanceof Vector) {
            Vector vec = (Vector) other;

            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        }

        return false;
    }

    public Location asLocation (String world) {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Vector clone () {
        return new Vector(x, y, z);
    }

}