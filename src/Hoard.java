import bagel.Font;

public class Hoard extends Actor {
    public static final String TYPE = "Hoard";
    private int fruit;
    private Font showFruit;

    public Hoard(int x, int y) {

        super("res/images/hoard.png", TYPE, x, y);
        this.fruit = 0;
        this.showFruit = new Font("res/conformable.otf", 30);

    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    @Override
    public void update() {
        this.showFruit.drawString( String.valueOf(this.fruit), this.getX()+5, this.getY()+5);
    }
}
