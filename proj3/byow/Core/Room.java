package byow.Core;

import java.io.Serializable;

public class Room implements Serializable {

    private Point center;
    private int upper;
    private int lower;
    private int left;
    private int right;

    public Room(Point center, int upper, int lower, int left, int right) {
        this.center = center;
        this.upper = upper;
        this.lower = lower;
        this.left = left;
        this.right = right;
    }

    public Point gerCenter() {

        return this.center;
    }

    public int getUpper() {
        return this.upper;
    }
    public int getLower() {
        return this.lower;
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    public String toString() {
        return "Center: " + center.toString() + " Up: " + upper
                + " Down: " + lower + " Left: " + left + " Right: " + right;
    }
}
