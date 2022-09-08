package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class Testing {
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 50);
        TETile[][] actualWorld = new TETile[80][50];
        TETile[][] cloud;
        for (int i = 0; i < actualWorld.length; i++) {
            for (int j = 0; j < actualWorld[0].length; j++) {
                actualWorld[i][j] = Tileset.NOTHING;
            }
        }


        World canvas = new World(13204L, null);
        canvas.generateRoom(actualWorld);


        Set<Point> points = new HashSet<>();
        Point center1 = new Point(5, 5);
        Point center2 = new Point(20, 30);
        Point center3 = new Point(10, 10);
        Point center4 = new Point(25, 40);
        Point center5 = new Point(55, 45);
        Point center6 = new Point(5, 10);
        Point center7 = new Point(75, 20);
        Point center8 = new Point(68, 40);
        points.add(center1);
//        points.add(center2);
        points.add(center3);
//        points.add(center4);
//        points.add(center5);
//        points.add(center6);
//        points.add(center7);
//        points.add(center8);
//        World.generateHallway(canvas.roomCenters, actualWorld);
//        for (int i = 3; i < 5; i++) {
//            actualWorld[3][i] = Tileset.FLOOR;
//        }
//        actualWorld[5][5] = Tileset.FLOOR;
        World.generateWall(actualWorld);
        ter.renderFrame(actualWorld);




        ter.renderFrame(actualWorld);

    }

    @Test
    public void testForAG2() {
        Engine e = new Engine();
        TETile[][] result = e.interactWithInputString("N999SDDDWWWDDD:Q");
//        TETile[][] result2 = e.interactWithInputString("L:Q");
    }

    @Test
    public void testForAG22() {
        Engine e = new Engine();
//        TETile[][] result = e.interactWithInputString("N999SDDDWWWDDD");
        TETile[][] result2 = e.interactWithInputString("L:Q");
    }
}
