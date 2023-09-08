package schoolplanner.simulator.npc.controllers;

import schoolplanner.simulator.map.DistanceMap;
import schoolplanner.simulator.map.SchoolMap;
import schoolplanner.simulator.npc.Direction;
import schoolplanner.util.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PathFindingContainer {

    private Random random;
    private SchoolMap schoolMap;
    private HashMap<String, DistanceMap> destinations;

    public PathFindingContainer(SchoolMap schoolMap) {
        this.destinations = new HashMap<>();
        this.random = new Random();
        this.schoolMap = schoolMap;

        for (String key : this.schoolMap.getMapLoader().getSimulatorRooms().keySet()) {
            this.destinations.put(key, this.schoolMap.getMapLoader().getSimulatorRooms().get(key).getDistanceMap());
        }

        this.destinations.putAll(this.schoolMap.getMapLoader().getChairs());
    }

    /**
     * this method will return a coordinate of the next tile in the path to destination
     * @param point location of the npc;
     * @param nameOfLocation name of the area the npc wants to go to;
     * @return point to go next;
     */
    public Coordinate getDirectionTo(Coordinate point, String nameOfLocation) {

        point = this.schoolMap.convertToTileLocation(point.getValueX(), point.getValueY());

        if (!this.destinations.containsKey(nameOfLocation)) {
            System.out.println("The location that is requested is NA, key: " + nameOfLocation);
            return null;
        }

        DistanceMap distanceMap = this.destinations.get(nameOfLocation);

        ArrayList<Direction> directionsAccessible = new ArrayList<>();

        int stepsToGO = distanceMap.getPointInt(point);

        if (stepsToGO == 0) {
            return null;
        }

        int x = point.getValueX();
        int y = point.getValueY();


        if (y > 0) {
            if (distanceMap.getMap().get(y - 1).get(x) < stepsToGO && distanceMap.getMap().get(y - 1).get(x) != Integer.MAX_VALUE) {

                directionsAccessible.add(Direction.Upwards);

            }
        }

        if (y < distanceMap.getMap().size() - 1) {
            if (y < distanceMap.getMap().size()) {
                if (distanceMap.getMap().get(y + 1).get(x) < stepsToGO && distanceMap.getMap().get(y + 1).get(x) != Integer.MAX_VALUE) {

                    directionsAccessible.add(Direction.Downwards);

                }
            }
        }

        if (x > 0) {
            if (distanceMap.getMap().get(y).get(x - 1) < stepsToGO && distanceMap.getMap().get(y).get(x - 1) != Integer.MAX_VALUE) {

                directionsAccessible.add(Direction.Left);

            }
        }

        if (x < distanceMap.getMap().get(y).size() - 1)
        if (x < distanceMap.getMap().get(0).size()) {
            if (distanceMap.getMap().get(y).get(x + 1) < stepsToGO && distanceMap.getMap().get(y).get(x + 1) != Integer.MAX_VALUE) {

                directionsAccessible.add(Direction.Right);

            }
        }

        if (directionsAccessible.isEmpty()) {
            directionsAccessible.add(Direction.None);
        }

        switch (directionsAccessible.get(random.nextInt(directionsAccessible.size()))) {

            case Upwards:
                return this.schoolMap.getTileLocation(x, y - 1);
            case Downwards:
                return this.schoolMap.getTileLocation(x, y + 1);
            case Left:
                return this.schoolMap.getTileLocation(x - 1, y);
            case Right:
                return this.schoolMap.getTileLocation(x + 1, y);
            default:
                return this.schoolMap.getTileLocation(x, y);
        }

    }
}
