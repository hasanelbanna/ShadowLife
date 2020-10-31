import bagel.Font;

public class Thief extends Actor implements Movable {

    public static final String TYPE = "Thief";
    private int direction;
    private boolean carrying = false;
    private boolean consuming = false;
    private boolean active = true;
    private final Font status;
    private final int UP = 0;
    private final int RIGHT = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;


    public Thief(int x, int y) {
        super("res/images/thief.png", TYPE, x, y);
        this.direction = UP;
        this.status = new Font("res/conformable.otf", 24);

    }
    public Thief(int x, int y, int direction) {
        super("res/images/thief.png", TYPE, x, y);
        this.direction = direction;
        this.status = new Font("res/conformable.otf", 24);

    }

    public boolean isActive() {
        return !this.active;
    }

    public int getDirection(){
        return this.direction;
    }

    private void setDirection(int direction){
        this.direction = direction;
    }

    public void rotateNinetyClockwise(){
        this.direction = (this.direction + 1) % 4;
    }

    public void rotateNinetyAntiClockwise(){
        this.direction = (this.direction - 1 + 4) % 4;
    }

    private void rotateTwoSeventy(int direction){
        if (direction == UP) {
            this.setDirection(LEFT);

        } else if (direction == RIGHT) {
            this.setDirection(UP);

        } else if (direction == DOWN) {
            this.setDirection(RIGHT);

        } else {
            this.setDirection(DOWN);
        }
    }

    private void followSign(int direction, Actor actor) {
        NonLivingActor sign = (NonLivingActor) actor;
        if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
            this.setDirection(direction);
        }
    }

    public void renderStatus(){
        if (this.carrying) {
            this.status.drawString("carrying", this.getX(), this.getY());
        }

        if (this.consuming) {
            this.status.drawString("consuming", this.getX(), this.getY());
        }
    }

    @Override
    public void update() {

        if (this.active) {
            switch (this.direction) {
                case UP:
                    move(0, -ShadowLife.TILE_SIZE);
                    break;
                case DOWN:
                    move(0, ShadowLife.TILE_SIZE);
                    break;
                case LEFT:
                    move(-ShadowLife.TILE_SIZE, 0);
                    break;
                case RIGHT:
                    move(ShadowLife.TILE_SIZE, 0);
                    break;
            }
        }


        for (Actor actor : ShadowLife.actorList) {

            if (actor.type.equals("Tree")) {
                Tree tree = (Tree) actor;
                if (this.getX() == tree.getX() && this.getY() == tree.getY() && !this.carrying) {
                    if (tree.getFruit() > 0) {
                        tree.setFruit(tree.getFruit() - 1);
                        this.carrying = true;
                    }
                }

            }

            if (actor.type.equals("GoldenTree")) {
                Tree goldenTree = (Tree) actor;
                if (this.getX() == goldenTree.getX() && this.getY() == goldenTree.getY()) {
                    this.consuming = false;
                    this.carrying = true;
                }
            }

            if (actor.type.equals("Pad")) {
                NonLivingActor pad = (NonLivingActor) actor;
                if (this.getX() == pad.getX() && this.getY() == pad.getY()) {
                    this.consuming = true;
                }
            }

            if (actor.type.equals("Stockpile")) {
                Stockpile stockpile = (Stockpile) actor;
                if(this.getX() == stockpile.getX() && this.getY() == stockpile.getY()) {
                    if(!this.carrying) {
                        if (stockpile.getFruit() > 0) {
                            this.carrying = true;
                            this.consuming = false;
                            stockpile.setFruit(stockpile.getFruit() - 1);
                            this.rotateNinetyClockwise();
                        }

                    } else {
                        this.rotateNinetyClockwise();
                    }
                }
            }

            if (actor.type.equals("Hoard")) {
                Hoard hoard = (Hoard) actor;
                if (this.getX() == hoard.getX() && this.getY() == hoard.getY()) {
                    if (this.consuming) {
                        this.consuming = false;
                        if (!this.carrying) {
                            if (hoard.getFruit() > 0) {
                                this.carrying = true;
                                hoard.setFruit(hoard.getFruit() - 1);
                            } else {
                                this.rotateNinetyClockwise();
                            }
                        }

                    } else if (this.carrying) {
                        // rotate direction by 90 degree clockwise
                        this.rotateNinetyClockwise();
                        hoard.setFruit(hoard.getFruit() + 1);
                        this.carrying = false;

                    }
                }
            }

            if (actor.type.equals("Gatherer")) {
                Gatherer gatherer = (Gatherer) actor;
                if(this.getX() == gatherer.getX() && this.getY() == gatherer.getY() && gatherer.isActive()) {
                    this.rotateTwoSeventy(this.getDirection());
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

        for (Actor actor : ShadowLife.actorList) {
            if (actor.type.equals("Fence")) {
                NonLivingActor fence = (NonLivingActor) actor;
                if (this.getX() == fence.getX()) {

                    if (this.getDirection() == UP && (this.getY() - fence.getY()) == ShadowLife.TILE_SIZE) {
                        this.active = false;
                        break;
                    } else if (this.getDirection() == DOWN && (fence.getY() - this.getY()) == ShadowLife.TILE_SIZE) {
                        this.active = false;
                        break;
                    }
                } else if (this.getY() == fence.getY()) {

                    if (this.getDirection() == RIGHT && (fence.getX() - this.getX()) == ShadowLife.TILE_SIZE) {
                        this.active = false;
                        break;
                    }
                    if (this.getDirection() == LEFT && (this.getX() - fence.getX()) == ShadowLife.TILE_SIZE) {
                        this.active = false;
                        break;
                    }
                }

            }
        }
    }
}
