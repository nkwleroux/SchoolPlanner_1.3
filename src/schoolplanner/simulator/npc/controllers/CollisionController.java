package schoolplanner.simulator.npc.controllers;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import schoolplanner.util.Coordinate;

import java.util.ArrayList;

public class CollisionController {

    private ArrayList<Circle> npcs;
    private ArrayList<Rectangle> walls;

    CollisionController(ArrayList<Circle> npcs) {
        this.npcs = npcs;
        this.walls = new ArrayList<>();
    }

    /**
     * this method will return the collisionPoint object, this object contains if the collision is a wall and where the
     * collision is located.
     * @param hitBox the hitbox of the checking npc.
     * @return CollisionPoint if there is collision else null;
     */
    public Coordinate isColliding(Circle hitBox) {

        for (Rectangle rectangle : this.walls) {
            if (rectangle.getBoundsInParent().intersects(hitBox.getBoundsInParent())) {
                return new Coordinate((int) rectangle.getX() / 2, (int) rectangle.getY() / 2);
            }
            for (Circle npc : this.npcs) {
                if (npc.getBoundsInParent().intersects(hitBox.getBoundsInParent()) && !npc.equals(hitBox)) {
                    return new Coordinate((int) npc.getCenterX(), (int) npc.getCenterY());
                }
            }
        }
        return null;
    }

    /**
     * this method returns an arrayList of the walls.
     * @return ArrayList of all walls.
     */
    ArrayList<Rectangle> getWalls() {
        return walls;
    }

    /**
     * this method returns an arrayList of the npc collision objects..
     * @return ArrayList of all Npc's.
     */
    ArrayList<Circle> getNpcs() {
        return npcs;
    }
}
