import bagel.AbstractGame;
import bagel.Image;
import bagel.Input;
import bagel.Window;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Project 2B for Object Oriented Software Development(SWEN20003).
 *
 * Acknowledgement goes to Eleanor McMurty who designed the simulation and
 * provided the sample solution to Project 1 for continuation of ShadowLife.
 *
 * @author Hasan Al Banna Ohi (studentID 1118853), October, 2020.
 * @version 14.0.2
 */
public class ShadowLife extends AbstractGame {

    /* Background image location of the simulation world */
    private final Image background = new Image("res/images/background.png");
    /* Different variables to track the tick count */
    private long lastTick = 0;
    private static long TICK_TIME;
    private static long MAX_TICKS;
    private static int tickTracker = 0;
    /* Variables to track the gatherer activities */
    private static int totalGatherers = 0;
    private static boolean gathererDrown = false;
    private static int gathererTicks = 0;
    private static int totalGathererActive = 0;
    /* Variables to track the thief activities */
    private static int totalThieves = 0;
    private static int totalThiefActive = 0;
    private static boolean thiefDrown = false;
    private static int thiefTicks = 0;
    /* ArrayList to keep track of the actors in the simulation */
    private static final ArrayList<Actor> actorList = new ArrayList<>();

    /**
     * Method to make the actorList available for other actors to use
     *
     * @return the arrayList conataining all the actors
     */
    public static ArrayList<Actor> getActorList() {
        return actorList;
    }

    /**
     * Method to get the total number of active gatherers
     *
     * @return total active gatherer
     */
    public static int getTotalGathererActive() {
        return totalGathererActive;
    }

    /**
     * Method to increase or decrease of the number of total active gatherers
     *
     * @param totalGathererActive new number of active gatherers
     */
    public static void setTotalGathererActive(int totalGathererActive) {
        ShadowLife.totalGathererActive = totalGathererActive;
    }

    /**
     * Method to get the total number of active thieves
     *
     * @return total active thieves
     */
    public static int getTotalThiefActive() {
        return totalThiefActive;
    }

    /**
     * Method to increase or decrease of the number of total active thieves
     *
     * @param totalThiefActive new number of active thieves
     */
    public static void setTotalThiefActive(int totalThiefActive) {
        ShadowLife.totalThiefActive = totalThiefActive;
    }

    /**
     * Method to read the CSV file containing information about the world and add the actors in the actorList
     */
    private void loadActors() {

        String[] commands = argsFromFile();
        assert commands != null;
        if (commands.length != 3) { exitShadowLife(); }

        TICK_TIME = Integer.parseInt(commands[0]);
        MAX_TICKS = Integer.parseInt(commands[1]);

        /* Exit shadowLife for the wrong command line arguments */
        if (TICK_TIME < 0 || MAX_TICKS < 0) { exitShadowLife();
        }

        String worldFile = commands[2];

        try (BufferedReader reader = new BufferedReader(new FileReader(worldFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Line format is: type,x,y
                String[] parts = line.split(",");
                String type = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                // Add different actors to the actorList after instantiating based on its type
                switch (type) {
                    case StaticActor.TYPE_TREE:
                        actorList.add(new StaticActor("res/images/tree.png", "Tree", x ,y, 3));
                        break;
                    case StaticActor.TYPE_Golden_Tree:
                        actorList.add(new StaticActor("res/images/gold-tree.png", "GoldenTree",x ,y));
                        break;
                    case StaticActor.TYPE_STOCKPILE:
                        actorList.add(new StaticActor("res/images/cherries.png", "Stockpile" , x, y, 0));
                        break;
                    case StaticActor.TYPE_HOARD:
                        actorList.add(new StaticActor("res/images/hoard.png", "Hoard", x, y, 0));
                        break;
                    case StaticActor.TYPE_PAD:
                        actorList.add(new StaticActor("res/images/pad.png", "Pad", x, y));
                        break;
                    case StaticActor.TYPE_FENCE:
                        actorList.add(new StaticActor("res/images/fence.png", "Fence", x ,y));
                        break;
                    case StaticActor.TYPE_LEFT:
                        actorList.add(new StaticActor("res/images/left.png", "SignLeft", x, y));
                        break;
                    case StaticActor.TYPE_RIGHT:
                        actorList.add(new StaticActor("res/images/right.png", "SignRight", x, y));
                        break;
                    case StaticActor.TYPE_UP:
                        actorList.add(new StaticActor("res/images/up.png", "SignUp", x, y));
                        break;
                    case StaticActor.TYPE_DOWN:
                        actorList.add(new StaticActor("res/images/down.png", "SignDown", x, y));
                        break;
                    case StaticActor.TYPE_POOL:
                        actorList.add(new StaticActor("res/images/pool.png", "Pool", x, y));
                        break;
                    case Gatherer.TYPE:
                        actorList.add(new Gatherer(x, y));
                        totalGatherers++;
                        totalGathererActive++;
                        break;
                    case Thief.TYPE:
                        actorList.add(new Thief(x, y));
                        totalThieves++;
                        totalThiefActive++;
                        break;
                }
            }
        } catch (IOException e) {
            // Exit the program for any exception
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Load the actors to participate in the simulation
     */
    public ShadowLife() {
        loadActors();
    }

    /**
     * Update the state of the game, reading from input
     *
     * @param input The current state based on the tick
     */
    @Override
    protected void update(Input input) {

        /* integer and arraylist variables to keep track of the new gatherers created */
        int newX;
        int newY;
        ArrayList<Actor> newGatherers = new ArrayList<>();
        ArrayList<Actor> removalListGatherer = new ArrayList<>();
        ArrayList<Actor> newThieves = new ArrayList<>();
        ArrayList<Actor> removalListThief = new ArrayList<>();
        /* Check if gatherer or thief or both exist in the simulation world */
        boolean gathererExists = false;
        boolean thiefExists = false;

        /* Close the window if max ticks has been elapsed */
        if (tickTracker > MAX_TICKS) {
            Window.close();
        }

        for (Actor actor : actorList) {
            if (actor instanceof Gatherer) {
                gathererExists = true;
                break;
            }
        }

        for (Actor actor : actorList) {
            if (actor instanceof Thief) {
                thiefExists = true;
                break;
            }
        }

        /* Close the window when all the dynamic actors are set to rest */
        if (gathererExists && !thiefExists && totalGathererActive == 0) {
            Window.close();
        } else if (!gathererExists && thiefExists && totalThiefActive == 0) {
            Window.close();
        } else if (totalThiefActive == 0 && totalGathererActive == 0) {
            Window.close();
        }

        /* Backbone of the program: proceed with every tick */
        if (System.currentTimeMillis() - lastTick > TICK_TIME) {
            lastTick = System.currentTimeMillis();

            for (Actor actor : actorList) {

                if (actor.type.equals("Gatherer")) {
                    Gatherer gatherer = (Gatherer) actor;
                    if (gatherer.isOnPool()) {
                        continue;
                    }

                    if (gatherer.isActive()) {
                        gatherer.tick();
                        gathererTicks++;
                    }
                }

                if (actor.type.equals("Thief")) {
                    Thief thief = (Thief) actor;
                    if (thief.isOnPool()) {
                        continue;
                    }

                    if (thief.isActive()) {
                        thief.tick();
                        thiefTicks++;
                    }
                }

            }
            tickTracker++;

        }


        /* Draw all the actors, the number of fruits and thief's status */
        background.drawFromTopLeft(0, 0);

        for (Actor actor : actorList) {

            if (actor.type.equals("Gatherer")) {
                Gatherer gatherer = (Gatherer) actor;
                if (gatherer.isOnPool()) {
                    continue;
                }
            }

            if (actor.type.equals("Thief")) {
                Thief thief = (Thief) actor;
                if (thief.isOnPool()) {
                    continue;
                }
            }

            actor.render();
            if (actor.type.equals("Tree") || actor.type.equals("Stockpile") || actor.type.equals("Hoard")) {
                actor.update();
            }
            if (actor.type.equals("Thief")) {
                Thief thief = (Thief) actor;
                thief.renderStatus();
            }
        }


        /* If a dynamic actor standing on a pool, destroy it and create 2 new ones and rotate them
            90 Degree and -90 Degree based on the direction of the destroyed gatherer */
        for (Actor actor : actorList) {
            if (actor.type.equals("Gatherer")) {
                Gatherer gatherer = (Gatherer) actor;
                if (gatherer.isOnPool()) {
                    removalListGatherer.add(gatherer);
                    gathererDrown = true;
                    newX = gatherer.getX();
                    newY = gatherer.getY();
                    Gatherer a = new Gatherer(newX, newY);
                    a.setDirection(gatherer.getDirection());
                    a.rotateNinetyClockwise();
                    Gatherer b = new Gatherer(newX, newY);
                    b.setDirection(gatherer.getDirection());
                    b.rotateNinetyAntiClockwise();
                    newGatherers.add(a);
                    newGatherers.add(b);
                }
            }

            if(actor.type.equals("Thief")) {
                Thief thief = (Thief) actor;
                if (thief.isOnPool()) {
                    removalListThief.add(thief);
                    thiefDrown = true;
                    newX = thief.getX();
                    newY = thief.getY();
                    Thief a = new Thief(newX, newY);
                    a.setDirection(thief.getDirection());
                    a.rotateNinetyClockwise();
                    Thief b = new Thief(newX, newY);
                    b.setDirection(thief.getDirection());
                    b.rotateNinetyAntiClockwise();
                    newThieves.add(a);
                    newThieves.add(b);
                }
            }
        }

        if (gathererDrown) {
            actorList.removeAll(removalListGatherer);
            actorList.addAll(newGatherers);
            totalGatherers += 2;
            totalGathererActive += 2;
            gathererDrown = false;
        }

        if (thiefDrown) {
            actorList.removeAll(removalListThief);
            actorList.addAll(newThieves);
            totalThieves += 2;
            totalThiefActive += 2;
            thiefDrown = false;
        }
    }

    /**
     * Main method for the simulation.
     *
     * Creates the simulation and runs it. Reads three command line arguments: tick length, max ticks and the simulation
     * world file. Also prints the status of ticks and fruits.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        new ShadowLife().run();

        int tickToPrint = 1;

        if (totalThieves > 0 && totalGatherers > 0) {
            tickToPrint += Math.max(thiefTicks / totalThieves, gathererTicks / totalGatherers);
        } else if (totalThieves > 0) {
            tickToPrint +=  thiefTicks / totalThieves;
        } else if (totalGatherers > 0) {
            tickToPrint += gathererTicks / totalGatherers;
        } else {
            tickToPrint = 0;
        }

        System.out.println(tickToPrint + " ticks");

        for (Actor actor : ShadowLife.actorList) {
            if (actor.type.equals("Stockpile")) {
                StaticActor stockpile = (StaticActor) actor;
                System.out.println(stockpile.getFruit());
            }

        }

        for (Actor actor : ShadowLife.actorList) {
            if (actor.type.equals("Hoard")) {
                StaticActor hoard = (StaticActor) actor;
                System.out.println(hoard.getFruit());
            }
        }
    }

    /**
     * Helper method to read the command line arguments, deals with a specific bug on MacOS
     *
     * @return the command line argument containing the tick length, max ticks and world file
     */
    private static String[] argsFromFile() {
        try {
            return Files.readString(Path.of("args.txt"), Charset.defaultCharset()) .split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to exit the simulation for any wrong command line arguments
     */
    private static void exitShadowLife() {
        System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
        System.exit(-1);
    }
}
