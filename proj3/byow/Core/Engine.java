package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public final int WIDTH = 80;
    public final int HEIGHT = 50;
    TETile buffer;
    Avatar avatar;
    TETile[][] map = new TETile[WIDTH][HEIGHT];
    TETile[][] cloud = new TETile[WIDTH][HEIGHT];
    World actualWorld;
    Portal portal;
    Font smallFont = new Font("", Font.PLAIN,15);
    private String numbers = "1234567890\n";

    boolean gameOver = false;

    boolean win = false;

    TETile lastMouseHover;

    boolean isCloud;

    int timeUsed;
    int totalTime = 30;

    int timeUsedBefore;


    char lastMove;

    int manaLeft;
//    int timeLeft;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        map = new TETile[80][50];
        cloud = new TETile[80][50];
        mainMenuDisplay();
        char option = menuInput();
        switch (option) {
            case 'n':
                Long seed = intoGame();
                actualWorld = new World(seed);
                initialCanvas(map, cloud, actualWorld, portal);

                avatar = Avatar.generateAvatar(actualWorld.rooms);
                buffer = map[avatar.x][avatar.y];
                map[avatar.x][avatar.y] = Tileset.AVATAR;
                actualWorld.setAvatar(avatar);
//                showTheDemo();
                totalTime = 30;
                StdDraw.setFont(smallFont);
                ter.renderFrame(map);
                timeUsedBefore = 0;
                interactWithKey();
                break;
            case 'l':
                loadGame();
//                StdDraw.setFont(smallFont);
//                load total time
//                interactWithKey("load");
                interactWithKey();
                break;
            case 'e':
                System.exit(0);
        }
    }

    public void save() {
        File outFile = new File("gameFile.txt");
        Utils.writeContents(outFile, Utils.serialize(actualWorld));
    }

    public void loadGame_test() {
        World testWorld = new World(123L);
        TETile[][] newMap = new TETile[80][50];
        TETile[][] cloud = new TETile[80][50];
        initialCanvas(newMap, cloud, testWorld, portal);

    }
    public void loadGame() {
        actualWorld = load();
        Font smallFont = new Font("", Font.PLAIN,15);
        map = actualWorld.map;
        avatar = actualWorld.avatar;
        buffer = actualWorld.buffer;
        cloud = actualWorld.cloud;
//        totalTime = actualWorld.totalTime;
        isCloud = actualWorld.isCloud;
        timeUsedBefore = actualWorld.timeUsedBefore;
        for (Light light : actualWorld.lights) {
            if (light.equals(Tileset.OFFLIGHT)) {
                buffer = Light.off(map, light.getX(), light.getY(), buffer);
            }
        }

        StdDraw.setFont(smallFont);
        ter.renderFrame(actualWorld.map);
//        interactWithKeySimple();
    }
    public void loadGameAG() {
        actualWorld = load();
        Font smallFont = new Font("", Font.PLAIN,15);
        map = actualWorld.map;
        avatar = actualWorld.avatar;
        buffer = actualWorld.buffer;
        cloud = actualWorld.cloud;
        for (Light light : actualWorld.lights) {
            if (light.equals(Tileset.OFFLIGHT)) {
                buffer = Light.off(map, light.getX(), light.getY(), buffer);
            }
        }

        StdDraw.setFont(smallFont);
        ter.renderFrame(actualWorld.map);
//        interactWithKeySimple();
    }



    public World load() {
        World world;
        File inFile = new File("gameFile.txt");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            world = (World) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            world = null;
        }

        return world;
    }

    private void interactWithKey() {
        LocalTime initialTime = LocalTime.now();
        isCloud = true;
        manaLeft = 10;
        while (!gameOver) {
            mouseListener();
            timeListener(initialTime);
            gameLister(actualWorld.getPoints(), avatar);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                keyListener(key);
            }
        }
        gameOver();
    }
    private void interactWithKey(String input) {
        if (input.equals("load")) {
            LocalTime initialTime = LocalTime.now();
            isCloud = true;
            manaLeft = 10;
            while (!gameOver) {
                mouseListener();
                timeListener(initialTime);
                gameLister(actualWorld.getPoints(), avatar);
                if (StdDraw.hasNextKeyTyped()) {
                    char key = StdDraw.nextKeyTyped();
                    keyListener(key);
                }
            }
            gameOver();
        }
    }

    public void mouseListener() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        if (isCloud) {
            World.generateCloud(map, cloud, avatar);
            displayMouse(mouseX, mouseY, cloud);
        } else {
            displayMouse(mouseX, mouseY, map);
        }
    }

    public void keyListener(char key) {
        if (key == 'c') {
            isCloud = true;
        }
        if (key == 'm') {
            isCloud = false;
        }
        if (key == ':') {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
            }
            char key3 = StdDraw.nextKeyTyped();
            if (Character.toLowerCase(key3) == 'q') {
                actualWorld.setBuffer(buffer);
                actualWorld.setAvatar(avatar);
                actualWorld.setMap(map);
                actualWorld.setCloud(cloud);
//                actualWorld.setTotalTime(timeUsed);
                actualWorld.setTimeUsedBefore(timeUsed + timeUsedBefore);
                actualWorld.setIsCloud(isCloud);
                save();
                World.cleanCloud(cloud);
                ter.renderFrame(cloud);
                drawFrameAdd("Game will be saved", 40, 48, 30);
                StdDraw.pause(1000);
                System.exit(0);
            }
        }
        buffer = operationWithKey(key, buffer, map, avatar, actualWorld);
        if (isCloud) {
            World.cleanCloud(cloud);
            World.generateCloud(map, cloud, avatar);
            ter.renderFrame(cloud);
        } else {
            ter.renderFrame(map);
        }
    }

    private void gameLister(Point[][] points, Avatar avatar) {
        int x = avatar.x;
        int y = avatar.y;
        if (points[x][y].getType().equals("trap")) {
            gameOver = true;
        } else if (points[x][y].getType().equals("exit")) {
            win = true;
            gameOver = true;
        }
    }

    private void timeListener(LocalTime initialTime) {
        LocalTime time = LocalTime.now();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d1 = null;
        java.util.Date d2 = null;
        try {
            d1 = format.parse(initialTime.toString());
            d2 = format.parse(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long diff = d2.getTime() - d1.getTime();
        Long diffSeconds = diff / 1000;
        timeUsed = diffSeconds.intValue();
        totalTime = 6000 - timeUsed - timeUsedBefore;
        addText("Time left:" + totalTime, 40, 48, smallFont);
        addText("Mega left:" + manaLeft, 5, 47, smallFont);
        if (totalTime <= 0) {
            gameOver = true;
            return;
        }
    }

    private void interactWithKeySimple() {
        boolean notPushQ = true;
        Font smallFont = new Font("", Font.PLAIN,15);
        while (notPushQ) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
            }

            char key = StdDraw.nextKeyTyped();
            if (key != 'c') {
                if (key == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(1);
                    }

                    key = StdDraw.nextKeyTyped();
                    if (Character.toLowerCase(key) == 'q') {
                        actualWorld.setBuffer(buffer);
                        actualWorld.setAvatar(avatar);
                        actualWorld.setMap(map);
                        actualWorld.setCloud(cloud);
                        actualWorld.setTotalTime(timeUsed);

                        save();
                        drawFrameAdd("Game will be saved", 40, 48, 30);
                        System.exit(0);
                        return;
                    }

                }
                buffer = operationWithKey(key, buffer, map, avatar, actualWorld);
                ter.renderFrame(map);
                continue;
            }

            while (true) {
                if (key == 'c') {
                    World.generateCloud(map, cloud, avatar);
                    ter.renderFrame(cloud);
                }
                while (!StdDraw.hasNextKeyTyped()) {
                    StdDraw.pause(1);
                }
                char move = StdDraw.nextKeyTyped();
                key = move;
                if (move == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(1);
                    }
                    key = StdDraw.nextKeyTyped();
                    if (Character.toLowerCase(key) == 'q') {
                        actualWorld.setBuffer(buffer);
                        actualWorld.setAvatar(avatar);
                        actualWorld.setMap(map);
                        actualWorld.setCloud(cloud);
                        save();
                        drawFrameAdd("Game will be saved", 40, 48, 30);
                        StdDraw.pause(1000);
                        System.exit(0);
                    }
                }
                if (move == 'm') {
                    ter.renderFrame(map);
                    World.cleanCloud(cloud);
                    break;
                }
                World.cleanPartCloud(cloud);
                buffer = operationWithKey(move, buffer, map, avatar, actualWorld);
                World.generateCloud(map, cloud, avatar);
                ter.renderFrame(cloud);
                World.cleanPartCloud(cloud);
            }
        }
    }
    private void interactWithCloud() {
        boolean notPushQ = true;
        Font smallFont = new Font("", Font.PLAIN,15);
        while (notPushQ) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
            }

            char key = StdDraw.nextKeyTyped();
                if (key == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(1);
                    }

                    key = StdDraw.nextKeyTyped();
                    if (Character.toLowerCase(key) == 'q') {
                        actualWorld.setBuffer(buffer);
                        actualWorld.setAvatar(avatar);
                        actualWorld.setMap(map);
                        actualWorld.setCloud(cloud);
                        save();
                        drawFrameAdd("Game will be saved", 40, 48, 30);
                        System.exit(0);
                        return;
                    }

                }
                buffer = operationWithKey(key, buffer, map, avatar, actualWorld);
                ter.renderFrame(cloud);
                addText("press 'O/F' to turn on/off the light, remember to step on it", 60, 47, smallFont);
                addText("press 'T' to transport, remember to step on the portal", 60, 46, smallFont);

            while (true) {
                while (!StdDraw.hasNextKeyTyped()) {
                    StdDraw.pause(1);
                }
                char move = StdDraw.nextKeyTyped();
                key = move;
                if (move == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                        StdDraw.pause(1);
                    }

                    key = StdDraw.nextKeyTyped();
                    if (Character.toLowerCase(key) == 'q') {
                        actualWorld.setBuffer(buffer);
                        actualWorld.setAvatar(avatar);
                        actualWorld.setMap(map);
                        actualWorld.setCloud(cloud);
                        save();
                        drawFrameAdd("Game will be saved", 40, 48, 30);
                        StdDraw.pause(1000);
                        System.exit(0);
                    }
                }
                int tempx = avatar.getX();
                int tempy = avatar.getY();
                buffer = operationWithKey(move, buffer, map, avatar, actualWorld);
                World.generateCloud(map, cloud, new Avatar(tempx, tempy));
                World.generateCloud(map, cloud, avatar);
                ter.renderFrame(cloud);
            }
        }
    }
    private void interactWithKeySimpleForAG(String move) {
        char key;
        for (int i = 0; i < move.length(); i++) {
            if (Character.toLowerCase(move.charAt(i)) == ':') {

                if (i <= move.length() - 1) {

                    if (Character.toLowerCase(move.charAt(i + 1)) == 'q') {
                        actualWorld.setBuffer(buffer);
                        actualWorld.setAvatar(avatar);
                        actualWorld.setMap(map);
                        actualWorld.setCloud(cloud);
                        save();

                        return;
                    }
                }
            }
            key = Character.toLowerCase(move.charAt(i));
            buffer = operationWithKey(key, buffer, map, avatar, actualWorld);
        }
    }

    private Long intoGame() {
        seedInputDisplay();
        String stringInput = keyboardInputString().toLowerCase();
        String seedString = stringInput.substring(0, stringInput.length());
        Long seed = Long.parseLong(seedString);
        Font smallFont = new Font("", Font.PLAIN,15);
        StdDraw.setFont(smallFont);

        return seed;
    }
    private TETile operationWithKey(char key, TETile buffer, TETile[][] map, Avatar avatar, World thisWorld) {
        switch (Character.toLowerCase(key)) {
            case 'w':
                buffer = avatar.moveTo(avatar.x, avatar.y + 1, map, buffer);
                lastMove = 'w';
                break;
            case 'a':
                buffer = avatar.moveTo(avatar.x - 1, avatar.y, map, buffer);
                lastMove = 'a';
                break;
            case 's':
                buffer = avatar.moveTo(avatar.x, avatar.y - 1, map, buffer);
                lastMove = 's';
                break;
            case 'd':
                buffer = avatar.moveTo(avatar.x + 1, avatar.y, map, buffer);
                lastMove = 'd';
                break;
            case 'f':
                TETile temp = map[avatar.getX()][avatar.getY()];
                buffer = Light.off(map, avatar.getX(), avatar.getY(), buffer);
                map[avatar.getX()][avatar.getY()] = temp;
                break;
            case 'o':
                buffer = Light.on(map, avatar.getX(), avatar.getY(), buffer);
                break;
            case 't':
                int avatarX = avatar.getX();
                int avatarY = avatar.getY();
                Portal currPortal = null;
                boolean isPortal = false;

                for (Portal portal: thisWorld.portalsEntrances) {
                    if (portal.getX() == avatarX && portal.getY() == avatarY) {
                        currPortal = portal;
                        isPortal = true;
                        break;
                    }
                }
                if (isPortal) {
                    int toX = currPortal.getExit().getX();
                    int toY = currPortal.getExit().getY();
                    buffer = avatar.moveTo(toX, toY, map, buffer);
                    World.generateCloud(map, cloud, avatar);
                }
                break;
            case 'r':
                fireball(avatar, lastMove, actualWorld.getPoints(), map);
                break;
            case 'v':
                drawFrame("Spec");
                drawFrameAdd("press any key to return back to game", 40, 38, 30);
                showTheDemo();
                break;


        }

        return buffer;
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        TERenderer ter = new TERenderer();
        String seedString = "";
        String play = "";
        char firstLetter = input.charAt(0);
        int playStart = 0;


        switch (Character.toLowerCase(firstLetter)) {
            case 'l':
                loadGameAG();
                play = input.substring(1);
                interactWithKeySimpleForAG(play);

                return map;
            case 'n':
                for (int i = 1; i < input.length(); i++) {
                    if (Character.toLowerCase(input.charAt(i)) == 's') {
                        playStart = i + 1;
                        break;
                    }
                    seedString += input.charAt(i);
                }
                play = input.substring(playStart);
                Long seed = Long.valueOf(seedString);
                actualWorld = new World(seed);
                initialCanvas(map, cloud, actualWorld, portal);
                avatar = Avatar.generateAvatar(actualWorld.rooms);
                buffer = map[avatar.x][avatar.y];
                map[avatar.x][avatar.y] = Tileset.AVATAR;
                actualWorld.setAvatar(avatar);

                interactWithKeySimpleForAG(play);

                return map;
        }

        return map;
    }


    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("TimesRoman", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, s);
        StdDraw.show();
    }
    public void drawFrameAdd(String s, int x, int y, int size) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
//        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("TimesRoman", Font.ITALIC, size);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }

    public void addText(String s, int x, int y, Font small) {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Plain", Font.BOLD, 15);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, s);
        StdDraw.show();
        StdDraw.setFont(small);
    }

    private String keyboardInputString() {
        String result = "";
        while (!result.toLowerCase().contains("s")) {
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                String nextString = Character.toString(next);
                if (numbers.contains(nextString)) {
                    result += nextString;
                    drawFrame(result);
                } else if (next == 's' || next == 'S') {
                    return result;
                } else {
                    drawFrameAdd("Invalid input, please input again", 60, 48, 30);
                    StdDraw.pause(50);
                    drawFrame(result);
                }
            }
        }
        return result;
    }

    private char menuInput() {
        char result = ' ';
        while (result != 'n' && result != 'l' && result != 'e') {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
            }
            result = Character.toLowerCase(StdDraw.nextKeyTyped());
        }

        return result;
    }

    private void mainMenuDisplay() {
        ter.initialize(80, 50);
        drawFrame("♚Welcome to BYOW by Ray and Lubin♚");
        drawFrameAdd("================================", 40, 23, 30);
        drawFrameAdd("♖Check the world (N)", 40, 21, 30);
        drawFrameAdd("♖Load game (L)", 40, 19, 30);
        drawFrameAdd("♖Exit game (Q)", 40, 17, 30);
    }

    private void seedInputDisplay() {
        drawFrame("Please Input seed: ");
    }

    /**
     * Generate rooms
     * @param map
     * @param actualWorld
     * @return
     */
    private void initialCanvas(TETile[][] map, TETile[][] cloud, World actualWorld, Portal portal) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = Tileset.NOTHING;
                cloud[i][j] = Tileset.NOTHING;
            }
        }

        actualWorld.generateRoom(map);
        actualWorld.generateHallway(map);
        actualWorld.updatePoints(map);

        World.generateWall(map);
        actualWorld.generateLights(map);
        actualWorld.generatePortal2(map);
        actualWorld.generateExitAndTrap(map);
    }

    private void displayMouse(double mouseX, double mouseY, TETile[][] tileArray) {
        if (mouseX <= 0 || mouseX >= WIDTH || mouseY <= 0 || mouseY >= HEIGHT) {
            return;
        }
        Double tileX = mouseX;
        Double tileY = mouseY;
        Integer intTileX = tileX.intValue();
        Integer intTileY = tileY.intValue();
        if (tileArray[intTileX][intTileY].equals(Tileset.NOTHING)) {
            upDisplay("this tile is nothing");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }
            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.WALL)) {
            upDisplay("this tile is wall");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.PORTAL)) {
            upDisplay("this tile is portal");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.LIGHT)) {
            upDisplay("this tile is light");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.AVATAR)) {
            upDisplay("this tile is avatar");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.FLOOR)) {
            upDisplay("this tile is floor");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.EXIT)) {
            upDisplay("this tile is exit");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.TRAP)) {
            upDisplay("this tile is trap");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.FIREBALL)) {
            upDisplay("this tile is a fire ball");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        } else if (tileArray[intTileX][intTileY].equals(Tileset.LIGHTWAVE)) {
            upDisplay("this tile is a lighten floor");
            TETile[][] refresh = new TETile[1][map[0].length];
            for (int i = 0; i < map[0].length; i++) {
                map[0][i] = Tileset.NOTHING;
                refresh[0][i] = Tileset.NOTHING;
            }

            StdDraw.pause(100);
            if (isCloud) {
                ter.renderFrame(cloud);
            } else {
                ter.renderFrame(map);
            }
        }
        lastMouseHover = tileArray[intTileX][intTileY];
    }

    private void upDisplay(String s) {
        addText(s, 5, 49, smallFont);
    }

    private void fireball(Avatar avatar, char lastmove, Point[][] points, TETile[][] tileMap) {
        int x = avatar.x;
        int y = avatar.y;
        if (manaLeft <= 0) {
            return;
        }
        manaLeft -= 1;
        switch (lastmove) {
            case 'w':
                fireballUpDown(1, points, x, y, tileMap);
                break;
            case 's':
                fireballUpDown(-1, points, x, y, tileMap);
                break;
            case 'a':
                fireballLeftRight(-1, points, x, y, tileMap);
                break;
            case 'd':
                fireballLeftRight(1, points, x, y, tileMap);
                break;
            default:
                break;
        }
    }

    private void fireballUpDown(int i, Point[][] points, int x, int y, TETile[][] tileMap) {
        if (points[x][y + i].getType().equals("floor") || points[x][y + i].getType().equals("trap")){
            Fireball fireball = new Fireball(x, y+i);
            TETile fireballbuffer = tileMap[x][y+i];
            tileMap[x][y + i] = Tileset.FIREBALL;
            if (points[x][y + i].getType().equals("trap")) {
                points[x][y + i] = new Point(x, y + i, "floor");
                return;
            }
            int steps = 5;
            while (steps > 0) {
//                System.out.println(points[x][y].getType());
                y += i;
                if (points[x][y] != null && points[x][y].getType().equals("floor")){
                    fireballbuffer = fireball.moveTo(x, y, tileMap, fireballbuffer);
                    if (!isCloud) {
                        ter.renderFrame(map);
                        StdDraw.pause(20);
                    }
                    steps -= 1;
                } else if (points[x][y] != null && points[x][y].getType().equals("trap")) {
                    points[x][y] = new Point(x, y , "floor");
                    return;
                } else {
                    return;
                }
            }
        }
    }
    private void fireballLeftRight(int i, Point[][] points, int x, int y, TETile[][] tileMap) {
        if (points[x + i][y] != null) {
            if (points[x + i][y].getType().equals("floor") || points[x + i][y].getType().equals("trap")) {
                Fireball fireball = new Fireball(x + i, y);
                TETile fireballbuffer = tileMap[x + i][y];
                tileMap[x + i][y] = Tileset.FIREBALL;
                if (points[x + i][y].getType().equals("trap")) {
                    points[x + i][y] = new Point(x + i, y, "floor");
                    return;
                }
                int steps = 5;
                while (steps > 0) {
                    x += i;
                    if (points[x][y] != null && points[x][y].getType().equals("floor")) {
                        fireballbuffer = fireball.moveTo(x, y, tileMap, fireballbuffer);
                        steps -= 1;
                        if (!isCloud) {
                            ter.renderFrame(map);
                            StdDraw.pause(20);
                        }
                    } else if (points[x][y] != null && points[x][y].getType().equals("trap")) {
                        points[x][y] = new Point(x, y, "floor");
                        fireballbuffer = fireball.moveTo(x, y, tileMap, fireballbuffer);
                        return;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void gameOver() {
        if (win) {
            drawFrame("GOOD GAME, YOU WIN");
        } else {
            drawFrame("死ぬ");
        }
    }

    private void showTheDemo() {
//        drawFrame("In this mod, you can only win the game if you reach the exit, a green door");
        Font font = new Font("", Font.PLAIN,20);
        addText("You win : reach the exit, a green door", 40, 20, font);
        addText("Step on a trap : die", 40, 18, font);
        addText("'M' :  show map", 40, 16, font);
        addText("'R' :  shoot fire ball, eliminating the trap", 40, 14, font);
        addText("'T' : transport, step on the portal '❖'", 40, 12, font);
        addText("'F' and 'O' : operate light, step on the light '✶'", 40, 10, font);

        StdDraw.show();
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(1);
        }

    }
}
