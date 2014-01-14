package org.futuredev.workbench.math;

public class RemoteZeroVector extends Vector {

    double yaw, pitch;

    public RemoteZeroVector (double x, double y, double z, double yaw, double pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public RemoteZeroVector (Vector existing) {
        this (existing.x, existing.y, existing.z, 0.0D, 0.0D);
    }

    public double getYaw ()   { return yaw;   }
    public double getPitch () { return pitch; }

    /**
     * Gets a Vector pointing to the yaw + pitch by using fancy math
     * @return A vector with the amount to increment x, y, and z per 'unit'
     */
    public Vector getDirection () {
        double rotX = yaw, rotY = pitch, cosY = Math.cos(Math.toRadians(rotY));

        double x = (-cosY * Math.sin(Math.toRadians(rotX))), y = (-Math.sin(Math.toRadians(rotY))),
                z = (cosY * Math.cos(Math.toRadians(rotX)));

        return new Vector(x, y, z);
    }

    /**
     * Performs a ray-trace out to the current distance.
     *
     * This works by calculating how much x, y, and z need to be incremented per 'unit'.
     * So, for example, if a person's yaw is 0 and their pitch is 90, then x = 0, z = 0, and
     * y = 1.
     *
     * This results in a vector A(dx, dy, dz) {where d is the distance}.
     * We can now simply add the components of vector A to the current vector.
     *
     * @param distance The distance to go out.
     * @return The coordinate with the same yaw and pitch as this vector, but (distance) away.
     */
    public Vector trace (double distance) {
        Vector dir = this.getDirection();
        return this.clone().add(dir.x * distance, dir.y * distance, dir.z * distance);
    }


}