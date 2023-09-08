package schoolplanner.util;

public class SpawnMoment {
    private TimeStamp timeStamp;
    private String group;

    public SpawnMoment(TimeStamp timeStamp, String group) {
        this.timeStamp = timeStamp;
        this.group = group;
    }

    /**
     * Getter for timeStamp.
     * @return TimeStamp.
     */
    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * Getter for the group of the spawnMoment.
     * @return Group.
     */
    public String getGroup() {
        return group;
    }

    /**
     * Setter for the group of the spawnMoment.
     * @param group The group of the SpawnMoment.
     */
    public void setGroup(String group) {
        this.group = group;
    }
}
