/**
 * Class that instantiates a dynamic actor (gatherer or thief)
 */
public class DynamicActor extends Actor implements Movable {

    /* Direction if the actor */
    private int direction;
    /* Active or resting status of the actor */
    private boolean active;
    /* If the actor is on a pool */
    private boolean onPool;

    /**
     * Constructs a dynamic actor and initialises its position and initial direction
     *
     * @param filename location if the image file
     * @param type "Actor" classification of the dynamic actor
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param direction initial duirection
     */
    public DynamicActor(String filename, String type, int x, int y, int direction) {
        super(filename, type, x, y);
        this.direction = direction;
        this.active = true;

    }

    /**
     *  Method to check if the actor is standing on the pool
     */
    public boolean isOnPool() {
        return this.onPool;
    }

    /**
     * Check if the actor is active
     *
     * @return if the actor is active or resting
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Set the active status of the actor to true
     **/
    public void deactivateActor() {
        this.active = false;
    }

    /**
     * Method to get the current direction, implemented from the interface
     *
     * @return current direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Method to set a new direction for the actor
     * @param direction new direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Method to mova an active actor based on it's direction
     *
     * @param direction current direction
     */
    public void marchOn(int direction) {
        switch (direction) {
            case UP:
                move(0, -TILE_SIZE);
                break;
            case DOWN:
                move(0, TILE_SIZE);
                break;
            case LEFT:
                move(-TILE_SIZE, 0);
                break;
            case RIGHT:
                move(TILE_SIZE, 0);
                break;
        }
    }

    /**
     * Method to make the actor follow a sign and change it's direction accordingly
     *
     * @param actor dynamic actor (gatherer or thief)
     */
    public void followIfAtSign(StaticActor actor) {
        if (actor.type.equals("SignLeft")) {
            this.accordingToSign(LEFT, actor);
        }

        if (actor.type.equals("SignRight")) {
            this.accordingToSign(RIGHT, actor);
        }

        if (actor.type.equals("SignUp")) {
            this.accordingToSign(UP, actor);
        }

        if (actor.type.equals("SignDown")) {
            this.accordingToSign(DOWN, actor);
        }

    }

    /**
     * Helper method for followIfAtSign(StaticActor actor) method to set the direction based on sign
     * @param direction new direction
     * @param sign Sign type
     */
    private void accordingToSign(int direction, StaticActor sign) {
        if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
            this.setDirection(direction);
        }
    }

    /**
     * Rotate the actor 90 Degree clockwise
     */
    public void rotateNinetyClockwise() {
        this.direction = (this.getDirection() + 1) % 4;
    }

    /**
     * Rotate the actor 90 Degree anticlockwise
     */
    public void rotateNinetyAntiClockwise() {
        this.direction = (this.direction - 1 + 4) % 4;
    }


    /**
     * Method to change the gatherer direction by 180 Degree
     */
    public void rotateOneEighty() {

        if (this.getDirection() == RIGHT) {
            this.setDirection(LEFT);
        } else if (this.getDirection() == LEFT) {
            this.setDirection(RIGHT);
        } else if (this.getDirection() == UP) {
            this.setDirection(DOWN);
        } else {
            this.setDirection(UP);
        }

    }

    /**
     * When the actor stands on a pool it removes the actor and creates 2 new ones with the help of boolean variable
     *
     * @param pool to access pool coordinates
     */
    public void poolActivity(StaticActor pool) {
        if(this.getX() == pool.getX() && this.getY() == pool.getY()) {
            this.onPool = true;
            this.deactivateActor();
            if (this instanceof Gatherer) {
                ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
            } else if (this instanceof Thief) {
                ShadowLife.setTotalThiefActive(ShadowLife.getTotalThiefActive() - 1);
            }
        }

    }

    /**
     * Helper method for poolActivity(StaticActor pool) to remove the actor
     * @param actor actor to be removed
     */
    private void removeActor(Actor actor) {
        if (actor instanceof Gatherer) {
            ShadowLife.setTotalGathererActive(ShadowLife.getTotalGathererActive() - 1);
        }
        if (actor instanceof Thief) {
            ShadowLife.setTotalThiefActive(ShadowLife.getTotalThiefActive() - 1);
        }
    }

    /**
     * Method to stop the dynamic actor from moving and make it rest in front of fence
     *
     * @param fence to access the fence coordinates
     */
    public void stopActor(StaticActor fence) {

        if (this.getX() == fence.getX()) {

            if (this.getDirection() == UP && (this.getY() - fence.getY()) == TILE_SIZE) {
                this.deactivateActor();
                this.removeActor(this);
            } else if (this.getDirection() == DOWN && (fence.getY() - this.getY()) == TILE_SIZE) {
                this.deactivateActor();
                this.removeActor(this);
            }
        } else if (this.getY() == fence.getY()) {

            if (this.getDirection() == RIGHT && (fence.getX() - this.getX()) == TILE_SIZE) {
                this.deactivateActor();
                this.removeActor(this);
            } else if (this.getDirection() == LEFT && (this.getX() - fence.getX()) == TILE_SIZE) {
                this.removeActor(this);
                this.deactivateActor();
            }
        }

    }

    @Override
    public void update() {

    }
}
