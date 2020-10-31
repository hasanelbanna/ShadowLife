public class Sign extends Actor {
    public static final String TYPE_LEFT = "SignLeft";
    public static final String TYPE_RIGHT = "SignRight";
    public static final String TYPE_UP = "SignUp";
    public static final String TYPE_DOWN = "SignDown";
    public Sign(String imageLocation, String TYPE, int x, int y) {
        super(imageLocation, TYPE, x, y);
    }



    public void update() {}
}
