package schoolplanner.simulator.map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import schoolplanner.util.Coordinate;

import java.util.ArrayList;

public class SchoolMap {

    private MapLoader mapLoader;

    private ArrayList<Image> tiles;

    public SchoolMap(String filePathJson) {
        this.mapLoader = new MapLoader(filePathJson, "layers", "tilesets");
        this.tiles = this.mapLoader.getTiles();
    }

    /**
     * This method will add the map to the given Pane.
     */
    public void drawLayers(GraphicsContext context) {

        for (TileLayer layer: this.mapLoader.getLayers()) {
            if(layer.isVisible()){
                this.drawLayer(layer.getTiles(), context);
            }
        }

    }

    /**
     * This method is to draw each individual layer of the map.
     *
     * @param layerData the data to decide which pane goes where.
     */
    private void drawLayer(ArrayList<Integer> layerData, GraphicsContext context) {

        int x = 0;
        int y = 0;

        int tileWidth = this.mapLoader.getTileWidth();
        int tileHeight = this.mapLoader.getTileHeight();

        for (int i : layerData) {

            if (x >= this.mapLoader.getMapWidth() * tileWidth) {
                y += tileHeight;
                x = 0;
            }

            // In the data the 0's will be empty space, so it will be skipped, but it will add to the iteration.
            if (i == 0 || i > layerData.size()) {
                x += tileWidth;
                continue;
            }

            // The data in our tiles array begins with index 0, the data from the json begins with 1, the -1 will prevent misplacement.
            // Also, if the number is bigger than the size of the array, there will be an error.
            context.drawImage(this.tiles.get(i - 1), x, y);

            x += tileWidth;
        }
    }

    /**
     * this method will return the location of the param tileX and tileY
     * @return point on screen.
     */
    public Coordinate getTileLocation(double tileX, double tileY) {

        return new Coordinate((int)(tileX * this.mapLoader.getTileWidth()) + this.mapLoader.getTileWidth() / 2,
                            (int)(tileY * this.mapLoader.getTileHeight()) + this.mapLoader.getTileHeight() / 2);

    }

    /**
     * method will return the tile location of the point given.
     * @return tile location
     */
    public Coordinate convertToTileLocation(int x, int y) {

        int tempX = 0;
        int tempY = 0;

        int mapWidth = this.mapLoader.getMapWidth();
        int mapHeight = this.mapLoader.getMapHeight();

        int tileWidth = this.mapLoader.getTileWidth();
        int tileHeight = this.mapLoader.getTileHeight();

        for (int i = 0; i <= mapWidth - 1; i++) {

            if ((i * tileWidth) <= x && (i * tileWidth) + tileWidth >= x) {
                tempX = i;
                break;
            }

        }

        for (int i = 0; i <= mapHeight - 1; i++) {

            if ((i * tileHeight) <= y && (i * tileHeight) + tileHeight >= y) {
                tempY = i;
                break;
            }

        }
        return new Coordinate(tempX, tempY);
    }

    /**
     * this method returns the mapLoader of the schoolMap.
     * @return mapLoader of the SchoolMap.
     */
    public MapLoader getMapLoader() {
        return mapLoader;
    }
}
