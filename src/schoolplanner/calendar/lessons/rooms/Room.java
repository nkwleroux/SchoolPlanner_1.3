package schoolplanner.calendar.lessons.rooms;

public abstract class Room {
    protected String name;
    private int capacity;

    public Room(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * this method returns a string which is the name of the room.
     * @return name of the room.
     */
    public String getName() {
        return name;
    }

    /**
     * this method sets the name to the parameter which is given.
     * @param name name of the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this method returns the amount of people that can be in this room.
     * @return capacity of the room.
     */
    public int getCapacity() {
        return capacity;
    }
}
