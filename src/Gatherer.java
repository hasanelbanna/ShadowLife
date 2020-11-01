/**
 * Part of the code was taken from the solution provided for Project 1.
 */
import java.util.ArrayList;
public class Gatherer extends Actor implements Movable {
    public static final String TYPE = "Gatherer";
    private int direction;
    private boolean carrying = false;
    private boolean active = true;
    private final int UP = 0;
    private final int RIGHT = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private boolean onPool = false;

    public Gatherer(int x, int y) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = LEFT;
    }

    public Gatherer(int x, int y, int direction) {
        super("res/images/gatherer.png", TYPE, x, y);
        this.direction = direction;
    }

    public boolean isActive() {
        return !this.active;
    }

    public boolean isOnPool() {
        return onPool;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection(){
        return this.direction;
    }

    public void rotateNinetyClockwise(){
        this.direction = (this.direction + 1) % 4;
    }

    public void rotateNinetyAntiClockwise(){
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

    private void followSign(int direction, Actor actor) {
        NonLivingActor sign = (NonLivingActor) actor;
        if (this.getX() == sign.getX() && this.getY() == sign.getY()) {
            this.setDirection(direction);
        }
    }

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
