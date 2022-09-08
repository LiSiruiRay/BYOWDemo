package byow.Core;

public class Portal extends Point{

    Point exit;
    public Portal(int x, int y) {
        super(x, y, "portal");
    }

    void setExit(Point x) {
        exit = x;
    }

    Point getExit() {
        return exit;
    }

}
