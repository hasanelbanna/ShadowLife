/**
 * Most of the lines in this code was taken from the sample solution of project 1
 */

import bagel.Image;

public abstract class Actor {
    private int x;
    private int y;

    private final Image image;
    public final String type;

    public Actor(String filename, String type, int x, int y) {
        image = new Image(filename);
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public final void tick() {
        update();
    }

    public void render() {
        image.drawFromTopLeft(x, y);
    }

    public void move(int deltaX, int deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public abstract void update();

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
