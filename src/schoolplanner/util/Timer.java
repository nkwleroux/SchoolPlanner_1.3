package schoolplanner.util;

public class Timer {

    private long startMilliseconds;
    private long timerAmount;

    public Timer(long timerAmount) {
        this.timerAmount = timerAmount;
        this.startMilliseconds = System.currentTimeMillis();
    }

    /**
     * sets the timer duration in milliseconds.
     * @param timerAmount Duration in milliseconds.
     */
    public void setTimerAmount(long timerAmount) {
        this.timerAmount = timerAmount;
    }

    /**
     * Checks if timer has finished.
     * @return Boolean, true if timer is finished, false if not.
     */
    public boolean timeout() {
        return System.currentTimeMillis() - this.startMilliseconds > this.timerAmount;
    }

    /**
     * resets the timer.
     */
    public void reset() {
        this.startMilliseconds = System.currentTimeMillis();
    }
}
