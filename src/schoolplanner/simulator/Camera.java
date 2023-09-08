package schoolplanner.simulator;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import schoolplanner.util.Coordinate;
import java.awt.geom.AffineTransform;

public class Camera {
    private AffineTransform cameraTransform;
    private Coordinate mouseLocation;
    private double scale;

    public Camera() {
        this.mouseLocation = new Coordinate(0, 0);
        this.cameraTransform = new AffineTransform();
        this.scale = 1;
    }

    /**
     * this method initialises the handlers.
     * @param canvas canvas to use.
     */
    void initHandlers(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (event) -> {

            double deltaX = -(Camera.this.mouseLocation.getValueX() - event.getX());
            double deltaY = -(Camera.this.mouseLocation.getValueY() - event.getY());

            this.cameraTransform.translate(deltaX, deltaY);

            this.mouseLocation.setValueX((int) event.getX());
            this.mouseLocation.setValueY((int) event.getY());

        });

        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, (event -> {
            this.mouseLocation.setValueX((int) event.getX());
            this.mouseLocation.setValueY((int) event.getY());
        }));

        canvas.addEventHandler(ScrollEvent.ANY, (event -> {

            if (event.getDeltaY() < 0) {
                if(this.cameraTransform.getScaleX() > 0.5) {
                    this.scale = 0.90;

                    double deltaX = (cameraTransform.getTranslateX() * scale) - cameraTransform.getTranslateX();
                    double deltaY = (cameraTransform.getTranslateY() * scale) - cameraTransform.getTranslateY();

                    this.cameraTransform.scale(scale, scale);
                    this.cameraTransform.translate(deltaX, deltaY);

                }
            } else {
                if(this.cameraTransform.getScaleX() < 2) {
                    this.scale = 1.10;

                    double deltaX = (cameraTransform.getTranslateX() * scale) - cameraTransform.getTranslateX();
                    double deltaY = (cameraTransform.getTranslateY() * scale) - cameraTransform.getTranslateY();

                    this.cameraTransform.scale(scale, scale);
                    this.cameraTransform.translate(deltaX, deltaY);
                }
            }

        }));
    }

    /**
     * this method returns the cameraTransformation.
     * @return cameraTransformation
     */
    AffineTransform getCameraTransform() {
        return cameraTransform;
    }
}
