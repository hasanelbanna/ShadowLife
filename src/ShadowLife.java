/*
 * Project 2B for Object Oriented Software Development(SWEN20003).
 *
 * Acknowledgement goes to Eleanor McMurty who designed the simulation and
 * provided the sample solution for Project 1 for continuation of ShadowLife.
 *
 * @author Sheikh Hasan Al Banna Ohi, October, 2020.
 * @studentID 1118853
 */

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
 * Main class for the game.
 * Reading from Command-Line arguments and Renders the actors.
 */
public class ShadowLife extends AbstractGame {

    private static final int TILE_SIZE = 64;
    private long lastTick = 0;
    private static long TICK_TIME;
    private static long MAX_TICKS;
    private static int tickTracker = 0;
    private static int totalGatherers = 0;
    private static boolean gathererDrown = false;
    private static boolean thiefDrown = false;
    private static int totalGathererActive = 0;
    private static int gathererTicks = 0;
    private static int totalThieves = 0;
    private static int totalThiefActive = 0;
    private static int thiefTicks = 0;
    private final Image background = new Image("res/images/background.png");
    private static ArrayList<Actor> actorList = new ArrayList<>();

    public static int getTotalThiefActive() {
        return totalThiefActive;
    }
    public static void setTotalThiefActive(int totalThiefActive) {
        ShadowLife.totalThiefActive = totalThiefActive;
    }
    public static int getTotalGathererActive() {
        return totalGathererActive;
    }
    public static void setTotalGathererActive(int totalGathererActive) {
        ShadowLife.totalGathererActive = totalGathererActive;
    }
    public static int getTileSize() {
        return TILE_SIZE;
    }
    public static ArrayList<Actor> getActorList() {
        return actorList;
    }


    private void loadActors() {

        String[] commands = argsFromFile();
        assert commands != null;
        if (commands.length != 3) { exitShadowLife(); }

        TICK_TIME = Integer.parseInt(commands[0]);
        MAX_TICKS = Integer.parseInt(commands[1]);

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

                switch (type) {
                    case Tree.TYPE_TREE:
                        actorList.add(new Tree("res/images/tree.png", "Tree", x ,y));
                        break;
                    case Tree.TYPE_Golden_Tree:
                        actorList.add(new Tree("res/images/gold-tree.png", "GoldenTree",x ,y));
                        break;
                    case Stockpile.TYPE:
                        actorList.add(new Stockpile(x, y));
                        break;
                    case Hoard.TYPE:
                        actorList.add(new Hoard(x, y));
                        break;
                    case NonLivingActor.TYPE_PAD:
                        actorList.add(new NonLivingActor("res/images/pad.png", "Pad", x, y));
                        break;
                    case NonLivingActor.TYPE_FENCE:
                        actorList.add(new NonLivingActor("res/images/fence.png", "Fence", x ,y));
                        break;
                    case NonLivingActor.TYPE_LEFT:
                        actorList.add(new NonLivingActor("res/images/left.png", "SignLeft", x, y));
                        break;
                    case NonLivingActor.TYPE_RIGHT:
                        actorList.add(new NonLivingActor("res/images/right.png", "SignRight", x, y));
                        break;
                    case NonLivingActor.TYPE_UP:
                        actorList.add(new NonLivingActor("res/images/up.png", "SignUp", x, y));
                        break;
                    case NonLivingActor.TYPE_DOWN:
                        actorList.add(new NonLivingActor("res/images/down.png", "SignDown", x, y));
                        break;
                    case NonLivingActor.TYPE_POOL:
                        actorList.add(new NonLivingActor("res/images/pool.png", "Pool", x, y));
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
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public ShadowLife() {
        loadActors();
    }

    @Override
    protected void update(Input input) {

        int newX;
        int newY;
        ArrayList<Actor> newGatherers = new ArrayList<>();
        ArrayList<Actor> removalListGatherer = new ArrayList<>();
        ArrayList<Actor> newThieves = new ArrayList<>();
        ArrayList<Actor> removalListThief = new ArrayList<>();
        boolean gathererExists = false;
        boolean thiefExists = false;


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

        if (gathererExists && !thiefExists && totalGathererActive == 0) {
            Window.close();
        } else if (!gathererExists && thiefExists && totalThiefActive == 0) {
            Window.close();
        } else if (totalThiefActive == 0 && totalGathererActive == 0) {
            Window.close();
        }



        if (System.currentTimeMillis() - lastTick > TICK_TIME) {
            lastTick = System.currentTimeMillis();

            for (Actor actor : actorList) {

                if (actor.type.equals("Gatherer")) {
                    Gatherer gatherer = (Gatherer) actor;
                    if (gatherer.isOnPool()) {
                        continue;
                    }

                    if (!gatherer.isActive()) {
                        gatherer.tick();
                        gathererTicks++;
                    }
                }

                if (actor.type.equals("Thief")) {
                    Thief thief = (Thief) actor;
                    if (thief.isOnPool()) {
                        continue;
                    }

                    if (!thief.isActive()) {
                        thief.tick();
                        thiefTicks++;
                    }
                }

            }
            tickTracker++;

        }


        // Draw all the actors, the number of fruits and thief's status
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



        for (Actor actor : actorList) {
            if (actor.type.equals("Gatherer")) {
                Gatherer gatherer = (Gatherer) actor;
                if (gatherer.isOnPool()) {
                    removalListGatherer.add(gatherer);
                    gathererDrown = true;
                    newX = gatherer.getX();
                    newY = gatherer.getY();
                    Gatherer a = new Gatherer(newX, newY, gatherer.getDirection());
                    a.rotateNinetyClockwise();
                    Gatherer b = new Gatherer(newX, newY, gatherer.getDirection());
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
                    Thief a = new Thief(newX, newY, thief.getDirection());
                    a.rotateNinetyClockwise();
                    Thief b = new Thief(newX, newY, thief.getDirection());
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
        }

        if (thiefDrown) {
            actorList.removeAll(removalListThief);
            actorList.addAll(newThieves);
            totalThieves += 2;
            totalThiefActive += 2;
        }
    }


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
                Stockpile stockpile = (Stockpile) actor;
                System.out.println(stockpile.getFruit());
            }

        }

        for (Actor actor : ShadowLife.actorList) {
            if (actor.type.equals("Hoard")) {
                Hoard hoard = (Hoard) actor;
                System.out.println(hoard.getFruit());
            }
        }
    }


    private static String[] argsFromFile() {
        try {
            return Files.readString(Path.of("args.txt"), Charset.defaultCharset()) .split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void exitShadowLife() {
        System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
        System.exit(-1);
    }
}
