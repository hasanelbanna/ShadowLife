/**
 * Interface to determine what a dynamic actor is able to do
 */
public interface Movable {

    /* Amount of pixel a dynamic actor moves in each tick while active */
    int TILE_SIZE = 64;
    /* Directions that an actor csn follow  */
    int UP = 0;
    int RIGHT = 1;
    int DOWN = 2;
    int LEFT = 3;

    /* Method to get the current direction of the actor */
    int getDirection();

    /* Method to rotate the dynamic actor by 180 Degree */
    void rotateOneEighty();


    /* Method to rotate the dynamic actor by -90 Degree */
    void rotateNinetyClockwise();

    /* Method to rotate the dynamic actor by 90 Degree */
    void rotateNinetyAntiClockwise();

}
