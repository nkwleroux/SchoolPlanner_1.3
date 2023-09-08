package schoolplanner.util;

import javafx.scene.control.Alert;
import java.io.Serializable;

public class TimeStamp implements Serializable {
    private int hours;
    private int minutes;

    /**
     * This is our time class, this object holds a timestamp and has methods to make time calculations easy.
     */
    public TimeStamp(TimeStamp timeStamp) {
        this(timeStamp.getHours(), timeStamp.getMinutes());
    }

    public TimeStamp(int hours, int minutes) {
        if (hours < 24 && hours >= 0) {
            this.hours = hours;
        } else {
            AlertHandler.show(Alert.AlertType.ERROR, "Time error", "Illegal time!", "You can't make time with more " +
                    "than 23 hours and more than 59 minutes.");
        }

        if (minutes < 60 && minutes >= 0) {
            this.minutes = minutes;
        } else {
            AlertHandler.show(Alert.AlertType.ERROR, "Time error", "Illegal time!", "You can't make time with more " +
                    "Then 23 hours and more than 59 minutes.");
        }
    }

    /**
     * Getter for the hours of the timestamp.
     * @return Hours as int value.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Getter for the minutes of the timestamp.
     * @return Value of minutes as int.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Adds minutes to the timestamp.
     * @param amount Value in minutes.
     */
    public void addMinutes(int amount) {
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                if (minutes + 1 < 60) {
                    minutes++;
                } else {
                    hours++;
                    minutes = 0;
                }
            }
        } else {
            for (int i = 0; i > amount; i--) {
                if (minutes - 1 > 0) {
                    minutes--;
                } else {
                    hours--;
                    minutes = 59;
                }
            }
        }
    }

    /**
     * This method makes a new Time object with the difference in time.
     *
     * @param postTimeStamp is the time after the initial time.
     * @return a new Time object with the amount of time between the two.
     */
    public TimeStamp timeBetween(TimeStamp postTimeStamp) {

        if (this.isLaterThan(postTimeStamp)) {
            return postTimeStamp.timeBetween(this);
        }

        if (postTimeStamp.minutes >= this.minutes) {
            try {
                return new TimeStamp(postTimeStamp.getHours() - this.hours, postTimeStamp.minutes - this.minutes);
            } catch (Exception e) {
                AlertHandler.show(Alert.AlertType.ERROR, "Time error", "Illegal time!", "You can't make time with more " +
                        "than 23 hours and more than 59 minutes.");
            }
        } else {
            try {
                return new TimeStamp(postTimeStamp.getHours() - this.hours - 1, 60 - this.minutes + postTimeStamp.minutes);
            } catch (Exception e) {
                AlertHandler.show(Alert.AlertType.ERROR, "Time error", "Illegal time!", "You can't make time with more " +
                        "than 23 hours and more than 59 minutes.");
            }
        }
        return null;
    }

    /**
     * this method will return a true boolean if the time of the object is later than the time of the parameter time.
     *
     * @param secondTimeStamp the other time.
     * @return true: if the time is later than the secondTime time.
     * false: if the time is not later than the secondTime time
     */
    public boolean isLaterThan(TimeStamp secondTimeStamp) {

        if (this.hours > secondTimeStamp.hours) {
            return true;
        } else return (this.minutes > secondTimeStamp.getMinutes() && this.hours == secondTimeStamp.hours);

    }

    /**
     * ToString function for the class.
     * @return Data of the class in String value.
     */
    @Override
    public String toString() {
        if (this.minutes > 9) {
            return this.hours + ":" + this.minutes;
        } else {
            return this.hours + ":0" + this.minutes;
        }

    }
}
