package schoolplanner.simulator.npc;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import org.jfree.fx.FXGraphics2D;
import schoolplanner.simulator.map.SimulatorRoom;

import java.awt.image.BufferedImage;

public abstract class Npc {

    abstract public void update();

    abstract public void setDestination(SimulatorRoom simulatorRoom, String group);

    abstract public Circle getHitBox();

    abstract public String getCurrentDestination();

    public abstract void draw(GraphicsContext graphicsContext);

    public abstract void setImage(Image image);

    public abstract boolean isStudent();

    public abstract boolean isArrived();
}