package schoolplanner.simulator.npc.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import schoolplanner.simulator.npc.Direction;
import schoolplanner.simulator.npc.Npc;
import schoolplanner.util.SpriteSet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpriteController {
    private ArrayList<SpriteSet> spriteSets;
    private HashMap<Npc, Direction>  lastDirection;
    private int tickRate;
    private final int DEFAULT_tickRate = 50;

    public SpriteController() {
        this.spriteSets = new ArrayList<>();
        this.lastDirection = new HashMap<>();
        initSpriteController();
        this.tickRate = DEFAULT_tickRate;
    }

    private void initSpriteController(){
        loadStudentSpriteSets();
        loadTeacherSpriteSet();
    }

    ArrayList<SpriteSet> getSpriteSets() {
        return spriteSets;
    }

    /**
     * Update function to use in order to change/cycle sprites of an NPC.
     * @param direction The Direction the NPC is facing.
     * @param setNumber The SpriteSet index to use.
     * @param idle true = idle, false = walking.
     */
    public void update(Npc npc, Direction direction, int setNumber, boolean idle) {
        switch (direction) {
            case Upwards:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getUpIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getUpWalking());
                        this.tickRate = this.DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case UpRight:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getUpRightIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getUpRightWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case Right:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getRightIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getRightWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case DownRight:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getDownRightIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getDownRightWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case Downwards:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getDownIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getDownWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case DownLeft:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getDownLeftIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getDownLeftWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case Left:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getLeftIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getLeftWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
            case UpLeft:
                if (idle) {
                    npc.setImage(this.spriteSets.get(setNumber).getUpLeftIdle());
                } else {
                    if (this.tickRate == 0 || direction != lastDirection.get(npc)) {
                        npc.setImage(this.spriteSets.get(setNumber).getUpLeftWalking());
                        this.tickRate = DEFAULT_tickRate;
                    } else {
                        this.tickRate--;
                    }
                }
                break;
        }
        if (!lastDirection.containsKey(npc) || lastDirection.get(npc) != direction) {
            this.lastDirection.put(npc, direction);
        }
    }

    /**
     * Method that loads all the spriteSets for the students.
     */
    private void loadStudentSpriteSets() {
        Image image;
        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(new File("resources/pokemon sprite.png"));
            int[] order = {1,7,3,5,0,4,2,6};
            for(int h = 0; h < 4; h++) {
                ArrayList<Image> tempImages = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 3; j++) {
                        image = SwingFXUtils.toFXImage(spriteSheet.getSubimage(((j * 32 + order[i] * 96)), h*32, 32, 32), null);
                        tempImages.add(image);
                    }
                }
                this.spriteSets.add(new SpriteSet(tempImages));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that loads all the spriteSets for the students.
     */
    private void loadTeacherSpriteSet() {
        Image image;
        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(new File("resources/TeacherSprite.png"));
            ArrayList<Image> tempImages = new ArrayList<>();
            int[] order = {3,2,0,1};
            int counter = 0;
            for (int i = 0; i < 4 ; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 4; k++) {
                        if (k != 3) {
                            image = SwingFXUtils.toFXImage(spriteSheet.getSubimage(k*32,order[counter]*32,32,32), null);
                            tempImages.add(image);
                        }
                    }
                }
                counter++;
            }
            this.spriteSets.add(new SpriteSet(tempImages));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
