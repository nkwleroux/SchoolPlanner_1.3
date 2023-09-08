package schoolplanner.simulator.npc.types;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import schoolplanner.simulator.map.SimulatorRoom;
import schoolplanner.simulator.npc.controllers.CollisionController;
import schoolplanner.simulator.npc.controllers.PathFindingContainer;
import schoolplanner.simulator.npc.Direction;
import schoolplanner.simulator.npc.Npc;
import schoolplanner.simulator.npc.controllers.SpriteController;
import schoolplanner.util.Coordinate;

import java.util.Random;

public class UniversalNpc extends Npc {

    private String currentDestination;
    private String currentChair = "";
    private SimulatorRoom lastDestination;
    private PathFindingContainer pathFindingContainer;
    private Circle hitBox;
    private Coordinate currentPosition;
    private Coordinate currentDestinationPoint;
    private Image image;
    private SpriteController spriteController;
    private CollisionController collisionController;
    private boolean isWalking;
    private boolean hasDestination;
    private boolean arrived;
    private double speed;
    private boolean isStudent;
    private int spriteSetNumber;

    public UniversalNpc(double speed,
                        PathFindingContainer pathFindingContainer,
                        int spriteSetNumber,
                        Coordinate currentPosition,
                        Image npcAvatar,
                        SpriteController spriteController,
                        CollisionController collisionController,
                        boolean isStudent) {

        this.isStudent = isStudent;
        this.previous = null;
        this.spriteController = spriteController;
        this.collisionController = collisionController;
        this.image = npcAvatar;
        this.isWalking = false;
        this.hasDestination = false;
        this.arrived = false;
        this.spriteSetNumber = spriteSetNumber;

        this.lastDestination = null;
        this.currentDestination = null;
        this.currentPosition = currentPosition;
        this.pathFindingContainer = pathFindingContainer;
        this.speed = speed;
        this.hitBox = new Circle(10);
    }

    public UniversalNpc(double speed, PathFindingContainer pathFindingContainer, int spriteSetNumber,
                        Coordinate currentPosition, Image npcAvatar, SpriteController spriteController,
                        CollisionController collisionController) {

        this.isStudent = true;
        this.previous = null;
        this.spriteController = spriteController;
        this.collisionController = collisionController;
        this.image = npcAvatar;
        this.isWalking = false;
        this.hasDestination = false;
        this.spriteSetNumber = spriteSetNumber;

        this.lastDestination = null;
        this.currentDestination = null;
        this.currentPosition = currentPosition;
        this.pathFindingContainer = pathFindingContainer;
        this.speed = speed;
        this.hitBox = new Circle(10);
    }

    /**
     * this method will update the walking dynamics and the sprite controller for the right facing sprite.
     */
    public void update() {
        if (!this.isWalking && this.hasDestination) {

            Coordinate coordinate = pathFindingContainer.getDirectionTo(this.currentPosition, this.currentDestination);

            this.arrived = coordinate == null;

            if (coordinate != null) {
                goToNextTile(coordinate);
            }

        } else if (this.hasDestination) {
            walkStep();
        }

        if (this.hasDestination) {

            if (this.currentDestinationPoint.getValueX() == this.currentPosition.getValueX() &&
                    this.currentDestinationPoint.getValueY() == this.currentPosition.getValueY()) {
                this.isWalking = false;

                if (this.currentChair.toLowerCase().contains("up")) {
                    this.spriteController.update(this, Direction.Upwards, this.spriteSetNumber, true);
                } else if (this.currentChair.toLowerCase().contains("down")) {
                    this.spriteController.update(this, Direction.Downwards, this.spriteSetNumber, true);
                } else if (this.currentChair.toLowerCase().contains("left")) {
                    this.spriteController.update(this, Direction.Left, this.spriteSetNumber, true);
                } else if (this.currentChair.toLowerCase().contains("right")) {
                    this.spriteController.update(this, Direction.Right, this.spriteSetNumber, true);
                }
            }
        }
    }

    /**
     * this method will override the destination to the param one.
     * @param simulatorRoom the destination it has to go.
     * @param group specification for the chair in the room.
     */
    public void setDestination(SimulatorRoom simulatorRoom, String group) {

        if (this.lastDestination != null) {
            lastDestination.releaseChairs(group);
        }

        this.currentDestination = simulatorRoom.getChairName(group);

        this.currentChair = currentDestination;

        this.lastDestination = simulatorRoom;

        this.hasDestination = true;
    }

    /**
     * this method sets the destinationPoint;
     * @param direction the next coordinate.
     */
    private void goToNextTile(Coordinate direction) {
        if (direction == null) {
            return;
        }

        this.currentDestinationPoint = direction;

        if (direction.getValueX() == this.currentPosition.getValueX() &&
                direction.getValueY() == this.currentPosition.getValueY()) {
            return;
        }
        this.isWalking = true;
    }

    private Coordinate previous;

    /**
     * this method handles the walking and collision. it uses the currentDestinationPoint.
     */
    private void walkStep() {

        Coordinate pointBeforeMovement = new Coordinate(this.currentPosition);

        if (currentDestination == null || this.currentPosition == null) {
            return;
        }

        int currentX = currentPosition.getValueX();
        int currentY = currentPosition.getValueY();

        Direction direction = Direction.None;
        if (this.currentDestinationPoint.getValueX() < currentX) { //Left
            direction = Direction.Left;
            this.currentPosition.setValueX((int) (currentX - this.speed));
        }
        if (this.currentDestinationPoint.getValueX() > currentX) { //Right
            direction = Direction.Right;
            this.currentPosition.setValueX((int) (currentX + this.speed));
        }
        if (this.currentDestinationPoint.getValueY() < currentY) { //Up
            direction = Direction.Upwards;
            this.currentPosition.setValueY((int) (currentY - this.speed));
        }
        if (this.currentDestinationPoint.getValueY() > currentY) { //Down
            direction = Direction.Downwards;
            this.currentPosition.setValueY((int) (currentY + this.speed));
        }
        this.setHitBox();

        this.spriteController.update(this, direction, spriteSetNumber, false);

        Coordinate collision = this.collisionController.isColliding(this.hitBox);

        if (collision == null) {
            return;
        }

        int bound = 1;

        if(previous != null && (pointBeforeMovement.getValueX() - previous.getValueX()
                == 0 && pointBeforeMovement.getValueY() - previous.getValueY() == 0)){
            Random random = new Random();
            bound = random.nextInt(4);
        }

        boolean pushed = false;

        currentX = currentPosition.getValueX();
        currentY = currentPosition.getValueY();

        if (currentX < collision.getValueX() + 5) {
            this.currentPosition.setValueX(currentX - bound);
            this.currentPosition.setValueY(currentY);
            pushed = true;
        } else if (currentX > collision.getValueX() + 5) {
            this.currentPosition.setValueX(currentX +bound);
            this.currentPosition.setValueY(currentY);
            pushed = true;
        }
        if (currentY < collision.getValueY() + 5) {
            this.currentPosition.setValueX(currentX);
            this.currentPosition.setValueY(currentY - bound);
            pushed = true;
        } else if (currentY > collision.getValueY() + 5) {
            this.currentPosition.setValueX(currentX);
            this.currentPosition.setValueY(currentY + bound);
            pushed = true;
        }

        if (pushed) {
            goToNextTile(pathFindingContainer.getDirectionTo(this.currentPosition, this.currentDestination));
        }
        this.setHitBox();
        this.previous = new Coordinate(this.currentPosition);

    }

    /**
     * this method will set the hitBox to the currentDestinationPoint.
     */
    private void setHitBox() {
        this.hitBox.setCenterX(this.currentPosition.getValueX());
        this.hitBox.setCenterY(this.currentPosition.getValueY());
    }

    public String getCurrentDestination() {
        return currentDestination;
    }

    public boolean isArrived() {
        return arrived;
    }

    /**
     * setImage will be called by the spriteController to update the sprite.
     * @param image the new sprite.
     */
    @Override
    public void setImage(Image image) {
        this.image = image;
    }

    public Circle getHitBox() {
        return hitBox;
    }

    public boolean isStudent() {
        return isStudent;
    }

    /**
     * draws the npc.
     * @param context Graphics to use.
     */
    @Override
    public void draw(GraphicsContext context) {
        context.translate(-16, -16);
        context.drawImage(image, this.currentPosition.getValueX(), this.currentPosition.getValueY());
        context.translate(16, 16);
    }
}
