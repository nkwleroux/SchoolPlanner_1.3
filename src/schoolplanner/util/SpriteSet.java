package schoolplanner.util;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class SpriteSet {
    private Image upIdle;
    private Queue<Image> upWalking;
    private Image upRightIdle;
    private Queue<Image> upRightWalking;
    private Image rightIdle;
    private Queue<Image> rightWalking;
    private Image downRightIdle;
    private Queue<Image> downRightWalking;
    private Image downIdle;
    private Queue<Image> downWalking;
    private Image downLeftIdle;
    private Queue<Image> downLeftWalking;
    private Image leftIdle;
    private Queue<Image> leftWalking;
    private Image upLeftIdle;
    private Queue<Image> upLeftWalking;

    public SpriteSet(ArrayList<Image> images) {
        this.upWalking = new LinkedList<Image>();
        this.upRightWalking = new LinkedList<Image>();
        this.rightWalking = new LinkedList<Image>();
        this.downRightWalking = new LinkedList<Image>();
        this.downWalking = new LinkedList<Image>();
        this.downLeftWalking = new LinkedList<Image>();
        this.leftWalking = new LinkedList<Image>();
        this.upLeftWalking = new LinkedList<Image>();
        setQueues(images);
        setIdles();
    }

    /**
     * Getter for the up Idle sprite.
     * @return Sprite image.
     */
    public Image getUpIdle() {
        return upIdle;
    }

    /**
     * Getter for the up Walking sprite.
     * @return Sprite image.
     */
    public Image getUpWalking() {
        Image image = this.upWalking.poll();
        this.upWalking.add(image);
        return image;
    }

    /**
     * Getter for the upRight Idle sprite.
     * @return Sprite image.
     */
    public Image getUpRightIdle() {
        return upRightIdle;
    }

    /**
     * Getter for the upRight Walking sprite.
     * @return Sprite image.
     */
    public Image getUpRightWalking() {
        Image image = this.upRightWalking.poll();
        this.upRightWalking.add(image);
        return image;
    }

    /**
     * Getter for the right Idle sprite.
     * @return Sprite image.
     */
    public Image getRightIdle() {
        return rightIdle;
    }

    /**
     * Getter for the right Walking sprite.
     * @return Sprite image.
     */
    public Image getRightWalking() {
        Image image = this.rightWalking.poll();
        this.rightWalking.add(image);
        return image;
    }

    /**
     * Getter for the downRight Idle sprite.
     * @return Sprite image.
     */
    public Image getDownRightIdle() {
        return downRightIdle;
    }

    /**
     * Getter for the downRight Walking sprite.
     * @return Sprite image.
     */
    public Image getDownRightWalking() {
        Image image = this.downRightWalking.poll();
        this.downRightWalking.add(image);
        return image;
    }

    /**
     * Getter for the down Idle sprite.
     * @return Sprite image.
     */
    public Image getDownIdle() {
        return downIdle;
    }

    /**
     * Getter for the down Walking sprite.
     * @return Sprite image.
     */
    public Image getDownWalking() {
        Image image = this.downWalking.poll();
        this.downWalking.add(image);
        return image;
    }

    /**
     * Getter for the downLeft Idle sprite.
     * @return Sprite image.
     */
    public Image getDownLeftIdle() {
        return downLeftIdle;
    }

    /**
     * Getter for the downLeft Walking sprite.
     * @return Sprite image.
     */
    public Image getDownLeftWalking() {
        Image image = this.downLeftWalking.poll();
        this.downLeftWalking.add(image);
        return image;
    }

    /**
     * Getter for the left Idle sprite.
     * @return Sprite image.
     */
    public Image getLeftIdle() {
        return leftIdle;
    }

    /**
     * Getter for the left Walking sprite.
     * @return Sprite image.
     */
    public Image getLeftWalking() {
        Image image = this.leftWalking.poll();
        this.leftWalking.add(image);
        return image;
    }

    /**
     * Getter for the upLeft Idle sprite.
     * @return Sprite image.
     */
    public Image getUpLeftIdle() {
        return upLeftIdle;
    }

    /**
     * Getter for the upLeft Walking sprite.
     * @return Sprite image.
     */
    public Image getUpLeftWalking() {
        Image image = this.upLeftWalking.poll();
        this.upLeftWalking.add(image);
        return image;
    }

    /**
     * Sets all images in the correct Queues.
     *
     * @param images The List of all images to set.
     */
    private void setQueues(ArrayList<Image> images) {
        ArrayList<Queue<Image>> queues = getQueues();
        for (int i = 0; i < queues.size(); i++) {
            for (int j = i * 3; j < 3 * (i + 1); j++) {
                queues.get(i).add(images.get(j));
            }
        }
    }

    /**
     * Removes the first sprite from the queues of each direction
     * and sets it as the Idle sprite for that particular direction.
     */
    private void setIdles() {
        this.upIdle = this.upWalking.poll();
        this.upRightIdle = this.upRightWalking.poll();
        this.rightIdle = this.rightWalking.poll();
        this.downRightIdle = this.downRightWalking.poll();
        this.downIdle = this.downWalking.poll();
        this.downLeftIdle = this.downLeftWalking.poll();
        this.leftIdle = this.leftWalking.poll();
        this.upLeftIdle = this.upLeftWalking.poll();
    }

    /**
     * Function to get an ArrayList containing all Queues.
     *
     * @return ArrayList containing all Queues.
     */
    private ArrayList<Queue<Image>> getQueues() {
        ArrayList<Queue<Image>> queues = new ArrayList<>();
        queues.add(this.upWalking);
        queues.add(this.upRightWalking);
        queues.add(this.rightWalking);
        queues.add(this.downRightWalking);
        queues.add(this.downWalking);
        queues.add(this.downLeftWalking);
        queues.add(this.leftWalking);
        queues.add(this.upLeftWalking);
        return queues;
    }
}
