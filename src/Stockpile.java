import bagel.Font;

public class Stockpile extends Actor {
    public static final String TYPE = "Stockpile";
    private int fruit;
    private final Font showFruit;

    public Stockpile(int x, int y) {

        super("res/images/cherries.png", TYPE, x, y);
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
        this.showFruit.drawString( String.valueOf(this.fruit), this.getX()+15, this.getY()+15);
    }
}
