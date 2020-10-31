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
    private Actor[] actors;
    private static int totalGathererActive = 0;
    private static int totalThiefActive = 0;
    private static int tickTracker = 0;
    private static int gathererTicks = 0;
    private static int thiefTicks = 0;
    private final Image background = new Image("res/images/background.png");

    /**
     * Below are the arraylists that was used in the Dynamic actors movement
     */
    public static ArrayList<GoldenTree> goldenTreeArrayList = new ArrayList<>();
    public static ArrayList<Pad> padArrayList = new ArrayList<>();
    public static ArrayList<Thief> thiefArrayList = new ArrayList<>();
    public static ArrayList<Hoard> hoardArrayList = new ArrayList<>();
    public static ArrayList<Stockpile> stockpileArrayList = new ArrayList<>();
    public static ArrayList<Gatherer> gathererArrayList = new ArrayList<>();
    public static ArrayList<Fence> fences = new ArrayList<>();
    public static ArrayList<SignDown> signDownArrayList = new ArrayList<>();
    public static ArrayList<SignUp> signUpArrayList = new ArrayList<>();
    public static ArrayList<SignRight> signRightArrayList = new ArrayList<>();
    public static ArrayList<SignLeft> signLeftArrayList = new ArrayList<>();
    public static ArrayList<Tree> treeArrayList = new ArrayList<>();


    private static void setTickTime(long tickTime) {
        TICK_TIME = tickTime;
    }

    private static void setMaxTicks(long maxTicks) {
        MAX_TICKS = maxTicks;
    }

    /**
     * Read the actors and creates corresponding objects from a csv file
     * provided via command line arguments along with tick rates and maximum numbers of tick.
     */
    private void loadActors() {
        int count = 0;

        String[] commands = argsFromFile();
        if (commands.length != 3) { exitShadowLife(); }

        setTickTime(Integer.parseInt(commands[0]));
        setMaxTicks(Integer.parseInt(commands[1]));
        if (TICK_TIME < 0 || MAX_TICKS < 0) { exitShadowLife();
        }

        String worldFile = commands[2];

        try (BufferedReader reader = new BufferedReader(new FileReader(worldFile))) {

            int size = (int) Files.lines(Path.of(worldFile)).count();
            actors = new Actor[size];

            String line;
            while ((line = reader.readLine()) != null) {
                // Line format is: type,x,y
                String[] parts = line.split(",");
                String type = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                switch (type) {
                    case Tree.TYPE:
                        treeArrayList.add(new Tree(x,y));
                        count++;
                        break;
                    case GoldenTree.TYPE:
                        goldenTreeArrayList.add(new GoldenTree(x, y));
                        actors[count++] = new GoldenTree(x, y);
                        break;
                    case Stockpile.TYPE:
                        stockpileArrayList.add(new Stockpile(x, y));
                        count++;
                        break;
                    case Hoard.TYPE:
                        hoardArrayList.add(new Hoard(x, y));
                        count++;
                        break;
                    case Pad.TYPE:
                        padArrayList.add(new Pad(x, y));
                        actors[count++] = new Pad(x, y);
                        break;
                    case Fence.TYPE:
                        actors[count++] = new Fence(x, y);
                        fences.add(new Fence(x, y));
                        break;
                    case SignLeft.TYPE:
                        actors[count++] = new SignLeft(x, y);
                        signLeftArrayList.add(new SignLeft(x, y));
                        break;
                    case SignRight.TYPE:
                        actors[count++] = new SignRight(x, y);
                        signRightArrayList.add(new SignRight(x,y));
                        break;
                    case SignUp.TYPE:
                        actors[count++] = new SignUp(x, y);
                        signUpArrayList.add(new SignUp(x,y));
                        break;
                    case SignDown.TYPE:
                        actors[count++] = new SignDown(x, y);
                        signDownArrayList.add(new SignDown(x,y));
                        break;
                    case Pool.TYPE:
                        actors[count++] = new Pool(x, y);
                        break;
                    case Gatherer.TYPE:
                        count++;
                        totalGathererActive++;
                        gathererArrayList.add(new Gatherer(x, y));
                        break;
                    case Thief.TYPE:
                        thiefArrayList.add(new Thief(x, y));
                        totalThiefActive++;
                        count++;
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

    /** Update the state of the game for each ticks and render them on the screen.
     *
     * @param input
     */
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

            for (Actor actor : actors) {
                if (actor != null) {
                    actor.tick();
                }
            }

            for (Gatherer gatherer : gathererArrayList) {
                if (gatherer != null) {
                    if(!gatherer.isActive()) {
                        totalGathererActive--;
                        continue;
                    }
                    gatherer.tick();
                    gathererTicks++;

                }
            }
            for (Thief thief : thiefArrayList) {
                if (thief != null) {
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
        for (Actor actor : actors) {
            if (actor != null) {
                actor.render();
            }
        }
        for (Tree tree : treeArrayList){
            if (tree != null) {
                tree.render();
                tree.update();
            }
        }
        for (Stockpile stock : stockpileArrayList){
            if (stock != null) {
                stock.render();
                stock.update();
            }
        }
        for (Hoard hoard : hoardArrayList){
            if (hoard != null) {
                hoard.render();
                hoard.update();
            }
        }
        for (Gatherer gatherer : gathererArrayList) {
            if (gatherer != null) {
                gatherer.render();
            }
        }
        for (Thief thief : thiefArrayList) {
            if (thief != null) {
                thief.render();
                thief.renderStatus();
            }
        }
    }

    /** Run the simulation and print the number of ticks and the total fruits on each stockpiles or hoards
     *
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        new ShadowLife().run();
        int tickToPrint = 1;
        if (thiefArrayList.size() > 0 && gathererArrayList.size() > 0) {
            tickToPrint += Math.max(thiefTicks / thiefArrayList.size(), gathererTicks / gathererArrayList.size());
        } else if (thiefArrayList.size() > 0) {
            tickToPrint +=  thiefTicks / thiefArrayList.size();
        } else if (gathererArrayList.size() > 0) {
            tickToPrint += gathererTicks / gathererArrayList.size();
        } else {
            tickToPrint = 0;
        }
        System.out.println(tickToPrint + " ticks");
        for(Hoard hoard: hoardArrayList){
            System.out.println(hoard.getFruit());
        }
        for(Stockpile stockpile: stockpileArrayList){
            System.out.println(stockpile.getFruit());
        }
    }

    /** Deals with particular bug in MacOS that prevents the program to read the command line arguments.
     *
     * @return the command line arguments
     */
    private static String[] argsFromFile() {
        try {
            return Files.readString(Path.of("args.txt"), Charset.defaultCharset()) .split(" ");
        } catch (IOException e) { e.printStackTrace();
        }
        return null;
    }

    /** A helper method to exit the code in case of wrong command line input.
     *
     */
    private static void exitShadowLife() {
        System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
        System.exit(-1);
    }
}
