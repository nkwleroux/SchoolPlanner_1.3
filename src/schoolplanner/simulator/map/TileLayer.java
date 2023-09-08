package schoolplanner.simulator.map;

import java.util.ArrayList;

public class TileLayer {
    private String name;
    private boolean visible;
    private ArrayList<Integer> tiles;

    TileLayer(String name, boolean visible, ArrayList<Integer> tiles) {
        this.name = name;
        this.visible = visible;
        this.tiles = tiles;
    }

    /**
     * this method returns the name of the TileLayer.
     * @return name of the Layer.
     */
    public String getName() {
        return name;
    }

    /**
     * this method returns a boolean which tells if the TileLayer is visible or not..
     * @return boolean true if the layer is visible.
     */
    boolean isVisible() {
        return visible;
    }

    /**
     * this method returns an arrayList of Tile Integers.
     * @return ArrayList of all tiles.
     */
    ArrayList<Integer> getTiles() {
        return tiles;
    }
}
