package schoolplanner.simulator.map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import schoolplanner.util.Coordinate;

import javax.imageio.ImageIO;
import javax.json.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.FileInputStream;
import java.io.InputStream;

public class MapLoader {

    private int tileWidth;
    private int mapWidth;
    private int mapHeight;
    private int tileHeight;
    private JsonObject jsonObject;

    private DistanceMapController distanceMapController;

    private ArrayList<Image> tiles;
    private ArrayList<TileLayer> layers;
    private ArrayList<Rectangle> wallCollisionArea;

    private HashMap<String, SimulatorRoom> simulatorRooms;
    private HashMap<String, DistanceMap> chairs;

    MapLoader(String jsonFileLocation, String layerArrayName, String textureArrayName) {

        this.layers = new ArrayList<>();
        this.tiles = new ArrayList<>();
        this.wallCollisionArea = new ArrayList<>();
        this.simulatorRooms = new HashMap<>();
        this.chairs = new HashMap<>();
        

        /* BUG - cant read file, throws nullpointer error */
        InputStream data = getClass().getResourceAsStream(jsonFileLocation);

        JsonReader reader = Json.createReader(data);
        this.jsonObject = reader.readObject();
        System.out.println(this.jsonObject.size());

        this.tileHeight = this.jsonObject.getInt("tileheight");
        this.tileWidth = this.jsonObject.getInt("tilewidth");
        this.mapWidth = this.jsonObject.getInt("width");
        this.mapHeight = this.jsonObject.getInt("height");

        this.loadTiles(textureArrayName);
        this.loadLayers(layerArrayName);
    }

    /**
     * this method will load each layer into a layer array.
     * @param arrayName Name of the Array in String.
     */
    private void loadLayers(String arrayName) {
        for (int i = 0; i < this.jsonObject.getJsonArray(arrayName).size(); i++) {

            JsonObject layer = this.jsonObject.getJsonArray(arrayName).getJsonObject(i);

            if (layer.getString("name").equals("Collision")) {
                this.initDistanceMapController(layer);
                continue;
            }

            if (layer.getString("name").equals("Walls")) {
                this.loadWallCollision(layer);
            }

            if (layer.getString("type").equals("objectgroup")) {
                this.loadAreas(layer);
                continue;
            }

            if (layer.getString("type").equals("tilelayer")) {

                this.layers.add(new TileLayer(layer.getString("name"), layer.getBoolean("visible"),
                        this.getDataArray(layer.getJsonArray("data"))));

            }

        }
    }

    /**
     * if the layer has "wall" in the name, this method will make squares every wall tile for the collision.
     * @param wallLayer the layer with wall data;
     */
    private void loadWallCollision(JsonObject wallLayer) {
        int x = 0;
        int y = 0;

        for (int i : this.getDataArray(wallLayer.getJsonArray("data"))) {

            if (x == this.mapWidth * this.tileWidth) {

                y += this.tileHeight;

                x = 0;
            }

            // In the data the 0's will be empty space, so it will be skipped but it will add x to the iteration.
            if (i == 0) {

                x += this.tileWidth;

                continue;
            }

            Rectangle rectangle = new Rectangle();

            rectangle.setX(x);
            rectangle.setY(y);

            rectangle.setWidth((this.tileWidth));
            rectangle.setHeight((this.tileHeight));

            this.wallCollisionArea.add(rectangle);

            x += this.tileWidth;
        }
    }

    /**
     * This method will load the texture png, after that it will slice it up to pieces with the designated size,
     * At last it will add all the sliced pieces into an array.
     */
    private void loadTiles(String arrayName) {

        for (int i = 0; i < this.jsonObject.getJsonArray(arrayName).size(); i++) {

            JsonObject tileSet = this.jsonObject.getJsonArray(arrayName).getJsonObject(i);

            try {

                BufferedImage tilemap = ImageIO.read(new File("Resources/" + tileSet.getString("image")));

                for (int y = 0; y < (tilemap.getHeight() / this.tileHeight); y++) {

                    for (int x = 0; x < (tilemap.getWidth() / this.tileWidth); x++) {

                        this.tiles.add(SwingFXUtils.toFXImage(tilemap.getSubimage(x * this.tileWidth, y * this.tileHeight,
                                this.tileWidth, this.tileHeight), null));
                    }

                }
            } catch (IOException e) {

                System.out.println("File not found");

            }
        }
    }

    /**
     * this method will load all the objects in the json. These objects have an area which are used to determine where
     * the classrooms and chairs are located.
     * @param object the object from json;
     */
    private void loadAreas(JsonObject object) {
        for (int i = 0; i < object.getJsonArray("objects").size(); i++) {

            JsonObject temp = object.getJsonArray("objects").getJsonObject(i);

            double width = temp.getInt("width");
            double height = temp.getInt("height");

            int y = temp.getInt("y") / this.tileHeight;
            int x = temp.getInt("x") / this.tileWidth;

            String name = temp.getString("name");

            String type = temp.getString("type");

            switch (type) {

                case "Room":
                    this.simulatorRooms.put(name, new SimulatorRoom(
                            this.distanceMapController.getDistanceMap(new Coordinate(x + (int) ((width / this.tileWidth) / 2), y + (int) ((height / this.tileHeight) / 2))),
                            name));
                    break;

                case "Chair":

                    this.chairs.put(name + " " + temp.getInt("id"),
                            this.distanceMapController.getDistanceMap(new Coordinate(x + (int) ((width / this.tileWidth) / 2), y + (int) ((height / this.tileHeight) / 2))));

                    for (String key : this.simulatorRooms.keySet()) {

                        if (name.contains(key)) {
                            this.simulatorRooms.get(key).addChair(name + " " + temp.getInt("id"));

                        }
                    }
                    break;
            }
        }
    }

    /**
     * this init method initialises the controller of the distance map.
     * and searches which layer is the collisionLayer.
     * @param layer Layer object
     */
    private void initDistanceMapController(JsonObject layer) {

        ArrayList<Integer> collisionLayer = this.getDataArray(layer.getJsonArray("data"));

        for (TileLayer tileLayer : layers) {
            if (tileLayer.getName().equals("Collision")) {
                collisionLayer = tileLayer.getTiles();
            }
        }

        if (collisionLayer == null) {
            return;
        }

        this.distanceMapController = new DistanceMapController(this.mapWidth, this.mapHeight, collisionLayer);
    }

    /**
     * This method is to convert an array in a json file to an ArrayList.
     *
     * @param jsonValues the values parameter is the array in the json.
     * @return this method will return an ArrayList with all the data.
     */
    private ArrayList<Integer> getDataArray(JsonArray jsonValues) {

        ArrayList<Integer> tempArray = new ArrayList<>();

        for (JsonValue i : jsonValues) {
            if (Long.parseLong(i.toString()) > 10000) {
                tempArray.add(0);
                continue;
            }
            tempArray.add(Integer.parseInt(i.toString()));
        }

        return tempArray;
    }

    /**
     * this method returns an arrayList of rectangle's. these rectangle's are the wallCollision area's.
     * @return ArrayList of the wallCollisionArea's.
     */
    public ArrayList<Rectangle> getWallCollisionArea() {
        return wallCollisionArea;
    }

    /**
     * this method returns a hashMap containing simulator rooms and its names.
     * @return HashMap of all Simulator Rooms.
     */
    public HashMap<String, SimulatorRoom> getSimulatorRooms() {
        return this.simulatorRooms;
    }

    /**
     * this method returns the width of a tile.
     * @return Width of a tile.
     */
    int getTileWidth() {
        return tileWidth;
    }

    /**
     * this method returns the width of the map.
     * @return Width of the Map.
     */
    int getMapWidth() {
        return mapWidth;
    }

    /**
     * this method returns the height of the map.
     * @return Height of the Map.
     */
    int getMapHeight() {
        return mapHeight;
    }

    /**
     * this method returns the height of a tile.
     * @return Height of a tile.
     */
    int getTileHeight() {
        return tileHeight;
    }

    /**
     * this method returns a arrayList of bufferedImage's.
     * @return ArrayList of all tiles.
     */
    public ArrayList<Image> getTiles() {
        return tiles;
    }

    /**
     * this method returns a arrayList of TileLayer's.
     * @return ArrayList of all Layers
     */
    ArrayList<TileLayer> getLayers() {
        return layers;
    }

    /**
     * this method returns a hashMap of distanceMaps with its names..
     * @return HashMap of all chair distanceMaps.
     */
    public HashMap<String, DistanceMap> getChairs() {
        return chairs;
    }
}
