public class Pool extends Actor {
    public static final String TYPE = "Pool";

    public Pool(int x, int y) {

        super("res/images/pool.png", TYPE, x, y);
    }

    @Override
    public void update() {

        for (Actor actor : ShadowLife.actorList) {
            if (actor.type.equals("Gatherer")) {
                Gatherer gatherer = (Gatherer) actor;
                if (Math.abs(gatherer.getX() - this.getX()) <= 10 &&
                        Math.abs(gatherer.getY() - this.getY()) <= 10) {

                    Gatherer clone = new Gatherer(gatherer.getX(), gatherer.getY(), gatherer.getDirection());
                    clone.rotateNinetyClockwise();
                    gatherer.rotateNinetyAntiClockwise();
                    ShadowLife.actorList.add(clone);
                    ShadowLife.addToTotalGatherers();
                    break;
                }
            }

            if (actor.type.equals("Thief")) {
                Thief thief = (Thief) actor;
                if (Math.abs(thief.getX() - this.getX()) <= 10 &&
                        Math.abs(thief.getY() - this.getY()) <= 10) {

                    Thief clone = new Thief(thief.getX(), thief.getY(), thief.getDirection());
                    clone.rotateNinetyClockwise();
                    thief.rotateNinetyAntiClockwise();
                    ShadowLife.actorList.add(clone);
                    ShadowLife.addToTotalThieves();
                    break;
                }
            }
        }
    }
}

