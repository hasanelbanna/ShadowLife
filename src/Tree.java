/**
 * Some lines of this class was used from the sample solution of project 1
 */

import bagel.*;
public class Tree extends Actor {
    public static final String TYPE = "Tree";
    private int x;
    private int y;
    private int fruit;
    private Font showFruit;

    public Tree(int x, int y) {

        super("res/images/tree.png", TYPE, x, y);
        this.x = x;
        this.y = y;
        this.fruit = 3;
        this.showFruit = new Font("res/conformable.otf", 30);

    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    /**
     * Show the number of fruits on the screen
     */
    @Override
    public void update() {
        this.showFruit.drawString( String.valueOf(this.fruit), this.x, this.y);
    }
}