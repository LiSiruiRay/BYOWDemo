package byow.Core;

import jh61b.junit.In;

import java.io.Serializable;

public class Point implements Serializable {
    int x;
    int y;
    String type;
    public Point(int x, int y) {
        this(x, y, "");
    }

    public Point(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public boolean canStepOn() {
        return (type.equals("floor") || type.equals("light"));
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "(" + Integer.toString(x) + "," + Integer.toString(y) + ")" + " type: " + type;
    }
}
