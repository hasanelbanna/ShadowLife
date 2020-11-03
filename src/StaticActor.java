import bagel.Font;

/**
 * Class to create all the static members of the simulation world
 */
public class StaticActor extends Actor {

    /* Classification of all the static actors */
    public static final String TYPE_TREE = "Tree";
    public static final String TYPE_Golden_Tree = "GoldenTree";
    public static final String TYPE_STOCKPILE = "Stockpile";
    public static final String TYPE_HOARD = "Hoard";
    public static final String TYPE_PAD = "Pad";
    public static final String TYPE_LEFT = "SignLeft";
    public static final String TYPE_RIGHT = "SignRight";
    public static final String TYPE_UP = "SignUp";
    public static final String TYPE_DOWN = "SignDown";
    public static final String TYPE_FENCE = "Fence";
    public static final String TYPE_POOL = "Pool";
    /* Number of fruits on trees, stockpiles, hoards */
    private int fruit;
    /* Font that will be used to render the number of fruits on the screen */
    private final Font showFruit = new Font("res/conformable.otf", 30);

    /**
     *  Constructs a static actor (organic) with its position and fruit
     *
     * @param filename Image location of the static actor
     * @param type "Actor" type classification
     * @param x initial and final x coordinate
     * @param y initial and final y coordinate
     * @param fruit initial number of fruits
     */
    public StaticActor(String filename, String type, int x, int y, int fruit) {

        super(filename, type, x, y);
        this.fruit = fruit;

    }

    /**
     *  Constructs a static actor (inorganic) with its position
     *
     * @param filename Image location of the static actor
     * @param type "Actor" type classification
     * @param x initial and final x coordinate
     * @param y initial and final y coordinate
     */
    public StaticActor(String filename, String type, int x, int y) {

        super(filename, type, x, y);
    }

    /**
     * Method to get the available fruit number
     *
     * @return the current fruit number
     */
    public int getFruit() {
        return fruit;
    }

    /**
     * Increase or decrease a fruit based on the organic actor
     *
     * @param fruit the new number of fruits
     */
    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    /**
     * Draw the current number of fruits on organic actors with every tick
     */
    @Override
    public void update() {
        this.showFruit.drawString(String.valueOf(this.getFruit()), this.getX(), this.getY());

    }
}
