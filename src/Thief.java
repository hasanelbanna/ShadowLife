import bagel.Font;

/**
 * Thief is a dynamic actor that steals fruit and makes gatherer's job difficult
 */
public class Thief extends DynamicActor {

    /* "Actor" type of a thief */
    public static final String TYPE = "Thief";
    /* Fruit carrying or consuming status of the thief */
    private boolean carrying = false;
    private boolean consuming = false;
    /* Font that will be used to show thief's fruit carrying or consuming status */
    private final Font status;

    /**
     * Constructs a thief and initialises its position in the simulation world
     *
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public Thief(int x, int y) {
        super("res/images/thief.png", TYPE, x, y, UP);
        this.status = new Font("res/conformable.otf", 24);
    }

    /**
     * Show if the thief is carrying or consuming
     */
    public void renderStatus(){
        if (this.carrying) {
            this.status.drawString("carrying", this.getX(), this.getY());
        }

        if (this.consuming) {
            this.status.drawString("consuming", this.getX(), this.getY());
        }
    }

    /**
     * Changes the state of the thief with every tick
     */
    @Override
    public void update() {

        /* Move one tile if the thief is active  */
        if (this.isActive()) {
            this.marchOn(this.getDirection());
        }

        /* Iterate over the actor list for different thief activities */
        for (Actor actor : ShadowLife.getActorList()) {

            if (actor instanceof StaticActor) {
                StaticActor staticActor = (StaticActor) actor;

                if (this.getX() == staticActor.getX() && this.getY() == staticActor.getY()) {

                    /* Set carrying and consuming to true if it's standing on a golden tree*/
                    if (staticActor.type.equals("GoldenTree")) {
                        this.consuming = false;
                        this.carrying = true;
                    }

                    /* Take a fruit from a tree and rotate towards the opposite direction */
                    if (staticActor.type.equals("Tree") && !this.carrying) {
                        if (staticActor.getFruit() > 0) {
                            staticActor.setFruit(staticActor.getFruit() - 1);
                            this.carrying = true;
                        }
                    }

                    /* Decrease stockpile collection by 1 while consuming and change direction */
                    if (staticActor.type.equals("Stockpile")) {
                        if(!this.carrying) {
                            if (staticActor.getFruit() > 0) {
                                this.carrying = true;
                                this.consuming = false;
                                staticActor.setFruit(staticActor.getFruit() - 1);
                                this.rotateNinetyClockwise();
                            }

                        } else {
                            this.rotateNinetyClockwise();
                        }
                    }

                    /* Increase hoard collection by 1 while carrying or decrease by 1 while consuming
                     and rotate direction by 90 Degree clockwise */
                    if (staticActor.type.equals("Hoard")) {
                        if (this.consuming) {
                            this.consuming = false;
                            if (!this.carrying) {
                                if (staticActor.getFruit() > 0) {
                                    this.carrying = true;
                                    staticActor.setFruit(staticActor.getFruit() - 1);
                                } else {
                                    this.rotateNinetyClockwise();
                                }
                            }

                        } else if (this.carrying) {
                            this.rotateNinetyClockwise();
                            staticActor.setFruit(staticActor.getFruit() + 1);
                            this.carrying = false;

                        }
                    }

                    /* Change the consuming mode to true  */
                    if (staticActor.type.equals("Pad")) {
                        this.consuming = true;
                    }

                    /* Destroy current thief, create 2 new ones and set them to different directions */
                    if (staticActor.type.equals("Pool")){
                        this.poolActivity(staticActor);
                    }

                    /* Change thief direction according to sign */
                    if (staticActor.type.equals("SignUp") || staticActor.type.equals("SignRight") ||
                            staticActor.type.equals("SignDown") || staticActor.type.equals("SignLeft")) {
                        this.followIfAtSign(staticActor);
                    }

                }
            }

            /* Rotate thief by 270 Degree if it stands on a resting Gatherer */
            if (actor instanceof Gatherer) {
                Gatherer gatherer = (Gatherer) actor;
                if(this.getX() == gatherer.getX() && this.getY() == gatherer.getY() && !gatherer.isActive()) {
                    this.rotateNinetyAntiClockwise();
                }
            }

        }

        for (Actor actor : ShadowLife.getActorList()) {

            /* Stop in front of a fence */
            if (actor.type.equals("Fence")) {
                StaticActor fence = (StaticActor) actor;
                this.stopActor(fence);
            }
        }
    }
}
