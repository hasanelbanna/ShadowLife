public class Pool extends Actor{
    public static final String TYPE = "Pool";

    public Pool(int x, int y) {

        super("res/images/pool.png", TYPE, x, y);
    }

    @Override
    public void update() {

        //clone Gatherer if inside Pool
        for (Gatherer gatherer : ShadowLife.gathererArrayList) {
            if (Math.abs(gatherer.getX() - this.getX()) <= 10 &&
                    Math.abs(gatherer.getY() - this.getY()) <= 10) {

                Gatherer clone = new Gatherer(gatherer.getX(), gatherer.getY(), gatherer.getDirection());
                clone.moveClockwise();
                gatherer.moveAntiClockwise();
                ShadowLife.gathererArrayList.add(clone);
                break;
            }
        }

        for (Thief thief : ShadowLife.thiefArrayList) {
            if (Math.abs(thief.getX() - this.getX()) <= 10 &&
                    Math.abs(thief.getY() - this.getY()) <= 10) {

                Thief clone = new Thief(thief.getX(), thief.getY(), thief.getDirection());
                clone.moveClockwise();
                thief.moveAntiClockwise();
                ShadowLife.thiefArrayList.add(clone);
                break;
            }
        }
    }

}