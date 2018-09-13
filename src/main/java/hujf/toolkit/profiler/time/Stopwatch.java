package hujf.toolkit.profiler.time;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-12
 */
public final class Stopwatch {

    private Ticker ticker;
    private boolean running;
    private long elapsedNanos;
    private long startTick;

    /**
     * Creates (but does not start) a new stopwatch
     *
     */
    private Stopwatch() {
        this.ticker = Ticker.systemTicker();
    }

    /**
     * Creates (but does not start) a new stopwatch
     */
    public static Stopwatch createUnstarted() {
        return new Stopwatch();
    }

    /**
     * Creates (and starts) a new stopwatch
     */
    public static Stopwatch createStarted() {
        return new Stopwatch().start();
    }

    /**
     * Returns {@code true} if {@link #start()} has been called on this stopwatch,
     * and {@link #stop()} has not been called since the last call to {@code
     * start()}.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the stopwatch.
     */
    public Stopwatch start() {
        if (isRunning()) {
            throw new IllegalStateException("This stopwatch is already running.");
        }
        running = true;
        startTick = ticker.read();
        return this;
    }

    /**
     * Stops the stopwatch. Future reads will return the fixed duration that had
     * elapsed up to this point.
     */
    public Stopwatch stop() {
        long tick = ticker.read();
        if (!isRunning()) {
            throw new IllegalStateException("This stopwatch is already stopped.");
        }
        running = false;
        elapsedNanos += tick - startTick;
        return this;
    }

    /**
     * Sets the elapsed time for this stopwatch to zero,
     * and places it in a stopped state.
     */
    public Stopwatch reset() {
        elapsedNanos = 0;
        running = false;
        return this;
    }

    public long elapsedSeconds() {
        return elapsed(SECONDS);
    }

    public long elapsedMillis() {
        return elapsed(MILLISECONDS);
    }

    public long elapsedMicros() {
        return elapsed(MICROSECONDS);
    }

    /**
     * Returns the current elapsed time shown on this stopwatch, expressed
     * in the desired time unit, with any fraction rounded down.
     */
    public long elapsed(TimeUnit desiredUnit) {
        return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
    }

    private long elapsedNanos() {
        return running ? ticker.read() - startTick + elapsedNanos : elapsedNanos;
    }

    private static abstract class Ticker {

        /**
         * Returns the number of nanoseconds elapsed since this ticker's fixed
         * point of reference.
         */
        public abstract long read();

        /**
         * A ticker that reads the current time using {@link System#nanoTime}.
         *
         * @since 10.0
         */
        public static Ticker systemTicker() {
            return SYSTEM_TICKER;
        }

        private static final Ticker SYSTEM_TICKER = new Ticker() {
            @Override
            public long read() {
                return System.nanoTime();
            }
        };
    }

}
