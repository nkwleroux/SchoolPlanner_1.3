package schoolplanner.simulator.map;

import java.util.HashMap;

public class SimulatorRoom {

    private HashMap<String, String> chairs;

    private String name;
    private DistanceMap distanceMap;

    SimulatorRoom(DistanceMap distanceMap, String name) {
        this.chairs = new HashMap<>();

        this.name = name;
        this.distanceMap = distanceMap;
    }

    /**
     * this method returns a chair name if any of the chairs is available.
     */
    public String getChairName(String groupName){

        if(groupName.equals("Teacher")){
            for(String key : this.chairs.keySet()){
                if(key.contains("teach")){
                    this.chairs.put(key,groupName);
                    return key;
                }
            }
        }

        String chairName = this.name;
        for (String chair : this.chairs.keySet()) {
            if (this.chairs.get(chair).equals("Empty")) {
                chairName = chair;
                this.chairs.put(chair,groupName);
                break;
            }
        }
        return chairName;
    }

    /**
     * turns the chairs used by the param group and empties them.
     */
    public void releaseChairs(String groupName) {
        for (String chair : this.chairs.keySet()) {
            if (this.chairs.get(chair).equals(groupName)) {
                this.chairs.put(chair,"Empty");
            }
        }
    }

    /**
     * this method adds a chair to the hashMap of chairs.
     * @param name name of the chair to add.
     */
    void addChair(String name){
        this.chairs.put(name,"Empty");
    }

    /**
     * this method returns the name of the simulator Room.
     * @return name of the Room.
     */
    public String getName() {
        return name;
    }

    /**
     * this method returns the distanceMap of the simulatorRoom.
     * @return distanceMap of the room.
     */
    public DistanceMap getDistanceMap() {
        return distanceMap;
    }

    @Override
    public String toString() {
        return name;
    }
}
