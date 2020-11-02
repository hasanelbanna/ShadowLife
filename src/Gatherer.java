/**
 * Gather is a dynamic actor that collects fruit from trees and stocks them at stockpiles or hoards
 */
public class Gatherer extends Actor implements Movable {

    /* type of the actor */
    public static final String TYPE = "Gatherer";
    /* direction of the gatherer and initial value of the four directions*/
    private int direction;
    private final int UP = 0;
    private final int RIGHT = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    /* status of the gatherer */
    private boolean carrying = false;
    private boolean active = true;
    private boolean onPool = false;

    /**
     * Constructs a gatherer and initialises the position in the simulation world
     *
     * @param x x coordinate
     * @param y x coordinate
     */
    public Gatherer(int x, int y) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = LEFT;
    }

    /**
     * Constructs a gatherer and initialises the position and direction in the simulation world
     *
     * @param x x coordinate
     * @param y x coordinate
     * @param direction initial direction
     */
    public Gatherer(int x, int y, int direction) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = direction;
    }

    /**
     * method to check if the gatherer is active
     *
     * @return inverted active status
     */
    public boolean isActive() {
        return !this.active;
    }

    /**
     * method to check if the gatherer is standing on a pool
     *
     * @return true if on the pool
     */
    public boolean isOnPool() {
        return onPool;
    }

    /**
     * method to change the direction
     *
     * @param direction new direction
     */
    public void setDirection(int direction){
        this.direction = direction;
    }

    /**
     * get the current direction of the gatherer
     *
     * @return current direction
     */
    public int getDirection(){
        return this.direction;
    }

    /**
     * change the current direction to 90 degree clockwise
     */
    public void rotateNinetyClockwise(){
        this.direction = (this.direction + 1) % 4;
    }

    /**
     * change the current direction to 90 degree anticlockwise
     */
    public void rotateNinetyAntiClockwise(){
        this.direction = (this.direction - 1 + 4) % 4;
    }

    /**
     * change the current direction to 180 degree
     */
    private void rotateOneEighty(int direction) {
        if (direction == RIGHT) {
            this.setDirection(LEFT);
        } else if (direction == LEFT) {
            this.setDirection(RIGHT);
        } else if (direction == UP) {
            this.setDirection(DOWN);
        } else {
            this.setDirection(UP);
        }
    }

    /**
     * method that forces the gatherer to follow the sign changing the direction when it stands upon it
     *
     * @param direction current direction
     * @param actor actor
     */
    private void followSign(int direction, Actor actor) {
        NonLivingActor sign = (NonLivingActor) actor;
        if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
            this.setDirection(direction);
        }
    }

    /**
     * changes the state of the gatherer
     */
    @Override
    public void update() {
        if (this.active) {
            switch (this.direction) {
                case UP:
                    move(0, -ShadowLife.getTileSize());
                    break;
                case DOWN:
                    move(0, ShadowLife.getTileSize());
                    break;
                case LEFT:
                    move(-ShadowLife.getTileSize(), 0);
                    break;
                case RIGHT:
                    move(ShadowLife.getTileSize(), 0);
                    break;
            }
        }

        for (Actor actor : ShadowLife.getActorList()) {
            if (actor.type.equals("Tree")) {
                Tree tree = (Tree) actor;
                if (this.getX() == tree.getX() && this.getY() == tree.getY() && !this.carrying) {
                    if (tree.getFruit() > 0) {
                        tree.setFruit(tree.getFruit() - 1);
                        this.carrying = true;
                        this.rotateOneEighty(this.getDirection());

                    }
                }
            }

            if (actor.type.equals("GoldenTree")) {
                Tree goldenTree = (Tree) actor;
                if (this.getX() == goldenTree.getX() && this.getY() == goldenTree.getY()) {
                    this.carrying = true;
                }
            }

            if (actor.type.equals("Stockpile")) {
                Stockpile stockpile = (Stockpile) actor;
                if(this.getX() == stockpile.getX() && this.getY() == stockpile.getY()) {
                    if(this.carrying) {
                        this.carrying = false;
                        stockpile.setFruit(stockpile.getFruit() + 1);

                    }
                    this.rotateOneEighty(this.getDirection());
                }
            }

            if (actor.type.equals("Hoard")) {
                Hoard hoard = (Hoard) actor;
                if(this.getX() == hoard.getX() && this.getY() == hoard.getY()) {
                    if(this.carrying) {
                        this.carrying = false;
                        hoard.setFruit(hoard.getFruit() + 1);

                    }
                    this.rotateOneEighty(this.getDirection());
                }
            }

            if (actor.type.equals("Pool")) {
                NonLivingActor pool = (NonLivingActor) actor;
                if(this.getX() == pool.getX() && this.getY() == pool.getY()) {
                    ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
                    this.onPool = true;
                }
            }

            if (actor.type.equals("SignLeft")) {
                this.followSign(LEFT, actor);
            }

            if (actor.type.equals("SignRight")) {
                this.followSign(RIGHT, actor);
            }

            if (actor.type.equals("SignUp")) {
                this.followSign(UP, actor);
            }

            if (actor.type.equals("SignDown")) {
                this.followSign(DOWN, actor);
            }

        }

        for (Actor actor : ShadowLife.getActorList()) {
            if (actor.type.equals("Fence")) {
                NonLivingActor fence = (NonLivingActor) actor;
                if(this.getX() == fence.getX()){

                    if (this.getDirection() == UP && (this.getY() - fence.getY()) == ShadowLife.getTileSize()) {
                        this.active = false;
                        ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
                        break;
                    } else if(this.getDirection() == DOWN && (fence.getY() - this.getY()) == ShadowLife.getTileSize()) {
                        this.active = false;
                        ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
                        break;
                    }
                } else if (this.getY() == fence.getY()) {

                    if(this.getDirection() == RIGHT && (fence.getX() - this.getX()) == ShadowLife.getTileSize()) {
                        this.active = false;
                        ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
                        break;
                    }
                    if(this.getDirection() == LEFT && (this.getX() - fence.getX()) == ShadowLife.getTileSize()) {
                        this.active = false;
                        ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
                        break;
                    }
                }

            }

        }

    }
}
