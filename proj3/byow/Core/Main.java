package byow.Core;


//import byow.TileEngine.TERenderer;
//import byow.TileEngine.TETile;
//import byow.TileEngine.Tileset;
//import edu.princeton.cs.algs4.StdDraw;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
//    private static TETile[][] map;
//    private static TETile[][] cloud;
//    private static Avatar avatar;
//    private static TETile buffer;
//    private static TERenderer ter = new TERenderer();

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.interactWithKeyboard();
    }
//    public static void main2(String[] args) {
//        if (args.length > 2) {
//            System.out.println("Can only have two arguments - the flag and input string");
//            System.exit(0);
//        } else if (args.length == 2 && args[0].equals("-s")) {
//            Engine engine = new Engine();
//            engine.interactWithInputString(args[1]);
////            System.out.println(engine);
//        // DO NOT CHANGE THESE LINES YET ;)
//        } else if (args.length == 2 && args[0].equals("-p")) {
//            Engine engine = new Engine();
//            engine.interactWithKeyboard();
//        } else if (args[0].equals("dev")) {
//            World world = generateWorldDevelop();
////            System.out.println("test");
//            boolean notPushQ = true;
//            avatar = Avatar.generateAvatar(world.rooms);
//            buffer = map[avatar.x][avatar.y];
//            map[avatar.x][avatar.y] = Tileset.AVATAR;
//            int counter = 0;
////            map[10][10] = Tileset.LIGHT;
////            Light.generateLight(map, 40, 25, );
//
//            boolean pressedC;
////            ter.renderFrame(map);
//            boolean showCloud;
//            while (notPushQ) {
//
//                while (!StdDraw.hasNextKeyTyped()) {
//                    StdDraw.pause(1);
////                    System.out.println("test");
//                }
//
//                char key = StdDraw.nextKeyTyped();
//                showCloud = key == 'c';
//                if (!showCloud) {
//                    operationWithKey(key);
////                    ter.renderFrame(map);
//                    continue;
//                }
//
//                while (showCloud) {
//                    if (key == 'c') {
//                        World.generateCloud(map, cloud, avatar);
////                        ter.renderFrame(cloud);
//                    }
//                    while (!StdDraw.hasNextKeyTyped()) {
//                        StdDraw.pause(1);
////                        System.out.println("test2");
//                    }
//                    char move = StdDraw.nextKeyTyped();
//                    key = move;
//                    if (move == 'm') {
//                        showCloud = false;
////                        ter.renderFrame(map);
//                        break;
//                    }
//                    World.cleanCloud(cloud);
//                    operationWithKey(move);
//                    World.generateCloud(map, cloud, avatar);
////                    ter.renderFrame(cloud);
//                }
//
//            }
//        }
//    }
//    public static World generateWorldDevelop() {
////        TERenderer ter = new TERenderer();
////        ter.initialize(80, 50);
//
////        ter.initialize(80, 50);
//        map = new TETile[80][50];
//        cloud = new TETile[80][50];
//        World actualWorld = new World(2345L);
//        actualWorld.generateRoom(map);
//        /*
//        test
//         */
//        for (int i = 0; i < map.length; i++) {
//            for (int j = 0; j < map[0].length; j++) {
//                map[i][j] = Tileset.NOTHING;
//                cloud[i][j] = Tileset.NOTHING;
//            }
//        }
//
//        actualWorld.generateHallway(map);
//        World.generateWall(map);
////        for (Point point : actualWorld.roomCenters) {
////            Light.generateLight(map, point.getX(), point.getY(), actualWorld);
////        }
//        actualWorld.generateLights(map);
////        ter.renderFrame(map);
//        return actualWorld;
//    }
//    public static void operationWithKey(char key) {
//        switch (key) {
//            case 'w':
//                System.out.println("w");
//                buffer = avatar.moveTo(avatar.x, avatar.y + 1, map, buffer);
//                break;
//            case 'a':
//                System.out.println("a");
//                buffer = avatar.moveTo(avatar.x - 1, avatar.y, map, buffer);
//                break;
//            case 's':
//                System.out.println("s");
//                buffer = avatar.moveTo(avatar.x, avatar.y - 1, map, buffer);
//                break;
//            case 'd':
//                System.out.println("d");
//                buffer = avatar.moveTo(avatar.x + 1, avatar.y, map, buffer);
//                break;
//            case 'f':
//                buffer = Light.off(map, avatar.getX(), avatar.getY(), buffer);
//                break;
//            case 'o':
//                buffer = Light.on(map, avatar.getX(), avatar.getY(), buffer);
//        }
//    }
}
