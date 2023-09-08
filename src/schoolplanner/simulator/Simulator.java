package schoolplanner.simulator;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import schoolplanner.calendar.Schedule;
import schoolplanner.simulator.map.SchoolMap;
import schoolplanner.simulator.npc.controllers.NpcManager;
import schoolplanner.util.Timer;

import java.awt.*;

public class Simulator {

    // We chose to have the simulation running at 60 frames per second.
    private static final short fps = 60;

    // This variable shows the running states of the simulation.
    private boolean isRunning;
    private double speed;

    // This timer will show when to update all the entities to limit to the amount of fps given.
    private Timer fpsTimer;

    private Timer simulationTimer;
    private Camera camera;
    private Schedule selectedSchedule;
    private ToolBar toolBar;
    private Button backButton;
    private AnimationTimer animationTimer;
    private SchoolMap schoolMap;
    private NpcManager npcManager;
    private Label timeLabel;
    private FXGraphics2D graphics;
    private Canvas canvas;
    private Button skipButton;

    public Simulator(Schedule schedule, Button backButton, Stage window) {
        String json_path = "/pokemon_school_finito.json"; 
        this.schoolMap = new SchoolMap(json_path);
        this.backButton = backButton;
        this.selectedSchedule = schedule;
        this.isRunning = false;
        this.speed = 1;

        // To make sure the simulation is 60 fps, we wait for an update 1000 millis divided by the given fps.
        this.fpsTimer = new Timer((long) 1000.0 / fps);

        this.initSimulator();
        this.initNpcDynamics();

        this.camera = new Camera();

        BorderPane mainPane = new BorderPane();
        this.canvas = new ResizableCanvas(this::draw, mainPane);
        graphics = new FXGraphics2D(canvas.getGraphicsContext2D());
        mainPane.setCenter(canvas);
        window.setScene(new Scene(mainPane));
        window.setTitle("Simulator");
        window.show();

        camera.initHandlers(canvas);

        this.toolBar = new ToolBar();
        mainPane.setTop(this.toolBar);
        this.initToolbar();

    }

    /**
     * this init method initialises npc-dynamics for the npc's.
     */
    private void initNpcDynamics() {
        this.npcManager = new NpcManager(this.selectedSchedule, this.schoolMap);
    }

    /**
     * The init method makes the animation timer and has the handler.
     */
    private void initSimulator() {

        this.simulationTimer = new Timer(Math.round(60000 / this.speed));

        this.animationTimer = new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                if (fpsTimer.timeout()) {
                    if (isRunning) {
                        update();
                    }
                    fpsTimer.reset();
                }
                last = now;
                draw(graphics);
            }
        };

        animationTimer.start();
    }

    /**
     * The initToolbar method makes the buttons and has the handlers.
     */
    private void initToolbar() {

        Button playButton = new Button(">");

        playButton.setOnAction((event ->
            this.start()
        ));

        Button pauseButton = new Button("||");

        pauseButton.setOnAction((event ->
            this.stop()
        ));

        Label multiplyLabel = new Label(String.valueOf(1));

        Slider timeScaleSlider = new Slider();

        timeScaleSlider.setBlockIncrement(1);

        timeScaleSlider.setShowTickMarks(true);

        timeScaleSlider.setPrefWidth(200);

        timeScaleSlider.setMin(timeScaleSlider.getBlockIncrement());

        timeScaleSlider.setMax(6);

        timeScaleSlider.setValue(1);

        timeScaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            multiplyLabel.setText(String.valueOf(newValue.intValue()));

            this.speed = newValue.doubleValue();

            this.npcManager.setTimeScale(this.speed);

            this.simulationTimer.setTimerAmount(Math.round(60000 / this.speed));

        });

        this.timeLabel = new Label("Time in simulation: " + this.npcManager.getTimestamp().toString());

        this.timeLabel.setAlignment(Pos.TOP_RIGHT);

        this.timeLabel.setPadding(new Insets(0, 20, 0, 20));

        this.skipButton = new Button("Skip to next event");
        skipButton.setOnAction((event ->
            this.npcManager.setSkip(true)
        ));

        this.toolBar.getItems().addAll(playButton, pauseButton, this.backButton, this.timeLabel, timeScaleSlider, multiplyLabel, skipButton);
    }

    private void updateTimeLabel() {
        this.timeLabel.setText("Time in simulation: " + this.npcManager.getTimestamp().toString());
    }

    /**
     * The animation timer handler will call the update 60 times a second to update all the entities.
     */
    private void update() {
        if (simulationTimer.timeout()) {
            this.npcManager.addMinute(1);
            this.simulationTimer.reset();
        }

        for (int i = 0; i < this.speed; i++) {
            this.npcManager.update();
            this.updateTimeLabel();
        }

        this.skipButton.setDisable(!this.npcManager.ableToSkip());
    }

    /**
     * the time between updates the draw function will be called, this is for the 60+ hz screens.
     *
     * @param graphics the graphics from the canvas.
     */
    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(this.camera.getCameraTransform());
        graphics.setBackground(Color.WHITE);
        graphics.clearRect((int) -canvas.getWidth(), (int) -canvas.getHeight(), (int) canvas.getWidth() * 3, (int) canvas.getHeight() * 5);

        this.schoolMap.drawLayers(this.canvas.getGraphicsContext2D());
        this.npcManager.draw(this.canvas.getGraphicsContext2D());
    }

    /**
     * The start method will start the animation timer, also the states of the simulation is changed to true here.
     */
    public void start() {

        this.isRunning = true;

        this.animationTimer.start();
    }

    /**
     * The start method will stop the animation timer, also the states of the simulation is changed to false here.
     */
    private void stop() {
        this.isRunning = false;
    }

    /**
     * the quit method is to reset the simulator to re-run.
     */
    public void quit() {

        this.isRunning = false;

        this.npcManager.clearAllNpc();

        this.animationTimer.stop();
    }
}
