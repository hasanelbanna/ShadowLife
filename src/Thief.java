import bagel.Font;

public class Thief extends Actor{

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

    public void renderStatus(){
        if (this.carrying) {
            this.status.drawString("carrying", this.getX(), this.getY());
        }

        if (this.consuming) {
            this.status.drawString("consuming", this.getX(), this.getY());
        }
    }

    public boolean isActive() {
        return this.active;
    }

    private void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection(){
        return this.direction;
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

    public void moveClockwise(){
        this.direction = (this.direction + 1) % 4;
    }

    public void moveAntiClockwise(){
        this.direction = (this.direction - 1 + 4) % 4;
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

        for (SignRight sign : ShadowLife.signRightArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(RIGHT);
            }
        }
        for (SignLeft sign : ShadowLife.signLeftArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(LEFT);
            }
        }
        for (SignUp sign : ShadowLife.signUpArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(UP);
            }
        }
        for (SignDown sign : ShadowLife.signDownArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(DOWN);
            }
        }

        for (Pad pad : ShadowLife.padArrayList) {
            if (this.getX() == pad.getX() && this.getY() == pad.getY()) {
                this.consuming = true;
            }
        }

        for (Gatherer gatherer : ShadowLife.gathererArrayList) {
            if(this.getX() == gatherer.getX() && this.getY() == gatherer.getY() && !gatherer.isActive()) {
                this.rotateTwoSeventy(this.getDirection());
            }
        }

        for (GoldenTree goldenTree : ShadowLife.goldenTreeArrayList) {
            if (this.getX() == goldenTree.getX() && this.getY() == goldenTree.getY()) {
                this.consuming = false;
                this.carrying = true;
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
        }

        for (Hoard hoard : ShadowLife.hoardArrayList) {
            if (this.getX() == hoard.getX() && this.getY() == hoard.getY()) {
                if (this.consuming) {
                    this.consuming = false;
                    if (!this.carrying) {
                        if (hoard.getFruit() > 0) {
                            this.carrying = true;
                            hoard.setFruit(hoard.getFruit() - 1);
                        } else {
                            this.moveClockwise();
                        }
                    }

                } else if (this.carrying) {
                    // rotate direction by 90 degree clockwise

                    this.moveClockwise();

                    hoard.setFruit(hoard.getFruit() + 1);
                    this.carrying = false;

                }
            }
        }

        for (Stockpile stockpile : ShadowLife.stockpileArrayList) {
            if(this.getX() == stockpile.getX() && this.getY() == stockpile.getY()) {
                if(!this.carrying) {
                    if (stockpile.getFruit() > 0) {
                        this.carrying = true;
                        this.consuming = false;
                        stockpile.setFruit(stockpile.getFruit() - 1);
                        this.moveClockwise();
                    }

                } else {
                    this.moveClockwise();
                }
            }
        }

        for (Fence fence : ShadowLife.fences) {
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
