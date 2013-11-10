package org.futuredev.workbench.math;

/**
 * Represents a timer.
 * @author afistofirony
 */
public class Timer {

    long start;
    long finish;

    /**
     * Starts the timer.
     */
    public Timer () {
        start = System.nanoTime();
    }

    /**
     * Marks the stopping point.
     * @return This, for stacking.
     */
    public Timer mark () {
        finish = System.nanoTime();

        return this;
    }

    /**
     * Resets the timer.
     * @return This, for stacking.
     */
    public Timer reset () {
        start = System.nanoTime();
        finish = start;

        return this;
    }

    /**
     * Gets the elapsed time, in milliseconds.
     * @return The time.
     */
    public long timeElapsed () {
        return (finish - start) / 1000000;
    }

    /**
     * Gets the time between now and the starting point.
     * @return ^
     */
    public long report () {
        return (System.currentTimeMillis() - start) / 1000000;
    }

    /**
     * Gets the elapsed time, in nanoseconds.
     * @return The amount of nanoseconds elapsed.
     */
    public long nanosecondsElapsed () {
        return finish - start;
    }

}