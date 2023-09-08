package schoolplanner.simulator.map;

import schoolplanner.util.Coordinate;
import java.util.ArrayList;

public class DistanceMap {
    private ArrayList<ArrayList<Integer>> map;

    public DistanceMap(int mapWidth, int mapHeight) {
        this.map = new ArrayList<>();

        //fill the array to the size it will get.
        this.fillInitArray(mapHeight, mapWidth);
    }

    /**
     * getter for the point distance.
     * @param point the point to check.
     * @return distance from start that matches the point.
     */
    public int getPointInt(Coordinate point){
        return this.map.get(point.getValueY()).get(point.getValueX());
    }

    /**
     * Adds a point to the distance Map.
     * @param x x value.
     * @param y y value.
     * @param steps The amount of steps away from the start.
     */
    void addPoint(int x, int y, int steps){
        this.map.get(y).set(x, steps);
    }

    /**
     * getter for the distance map.
     * @return map ArrayList.
     */
    public ArrayList<ArrayList<Integer>> getMap() {
        return map;
    }

    /**
     * Fills the init Array with dummy values.
     * @param mapHeight Height of the distance map.
     * @param mapWidth Width of the distance map.
     */
    private void fillInitArray(int mapHeight, int mapWidth){
        for (int y = 0; y < mapHeight; y++) {

            this.map.add(new ArrayList<>());

            for (int x = 0; x < mapWidth; x++) {
                this.map.get(y).add(Integer.MAX_VALUE);
            }
        }
    }
}
