import bagel.Image;

/**
 * Actor represents a character in the simulation world.
 * There are both dynamic(gatherer and thief) and static actors (rest of the actors) in the simulation.
 */
public abstract class Actor {

    /* x, y coordinates of the actor */
    private int x;
    private int y;
    /* image of the actor */
    private final Image image;
    /* Classification of the actor */
    public final String type;

    /**
     * constructs and initialises a actor in the simulation world
     *
     * @param filename location of the image
     * @param type type of the actor
     * @param x x coordinate
     * @param y y coordinate
     */
    public Actor(String filename, String type, int x, int y) {
        this.image = new Image(filename);
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * a method that determines the movement of the dynamic actors
     */
    public final void tick() {
        update();
    }

    /**
     * renders the background
     */
    public void render() {
        image.drawFromTopLeft(x, y);
    }

    /**
     * changes the position of the dynamic actors
     *
     * @param deltaX change of position in x axis
     * @param deltaY change of position in y axis
     */
    public void move(int deltaX, int deltaY) {
        x += deltaX;
        y += deltaY;
    }

    /**
     * Update the state of the simulation
     */
    public abstract void update();

    /**
     * method to get the current x coordinate of a actor
     * @return x coordinate
     */
    public int getX() {
        return this.x;
    }


    /**
     * method to get the current y coordinate of a actor
     * @return y coordinate
     */
    public int getY() {
        return this.y;
    }

}
