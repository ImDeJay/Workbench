package org.futuredev.workbench.math;

import java.util.ArrayList;

public class VectorRegion {

    Vector min, max;

    public enum Direction {
        NORTH, SOUTH, EAST, WEST, UP, DOWN;

        public Direction invert () {
            switch (this) {
                default:
                case NORTH: return SOUTH;

                case SOUTH: return NORTH;
                case EAST: return WEST;
                case WEST: return EAST;
                case UP: return DOWN;
                case DOWN: return UP;
            }
        }
    }

    /**
     * Constructs a defined vector region.
     * @param min The min vector.
     * @param max The max vector.
     */
    public VectorRegion (Vector min, Vector max) {
        this.min = min;
        this.max = max;
        this.rectify();
    }

    /**
     * Checks if the region is defined.
     * @return See the previous line.
     */
    public boolean isDefined () {
        return min != null && max != null;
    }

    /**
     * Undefines the region (removes the points).
     * @return This, for stacking.
     */
    public VectorRegion undefine () {
        return this.setMax(null).setMin(null);
    }

    /**
     * Corrects a region's coordinate vectors.
     * This method ensures a region's min vector is always at the
     * smallest possible point and that the region's max vector is
     * always at the biggest.
     *
     * Pre-rectification:  [ {0, 10, 15}; {5, 5, 5} ]
     * Post-rectification: [ {0, 5, 5}; {5, 10, 15} ]
     *
     * @return This, for stacking.
     */
    public VectorRegion rectify () {
        double minX = this.min.getX();
        double minY = this.min.getY();
        double minZ = this.min.getZ();
        double maxX = this.max.getX();
        double maxY = this.max.getY();
        double maxZ = this.max.getZ();

        this.min = new Vector(Math.min(minX, maxX), Math.min(minY, maxY), Math.min(minZ, maxZ));
        this.max = new Vector(Math.max(minX, maxX), Math.max(minY, maxY), Math.max(minZ, maxZ));

        return this;
    }

    public Vector getMin () { return this.min; }
    public Vector getMax () { return this.max; }

    /**
     * Sets the minimum vector.
     * @param min The minimum vector.
     * @return This, for stacking.
     */
    public VectorRegion setMin (Vector min) {
        this.min = min;
        return this.rectify();
    }

    /**
     * Sets the maximum vector.
     * @param max The maximum vector.
     * @return This, for stacking.
     */
    public VectorRegion setMax (Vector max) {
        this.max = max;
        return this.rectify();
    }

    /**
     * Used to get the size of the region.
     * @return The size of this region.
     */
    public int size () {
        if (!isDefined())
            return 0;

        Vector diff = min.absoluteOffset(max);
        return diff.getBlockX() * diff.getBlockY() * diff.getBlockZ();
    }

    /**
     * Gets the dimensions of the region as a vector.
     * @return See the previous line.
     */
    public Vector dimensions () {
        return min.absoluteOffset(max);
    }

    /**
     * Used to check if this is bigger than another region.
     * @param other The other region.
     * @return Whether or not this one is bigger.
     */
    public boolean largerThan (VectorRegion other) {
        return this.dimensions().greaterThan(other.dimensions());
    }

    /**
     * Gets the dimensions divided by 1/2 so the region can be moved.
     * @return A vector with those dimensions.
     */
    public Vector expandable () {
        Vector dimensions = dimensions();
        double x = dimensions.x / 2;
        double y = dimensions.y / 2;
        double z = dimensions.z / 2;
        return new Vector(x, y, z);
    }

    /**
     * Shifts the region.
     * @param adjustments Any adjustments that should be made.
     * @return The shifted region.
     */
    public VectorRegion shift (Vector... adjustments) {
        for (Vector adjustment : adjustments) {
            this.max.add(adjustment.x, adjustment.y, adjustment.z);
            this.min.add(adjustment.x, adjustment.y, adjustment.z);
        }

        return this;
    }

    /**
     * Shifts the region from the starting vector to the finish vector.
     * @param from The vector at which to start.
     * @param to The vector at which to finish.
     * @return The shifted region.
     */
    public VectorRegion shift (Vector from, Vector to) {
        return this.shift(from.absoluteOffset(to));
    }

    /**
     * Shifts this vector to zero.
     * @return This, for stacking.
     */
    public VectorRegion shiftToZero () {
        return this.shift(this.min, new Vector(0, 0, 0));
    }

    public VectorRegion contract (Vector... adjustments) {
        for (Vector vec : adjustments)
            vec.invert();

        return this.expand(adjustments);
    }

    public VectorRegion contract (double amount, Direction... dirs) {
        for (int i = 0; i < dirs.length; ++i) {
            dirs[i] = dirs[i].invert();
        }

        return this.expand(amount, dirs);
    }

    public VectorRegion expand (Vector... adjustments) {
        for (Vector vec : adjustments) {
            this.rectify();

            if (vec.x != 0) {
                if (vec.x < 0)
                    this.min.subtract(vec.x, 0, 0);
                else
                    this.max.add(vec.x, 0, 0);
            }

            if (vec.y != 0) {
                if (vec.y < 0)
                    this.min.subtract(0, vec.y, 0);
                else
                    this.max.add(0, vec.y, 0);
            }

            if (vec.z != 0) {
                if (vec.z < 0)
                    this.min.subtract(0, 0, vec.z);
                else
                    this.max.add(0, 0, vec.z);
            }
        }

        return this.rectify();
    }

    /**
     * Expands the vector by a given amount. Simpler than using vectors for expansion.
     * @param amount The amount to expand.
     * @param dirs The directions in which to expand.
     * @return The expanded region.
     */
    public VectorRegion expand (double amount, Direction... dirs) {
        ArrayList<Vector> adjustments = new ArrayList<Vector>();
        for (Direction dir : dirs) {
            Vector adjustment;
            switch (dir) {
                case UP: adjustment = new Vector(0, amount, 0);
                    break;

                case DOWN: adjustment = new Vector(0, -(amount), 0);
                    break;

                case NORTH: adjustment = new Vector(0, 0, -(amount));
                    break;

                case SOUTH: adjustment = new Vector(0, 0, amount);
                    break;

                case WEST: adjustment = new Vector(-(amount), 0, 0);
                    break;

                default:
                case EAST: adjustment = new Vector(amount, 0, 0);
                    break;


            }

            adjustments.add(adjustment);
        }

        return this.expand(adjustments.toArray(new Vector[adjustments.size()]));
    }

    /**
     * Whether or not this region contains a point.
     * @param vec The vector to check.
     * @return Whether or not the vector is inside the region.
     */
    public boolean contains (Vector vec) {
        return this.isDefined() && vec.isWithin(this.min, this.max);
    }


}