package schoolplanner.simulator.npc.controllers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jfree.fx.FXGraphics2D;
import schoolplanner.calendar.Schedule;
import schoolplanner.calendar.lessons.Lesson;
import schoolplanner.calendar.lessons.npc.Teacher;
import schoolplanner.simulator.map.SchoolMap;
import schoolplanner.simulator.npc.Npc;
import schoolplanner.simulator.npc.types.UniversalNpc;
import schoolplanner.util.Coordinate;
import schoolplanner.util.SpawnMoment;
import schoolplanner.util.TimeStamp;
import schoolplanner.util.Timer;
import java.util.*;

public class NpcManager {

    private static final int DEFAULT_NPC_SPEED = 1;
    private double timeScale;
    private boolean skip;

    private Timer spawnTimer;
    private TimeStamp timestamp;
    private Schedule schedule;
    private ArrayList<Npc> exitList;
    private HashMap<String, Npc> teachers;
    private HashMap<String, ArrayList<Npc>> groups;
    private HashMap<SpawnMoment, Queue<Npc>> spawnQueue;
    private ArrayList<Npc> npcsOnScreen;
    private Random random;
    private SchoolMap schoolMap;
    private PathFindingContainer pathFindingContainer;
    private SpriteController spriteController;
    private CollisionController collisionController;


    public NpcManager(Schedule schedule, SchoolMap schoolMap) {

        this.skip = false;
        this.timeScale = 1;
        this.groups = new HashMap<>();
        this.random = new Random();
        this.exitList = new ArrayList<>();
        this.teachers = new HashMap<>();
        this.npcsOnScreen = new ArrayList<>();
        this.spawnQueue = new HashMap<>();
        this.pathFindingContainer = new PathFindingContainer(schoolMap);
        this.spriteController = new SpriteController();
        this.collisionController = new CollisionController(new ArrayList<>());
        this.spawnTimer = new Timer((int) (1000 / (this.timeScale)));
        this.timestamp = new TimeStamp(8, 25);

        this.schoolMap = schoolMap;
        this.schedule = schedule;

        this.collisionController.getWalls().addAll(this.schoolMap.getMapLoader().getWallCollisionArea());

        this.generateSpawnQueue();
    }


    /**
     * this method will generate a hashMap with every key being the spawn timeStamp.
     * The value is a queue filled with npc.
     */
    private void generateSpawnQueue() {

        double speed = DEFAULT_NPC_SPEED;
        Image npcAvatar;

        for (String key : schedule.getGroups().keySet()) {
            ArrayList<Npc> tempArray = new ArrayList<>();
            for (int i = 0; i < schedule.getGroups().get(key).getSize(); i++) {

                int spriteSet = random.nextInt(4);

                npcAvatar = spriteController.getSpriteSets().get(spriteSet).getDownIdle();

                Coordinate tempCoordinate = this.schoolMap.getTileLocation(1, 43 + random.nextInt(3));

                UniversalNpc universalNpc = new UniversalNpc(speed, pathFindingContainer, spriteSet,
                        tempCoordinate, npcAvatar, spriteController, this.collisionController);

                tempArray.add(universalNpc);
            }

            TimeStamp firstLesson = new TimeStamp(this.schedule.getFirstLesson(key).getStartTime());

            firstLesson.addMinutes(-5);

            this.spawnQueue.put(new SpawnMoment(firstLesson, key), new LinkedList<>(tempArray));
        }

        //the following code is responsible for putting all the teachers of the day in the spawn queue
        ArrayList<Teacher> counter = new ArrayList<>();
        for (Lesson lesson : this.schedule.getLessons()) {
            if (!counter.contains(lesson.getTeacher())) {
                counter.add(lesson.getTeacher());

                LinkedList<Npc> teachersList = new LinkedList<>();
                int spriteSet = 4;

                npcAvatar = spriteController.getSpriteSets().get(spriteSet).getDownIdle();

                Coordinate tempCoordinate = this.schoolMap.getTileLocation(1, 43 + random.nextInt(3));
                UniversalNpc universalNpc = new UniversalNpc(speed, pathFindingContainer, spriteSet,
                        tempCoordinate, npcAvatar, spriteController, this.collisionController, false);

                teachersList.add(universalNpc);
                this.spawnQueue.put(new SpawnMoment(new TimeStamp(8, 30), lesson.getTeacher().getName()), teachersList);
            }
        }
    }

    /**
     * this method will update all the npc's and will check if there is spawning needed;
     */
    public void update() {

        this.spawnCheck();

        for (Npc npc : this.npcsOnScreen) {
            npc.update();
        }

        //This list gets filled with npc's that have no next lesson,
        //so they can clear off the screen when they arrive at the exit.
        if (!this.exitList.isEmpty()) {
            for (Npc npc : this.exitList) {
                if (npc.isArrived()) {
                    this.npcsOnScreen.remove(npc);
                    this.collisionController.getNpcs().remove(npc.getHitBox());
                }
                if (this.npcsOnScreen.isEmpty()) {
                    this.clearAllNpc();
                    return;
                }
            }
        }

        //the value skip if true will add minutes to itself to speed up the time.
        //When there is any movement by npc's the skip will be set false;
        if (skip) {
            this.addMinute(1);
        }
    }

    /**
     * this method will check if the time equals any of the spawn times in the spawn queue.
     */
    private void spawnCheck() {
        boolean needSpawning = false;

        for (SpawnMoment spawnMoment : this.spawnQueue.keySet()) {
            if (!this.spawnQueue.get(spawnMoment).isEmpty()) {
                needSpawning = true;
                break;
            }
        }

        if (needSpawning) {
            for (SpawnMoment spawnMoment : this.spawnQueue.keySet()) {
                if (!spawnMoment.getTimeStamp().isLaterThan(this.timestamp) && spawnMoment.getTimeStamp().timeBetween(this.timestamp).getMinutes() < 5) {
                    if (!this.spawnQueue.get(spawnMoment).isEmpty() && this.spawnTimer.timeout()) {

                        Npc npc = this.spawnQueue.get(spawnMoment).remove();

                        this.npcsOnScreen.add(npc);
                        this.collisionController.getNpcs().add(npc.getHitBox());

                        if (npc.isStudent()) {
                            if (!this.groups.containsKey(spawnMoment.getGroup())) {
                                this.groups.put(spawnMoment.getGroup(), new ArrayList<>());
                            }
                            this.groups.get(spawnMoment.getGroup()).add(npc);
                            npc.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("Canteen"), spawnMoment.getGroup());
                        } else {
                            this.teachers.put(spawnMoment.getGroup(), npc);
                            npc.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("TeacherRoom"), spawnMoment.getGroup());
                        }

                        this.skip = false;
                        this.spawnTimer.reset();
                    }
                }
            }
        }
    }

    /**
     * updateNpcCommand will update all the npc destinations if needed.
     */
    private void updateNpcCommand() {

        for (String groupName : this.groups.keySet()) {
            for (Lesson lesson : this.schedule.getLessons()) {
                if (lesson.getGroup().getGroupName().equals(groupName)) {
                    for (Npc npc : this.groups.get(groupName)) {

                        if (this.timestamp.timeBetween(lesson.getStartTime()).getHours() == 0 && this.timestamp.timeBetween(lesson.getStartTime()).getMinutes() == 3) {

                            if (npc.getCurrentDestination() == null ||
                                    !npc.getCurrentDestination().contains(this.schoolMap.getMapLoader().getSimulatorRooms().get(lesson.getClassRoom().getName()).getName()) ||
                                    npc.getCurrentDestination().contains("canteen")) {

                                this.skip = false;
                                npc.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get(lesson.getClassRoom().getName()), lesson.getGroup().getGroupName());

                                for (String teacherName : this.teachers.keySet()) {
                                    if (lesson.getTeacher().getName().equals(teacherName)) {
                                        this.teachers.get(teacherName).setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get(lesson.getClassRoom().getName()), "Teacher");
                                    }
                                }
                            }

                        } else if (this.timestamp.getMinutes() == lesson.getEndTime().getMinutes() &&
                                this.timestamp.getHours() == lesson.getEndTime().getHours()) {
                            if (npc.getCurrentDestination() == null || !npc.getCurrentDestination().contains(this.schoolMap.getMapLoader().getSimulatorRooms().get("Canteen").getName())) {

                                this.skip = false;

                                if (this.schedule.getLastLesson(groupName).getEndTime().getMinutes() == lesson.getEndTime().getMinutes() &&
                                        this.schedule.getLastLesson(groupName).getEndTime().getHours() == lesson.getEndTime().getHours()) {
                                    npc.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("Exit"), lesson.getGroup().getGroupName());
                                    if (!this.exitList.contains(npc)) {
                                        this.exitList.add(npc);
                                    }
                                } else {
                                    npc.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("Canteen"), lesson.getGroup().getGroupName());
                                }

                                for (String teacherName : this.teachers.keySet()) {
                                    if (lesson.getTeacher().getName().equals(teacherName)) {
                                        if (this.schedule.getLastLessonTeacher(teacherName).getEndTime().getMinutes() == lesson.getEndTime().getMinutes() &&
                                                this.schedule.getLastLessonTeacher(teacherName).getEndTime().getHours() == lesson.getEndTime().getHours()) {

                                            Npc teacher = this.teachers.get(teacherName);
                                            teacher.setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("Exit"), teacherName);

                                            if (!this.exitList.contains(teacher)) {
                                                this.exitList.add(teacher);
                                            }

                                        } else {
                                            this.teachers.get(teacherName).setDestination(this.schoolMap.getMapLoader().getSimulatorRooms().get("TeacherRoom"), teacherName);
                                        }
                                    }
                                }
                            }
                        }

                        npc.update();

                    }
                }

            }
        }
    }

    /**
     * this method is called by the simulator every specific time.
     *
     * @param amount is the amount of minuted added.
     */
    public void addMinute(int amount) {

        this.timestamp.addMinutes(amount);

        this.updateNpcCommand();

    }

    public TimeStamp getTimestamp() {
        return timestamp;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
        this.spawnTimer.setTimerAmount((int) (1000 / (this.timeScale * 2)));
    }

    /**
     * when the back button is clicked, or the simulation has no npc's left (end of the day), this method will be called
     * to clear all the data for a fresh start.
     */
    public void clearAllNpc() {
        this.groups.clear();
        this.npcsOnScreen.clear();
        this.exitList.clear();
        this.teachers.clear();
        this.timestamp = new TimeStamp(8, 25);
        this.generateSpawnQueue();
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    /**
     * method is used to disable the skip button when it can't be used.
     *
     * @return true if all the npc's are arrived. false if not;
     */
    public boolean ableToSkip() {
        for (Npc npc : npcsOnScreen) {
            if (!npc.isArrived()) {
                return false;
            }
        }
        return true;
    }

    /**
     * method to draw all the npc's;
     *
     * @param context the graphics from the canvas.
     */
    public void draw(GraphicsContext context) {
        for (Npc npc : this.npcsOnScreen) {
            npc.draw(context);
        }
    }
}
