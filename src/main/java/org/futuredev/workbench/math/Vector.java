package org.futuredev.workbench.math;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.futuredev.workbench.localisation.StringHelper;

/**
 * Represents a point in a three-dimensional field.
 *
 * @author afistofirony
 */
public class Vector {

    double x, y, z;
    boolean mutable;

    /**
     * Constructs a vector with the given components.
     * @param x The x-component.
     * @param y The y-component.
     * @param z The z-component.
     * @param mutable Whether or not this vector is mutable.
     */
    public Vector (double x, double y, double z, boolean mutable) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.mutable = mutable;
    }

    /**
     * Shorthand constructor for a mutable vector.
     * @param x The x-component.
     * @param y The y-component.
     * @param z The z-component.
     */
    public Vector (double x, double y, double z) {
        this (x, y, z, true);
    }

    /**
     * I'm not sorry for one-line methods.
     * @return The x-component.
     */
    public double getX () { return this.x; }

    /**
     * @return The y-component.
     */
    public double getY () { return this.y; }

    /**
     * @return The z-component.
     */
    public double getZ () { return this.z; }

    /**
     * @return The x-component, rounded down to an integer value.
     */
    public int getBlockX () { return (int) this.x; }

    /**
     * @return The y-component, rounded down to an integer value.
     */
    public int getBlockY () { return (int) this.y; }

    /**
     * @return The z-component, rounded down to an integer value.
     */
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

        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Shifts all components by the given amount.
     * @param shift The amount to shift each component.
     * @return The shifted vector.
     */
    public Vector shift (double shift) {
        return this.add(shift, shift, shift);
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

    /**
     * Multiplies a vector.
     * @param by How many times to multiply the vector.
     * @return The multiplied vector.
     */
    public Vector multiply (double by) {
        return this.multiply(new Vector(by, by, by));
    }

    /**
     * Multiplies this vector by another vector.
     * @param other The vector to multiply by.
     * @return A multiplied vector.
     */
    public Vector multiply (Vector other) {
        if (mutable) {
            this.x *= other.getX();
            this.y *= other.getY();
            this.z *= other.getZ();
            return this;
        }

        return new Vector(this.x * other.getX(), this.y * other.getY(), this.z * other.getZ());
    }

    /**
     * Flips the coordinates of the vector.
     * @return An inverted vector.
     */
    public Vector invert () {
        return this.multiply(-1);
    }

    /**
     * Divides this vector by the given number using its reciprocal.
     * @param by The denominator for the division.
     * @return A divided vector.
     */
    public Vector divide (double by) {
        return this.multiply(1 / by);
    }

    /**
     * Gets the reciprocal of this vector.
     * @return A vector that will make each component of the
     * original vector equal 1 when multiplied with the original
     * vector.
     */
    public Vector reciprocal () {
        return new Vector(1 / this.x, 1 / this.y, 1 / this.z);
    }

    /**
     * Rounds this vector downward.
     * @return A floored vector.
     */
    public Vector floor () {
        if (mutable) {
            this.x = this.getBlockX();
            this.y = this.getBlockY();
            this.z = this.getBlockZ();
        }

        return new Vector(this.getBlockX(), this.getBlockY(),  this.getBlockZ(), false);
    }

    /**
     * Rounds this vector upward.
     * @return A ceil'd vector.
     */
    public Vector ceil () {
        return this.floor().add(1.0D, 1.0D, 1.0D);
    }

    /**
     * Gives the length of this vector by squaring its components.
     * @return The length of the vector.
     */
    public double length () {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * Normalises this vector.
     * @return A normalised vector.
     */
    public Vector normalise () {
        return divide(length());
    }

    /**
     * Edits the components so they are all positive.
     * @return A vector with absolute values of the components
     *         of the original.
     */
    public Vector absoluteValue () {
        if (mutable) {
            this.x = Math.abs(x);
            this.y = Math.abs(y);
            this.z = Math.abs(z);
        }

        return new Vector(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    /**
     * Whether or not this vector is axicollinear with another.
     *
     * Axicollinear: same x, y, or z value.
     *
     * @param other The Vector to compare to.
     * @return Whether or not this axicollinear.
     */
    public boolean isAxicollinear (Vector other) {
        boolean x = this.getX() == other.getX(),
                y = this.getY() == other.getY(),
                z = this.getZ() == other.getZ();

        return (x && (y || z)) || (y && z);
    }

    /**
     * Whether or not two vectors are on the same line.
     * @param other The other vector.
     * @return Whether or not two vectors are collinear.
     */
    public boolean isCollinearWith (Vector other) {
        if (this.isZero() || other.isZero()) return true;
        if (this.absoluteOffset(other).isZero()) return true;

        double x2 = other.x;
        double y2 = other.y;
        double z2 = other.z;

        if ((x == 0) != (x2 == 0) || (y == 0) != (y2 == 0) || (z == 0) != (z2 == 0))
            return false;

        double x3 = x2 / x;
        if (!Double.isNaN(x3))
            return other.equals(this.clone().multiply(x3));

        double y3 = y2 / y;
        if (!Double.isNaN(y3))
            return other.equals(this.clone().multiply(y3));

        double z3 = z2 / z;
        return !Double.isNaN(z3) && other.equals(this.clone().multiply(z3));
    }

    /**
     * Used to check if multiple vectors are collinear through the usage of
     * an inferred property of vector collinearity:
     *
     *   (equal sign means 'collinear')
     *
     *   Where a, b, and c are non-zero vectors:
     *     if a = b and b = c, then a = c.
     *   Where a is a zero vector, but b and c are not:
     *     if b = c, then a = c and b = c.
     *   Where a and b are zero vectors, but c is not:
     *     a = c and b = c.
     *   Where a, b, and c are zero vectors:
     *     a = c and b = c.
     *
     * @param others The other vectors.
     * @return Whether or not these vectors are collinear.
     */
    public boolean areCollinear (Vector... others) {
        switch (others.length) {
            case 0:
                return true;

            case 1:
                return this.isCollinearWith(others[0]);

            default:
                Vector b; // Root for comparison
                int index = 0;
                do { b = others[index]; } while (b.isZero() && index < others.length);

                if (others.length - index == 1) // All zero vectors
                    return true;

                for (int i = index; i < others.length; ++i) { // Start at first non-zero vector.
                    Vector c = others[index];
                    if (b.isZero()) // b is 0, so if a != c then they aren't collinear
                        if (!this.isCollinearWith(c))
                            return false;
                        else continue;

                    if (c.isZero()) // c is 0, so if a != b then they aren't collinear
                        if (!this.isCollinearWith(b))
                            return false;
                        else continue;

                    if (this.isZero()) // a is 0, so if b != c then they aren't collinear
                        if (!b.isCollinearWith(c))
                            return false;
                        else continue;

                    // None of them are zero vectors, so use transitive property.
                    if (!(this.isCollinearWith(b) && b.isCollinearWith(c)))
                        return false;

                    // That should be all the checking we need to do.
                }

                return true;
        }
    }

    /**
     * Used to tell the distance between two vectors.
     * @param other The other vector.
     * @return The distance between them.
     */
    public double distanceTo (Vector other) {
        return this.absoluteOffset(other).length();
    }

    /**
     * Used to tell if this is a zero vector.
     * @return Whether or not this vector lies at the origin point.
     */
    public boolean isZero () {
        return (x == 0) && (y == 0) && (z == 0);
    }

    /**
     * Used to get the actual difference between two vectors.
     * @param other The other vector.
     * @return A vector containing the differences between their x, y, and z coordinates.
     */
    public Vector offset (Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Used to get the absolute difference between the two vectors.
     * @param other The other vector.
     * @return The vector containing the absolute values of the differences between their
     *         x, y, and z coordinates.
     */
    public Vector absoluteOffset (Vector other) {
        return new Vector(Math.abs(x - other.x), Math.abs(y - other.y), Math.abs(z - other.z));
    }

    /**
     * Creates a new RemoteZeroVector from this one.
     * @return The new RZVector.
     */
    public RemoteZeroVector toRemoteVector () {
        return new RemoteZeroVector(this);
    }

    /**
     * Used to check if this vector lies higher in all three dimensions than
     * another vector.
     * @param other The other vector.
     * @return Whether or not this vector is greater.
     */
    public boolean greaterThan (Vector other) {
        return this.x > other.x && this.y > other.y && this.z > other.z;
    }

    /**
     * Used to check if this vector lies lower in all three dimensions than
     * another vector.
     * @param other The other vector.
     * @return Whether or not this vector is greater.
     */
    public boolean lessThan (Vector other) {
        return this.x < other.x && this.y < other.y && this.z < other.z;
    }

    /**
     * Used to check if this vector lies between two other vectors.
     * @param a One point.
     * @param b Another point.
     * @return Whether or not this vector is greater than A and less than B
     *         or less than A and greater than B.
     */
    public boolean isWithin (Vector a, Vector b) {
        return (greaterThan(a) && lessThan(b)) || (lessThan(a) && greaterThan(b));
    }

    /**
     * Whether or not this object equals another.
     * Inputs that will return true:
     *   - the same vector
     *   - org.futuredev.tracker.Vector where x, y, and z are equal.
     *   - com.sk89q.worldedit.Vector where x, y, and z are equal.
     *   - org.bukkit.util.Vector or org.bukkit.Location where x, y, and z are equal.
     *   - java.lang.String where format matches "x,y,z" and parsed values for x, y, and z are equal.
     *
     * @param other Another object.
     * @return Whether or not this object can be classified as the same point in an imaginary 3D field.
     */
    public boolean equals (Object other) {
        if (this == other)
            return true;

        if (other instanceof Vector || other instanceof org.bukkit.util.Vector
                || other instanceof Location || other instanceof String) {
            Vector vec;
            if (other instanceof org.bukkit.util.Vector)
                vec = Vector.fromBukkitVector((org.bukkit.util.Vector) other);
            else if (other instanceof Location)
                vec = Vector.fromLocation((Location) other);
            else if (other instanceof String)
                vec = Vector.fromString((String) other);
            else vec = (Vector) other;

            return this.x == vec.x && this.y == vec.y && this.z == vec.z;
        }

        return false;
    }

    /**
     * Whether or not this object "almost" equals another.
     * If this.equals(other) returns true for object, then this.remotelyEqualTo(other)
     * will return true.
     *
     * However, this can be used for "block" comparisons where we only want to
     * check if the integer portions of x, y, and z are equal.
     *
     * @param other The other vector.
     * @return Whether or not the two vectors are in the same block.
     */
    public boolean remotelyEqualTo (Object other) {
        if (this == other || this.equals(other))
            return true;

        if (other instanceof Vector || other instanceof org.bukkit.util.Vector
                || other instanceof Location || other instanceof String) {
            Vector vec;
            if (other instanceof org.bukkit.util.Vector)
                vec = Vector.fromBukkitVector((org.bukkit.util.Vector) other);
            else if (other instanceof Location)
                vec = Vector.fromLocation((Location) other);
            else if (other instanceof String)
                vec = Vector.fromString((String) other);
            else vec = (Vector) other;

            return this.floor().equals(vec.floor());
        }

        return false;
    }

    /**
     * Turns this vector into a location.
     * @param world The name of the world.
     * @return The location.
     */
    public Location asLocation (String world) {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * Creates a new instance of this exact object.
     * @return Clones this vector.
     */
    public Vector clone () {
        return new Vector(x, y, z, mutable);
    }

    /**
     * Gets a vector from a location.
     * @param location The location.
     * @return The vector.
     */
    public static Vector fromLocation (Location location) {
        return fromBukkitVector(location.toVector());
    }

    /**
     * Converts a Bukkit vector into a Tracker vector.
     * @param vector A Bukkit vector.
     * @return A Tracker vector.
     */
    public static Vector fromBukkitVector (org.bukkit.util.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Converts a String into a vector.
     * @param value A string formatted as x,y,z
     * @return The parsed vector.
     */
    public static Vector fromString (String value) {
        if (StringHelper.occurrencesOf(value, ':') < 2)
            return null;

        double x = Double.parseDouble(value.split(":")[0]),
                y = Double.parseDouble(value.split(":")[1]),
                z = Double.parseDouble(value.split(":")[2]);

        return new Vector(x, y, z);
    }

}