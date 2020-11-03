/**
 * Gather is a dynamic actor that collects fruit from trees and stocks them at stockpiles or hoards
 */
public class Gatherer extends DynamicActor {

    /* "Actor" type of a gatherer */
    public static final String TYPE = "Gatherer";
    /* Fruit carrying status of the gatherer */
    private boolean carrying = false;

    /**
     * Constructs a gatherer and initialises the position in the simulation world
     *
     * @param x initial x coordinate
     * @param y initial y coordinate
     */
    public Gatherer(int x, int y) {

        super("res/images/gatherer.png", TYPE, x, y, LEFT);

    }


    /**
     * Changes the state of the gatherer with every tick
     */
    @Override
    public void update() {

        /* Move one tile if the gatherer is active  */
        if (this.isActive()) {
            this.marchOn(this.getDirection());
        }

        /* Iterate over the actor list for different gatherer activities */
        for (Actor actor : ShadowLife.getActorList()) {

            if (actor instanceof StaticActor) {
                StaticActor staticActor = (StaticActor) actor;

                /* If the gatherer is standing on a static actor */
                if (this.getX() == staticActor.getX() && this.getY() == staticActor.getY()) {

                    /* Set carrying to true if it's standing on a golden tree*/
                    if (staticActor.type.equals("GoldenTree")) {
                        this.carrying = true;
                        break;
                    }

                    /* Take a fruit from a tree and rotate towards the opposite direction */
                    if (staticActor.type.equals("Tree") && !this.carrying) {
                        if (staticActor.getFruit() > 0) {
                            staticActor.setFruit(staticActor.getFruit() - 1);
                            this.carrying = true;
                            this.rotateOneEighty();
                        }
                    }

                    /* Change direction and stop carrying the fruit when it stocks it on a stockpile  */
                    if (staticActor.type.equals("Stockpile")) {

                        if(this.carrying) {
                            this.carrying = false;
                            staticActor.setFruit(staticActor.getFruit() + 1);
                        }
                        this.rotateOneEighty();
                    }

                    /* Change direction and stop carrying the fruit when it stocks it on a hoard */
                    if (staticActor.type.equals("Hoard")) {
                        if(this.carrying) {
                            this.carrying = false;
                            staticActor.setFruit(staticActor.getFruit() + 1);

                        }
                        this.rotateOneEighty();
                    }

                    /* Destroy current gatherer, create 2 new ones and set them to different directions */
                    if (staticActor.type.equals("Pool")){
                        this.poolActivity(staticActor);
                    }

                    /* Change gatherer direction according to sign */
                    if (staticActor.type.equals("SignUp") || staticActor.type.equals("SignRight") ||
                            staticActor.type.equals("SignDown") || staticActor.type.equals("SignLeft")) {
                        this.followIfAtSign(staticActor);
                    }
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
