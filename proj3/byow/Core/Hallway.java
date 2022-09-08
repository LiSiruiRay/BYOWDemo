package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Set;

public class Hallway {
    public static void generate(Set<Point> centers, TETile[][] map) {
        for (Point center : centers) {
            for (Point center2 : centers) {
                connect(center, center2, map);
            }
        }
    }

    private static void connect(Point p1, Point p2, TETile[][] map) {
        if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
            return;
        }

        int xDistance = Math.abs(p1.getX() - p2.getX());
        int yDistance = Math.abs(p1.getY() - p2.getY());
        int xStart = Math.min(p1.getX(), p2.getX());
        int yStart = Math.min(p1.getY(), p2.getY());
        int middleOfX = xDistance / 2;
        int middleOfY = yDistance / 2;
        for (int i = 0; i < xDistance; i++) { //change of x
            map[xStart + i][yStart] = Tileset.FLOOR;
            if (i > middleOfX) {
                map[xStart + i][yStart + yDistance] = Tileset.FLOOR;
                map[xStart + i][yStart + 1] = Tileset.FLOOR;
            }
        }
        for (int i = 0; i <= yDistance; i++) { //change of y
            map[xStart][yStart + i] = Tileset.FLOOR;
            map[xStart + middleOfX][yStart + i] = Tileset.FLOOR;
        }
    }
}
