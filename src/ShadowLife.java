/**
 * Project 2B for Object Oriented Software Development(SWEN20003).
 *
 * Acknowledgement goes to Eleanor McMurty who designed the simulation and
 * provided the sample solution for Project 1 for continuation of ShadowLife.
 * This class contains lines from the provided solution.
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

    public static final int TILE_SIZE = 64;
    private long lastTick = 0;
    private static long TICK_TIME;
    private static long MAX_TICKS;
    private static int tickTracker = 0;

    private static int totalGatherers = 0;
    private static int totalGathererActive = 0;
    private static int gathererTicks = 0;

    private static int totalThieves = 0;
    private static int totalThiefActive = 0;
    private static int thiefTicks = 0;
    private final Image background = new Image("res/images/background.png");

    /**
     * Below are the arraylists that was used in the Dynamic actors movement
     */

    public static ArrayList<Actor> actorList = new ArrayList<>();

    public static void addToTotalGatherers() {
        totalGatherers++;
        totalGathererActive++;
    }

    public static void addToTotalThieves() {
        totalThieves++;
        totalThiefActive++;
    }

    private static void setTickTime(long tickTime) {
        TICK_TIME = tickTime;
    }

    private static void setMaxTicks(long maxTicks) {
        MAX_TICKS = maxTicks;
    }

    private void loadActors() {

        String[] commands = argsFromFile();
        if (commands.length != 3) { exitShadowLife(); }

        setTickTime(Integer.parseInt(commands[0]));
        setMaxTicks(Integer.parseInt(commands[1]));
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
                    case Tree.TYPE:
                        actorList.add(new Tree(x,y));
                        break;
                    case GoldenTree.TYPE:
                        actorList.add(new GoldenTree(x,y));
                        break;
                    case Stockpile.TYPE:
                        actorList.add(new Stockpile(x,y));
                        break;
                    case Hoard.TYPE:
                        actorList.add(new Hoard(x,y));
                        break;
                    case Pad.TYPE:
                        actorList.add(new Pad(x,y));
                        break;
                    case Fence.TYPE:
                        actorList.add(new Fence(x,y));
                        break;
                    case Sign.TYPE_LEFT:
                        actorList.add(new Sign("res/images/left.png", "SignLeft", x, y));
                        break;
                    case Sign.TYPE_RIGHT:
                        actorList.add(new Sign("res/images/right.png", "SignRight", x, y));
                        break;
                    case Sign.TYPE_UP:
                        actorList.add(new Sign("res/images/up.png", "SignUp", x,y));
                        break;
                    case Sign.TYPE_DOWN:
                        actorList.add(new Sign("res/images/down.png", "SignDown", x,y));
                        break;
                    case Pool.TYPE:
                        actorList.add(new Pool(x,y));
                        break;
                    case Gatherer.TYPE:
                        actorList.add(new Gatherer(x,y));
                        totalGatherers++;
                        totalGathererActive++;
                        break;
                    case Thief.TYPE:
                        actorList.add(new Thief(x,y));
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
        // Halt the simulation as the dynamic actors reaches the fence
        //or total ticks exceeds the maximum number provided.
        if (totalThiefActive < 1 && totalGathererActive < 1) {
            Window.close();
        }
        tickTracker++;
        if(tickTracker > MAX_TICKS*TICK_TIME) {
            Window.close();
        }

        if (System.currentTimeMillis() - lastTick > TICK_TIME) {
            lastTick = System.currentTimeMillis();

            for (Actor actor : actorList) {

                if (actor.type.equals("Gatherer")) {
                    Gatherer gatherer = (Gatherer) actor;
                    if(!gatherer.isActive()) {
                        totalGathererActive--;
                        continue;
                    }
                    gatherer.tick();
                    gathererTicks++;
                }

                if (actor.type.equals("Thief")) {
                    Thief thief = (Thief) actor;
                    if(!thief.isActive()){
                        totalThiefActive--;
                        continue;
                    }
                    thief.tick();
                    thiefTicks++;
                }

            }

        }

        // Draw all the actors, the number of fruits and thief's status
        background.drawFromTopLeft(0, 0);

        for (Actor actor : actorList) {
            actor.render();
            if (actor.type.equals("Tree") || actor.type.equals("Stockpile") || actor.type.equals("Hoard")){
                actor.update();
            }
            if (actor.type.equals("Thief")) {
                Thief thief = (Thief) actor;
                thief.renderStatus();
            }
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
        } catch (IOException e) { e.printStackTrace();
        }
        return null;
    }


    private static void exitShadowLife() {
        System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
        System.exit(-1);
    }
}
