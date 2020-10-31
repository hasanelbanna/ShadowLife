/**
 * Part of the code was taken from the solution provided for Project 1.
 */

public class Gatherer extends Actor {
    public static final String TYPE = "Gatherer";
    private int direction;
    private boolean carrying = false;
    private boolean active = true;
    private final int UP = 0;
    private final int RIGHT = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;



    public Gatherer(int x, int y) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = LEFT;
    }
    public Gatherer(int x, int y, int direction) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = direction;
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

    public void moveClockwise(){
        this.direction = (this.direction + 1) % 4;
    }

    public void moveAntiClockwise(){
        this.direction = (this.direction - 1 + 4) % 4;
    }

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

        for (SignRight sign: ShadowLife.signRightArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(RIGHT);
            }
        }
        for (SignLeft sign: ShadowLife.signLeftArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(LEFT);
            }
        }
        for (SignUp sign: ShadowLife.signUpArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(UP);
            }
        }
        for (SignDown sign: ShadowLife.signDownArrayList) {
            if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
                setDirection(DOWN);
            }
        }

        for (Actor actor : ShadowLife.actorList) {
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
        }


        for (Stockpile stockpile : ShadowLife.stockpileArrayList) {
            if(this.getX() == stockpile.getX() && this.getY() == stockpile.getY()) {
                if(this.carrying) {
                    this.carrying = false;
                    stockpile.setFruit(stockpile.getFruit() + 1);

                }
                this.rotateOneEighty(this.getDirection());
            }
        }

        for (Hoard hoard : ShadowLife.hoardArrayList) {
            if(this.getX() == hoard.getX() && this.getY() == hoard.getY()) {
                if(this.carrying) {
                    this.carrying = false;
                    hoard.setFruit(hoard.getFruit() + 1);

                }
                this.rotateOneEighty(this.getDirection());
            }
        }


        for (Fence fence: ShadowLife.fences) {
            if(this.getX() == fence.getX()){

                if (this.getDirection() == UP && (this.getY() - fence.getY()) == ShadowLife.TILE_SIZE) {
                    this.active = false;
                    break;
                } else if(this.getDirection() == DOWN && (fence.getY() - this.getY()) == ShadowLife.TILE_SIZE) {
                    this.active = false;
                    break;
                }
            } else if (this.getY() == fence.getY()) {

                if(this.getDirection() == RIGHT && (fence.getX() - this.getX()) == ShadowLife.TILE_SIZE) {
                    this.active = false;
                    break;
                }
                if(this.getDirection() == LEFT && (this.getX() - fence.getX()) == ShadowLife.TILE_SIZE) {
                    this.active = false;
                    break;
                }
            }

        }




    }
}
