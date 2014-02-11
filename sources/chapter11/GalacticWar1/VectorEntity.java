/*********************************************************
 * Base vector shape class for game entities
 **********************************************************/
import java.awt.*;

public class VectorEntity extends BaseGameEntity {
    //variables
    private Shape shape;

    //accessor methods
    public Shape getShape() { return shape; }

    //mutator methods
    public void setShape(Shape shape) { this.shape = shape; }

    //default constructor
    VectorEntity() {
        setShape(null);
    }
}
