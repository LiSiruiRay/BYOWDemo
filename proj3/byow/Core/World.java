package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    int width;
    int height;
    Long seed;
    Avatar avatar;
    List<Room> rooms;
    Set<Light> lights;

    ArrayList<Portal> portalsEntrances;
    ArrayList<Point> portalsExits;
    Portal portal;
    Set<Point> roomCenters;
    TETile buffer;
    TETile[][] map;
    TETile[][] cloud;
    Point[][] points;
    int totalTime;

    int timeUsedBefore;
    boolean isCloud;

    public void setIsCloud (boolean isCloud) {
        this.isCloud = isCloud;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
    public void setTimeUsedBefore(int timeUsedBefore) {
        this.timeUsedBefore = timeUsedBefore;
    }

    public Portal generatePortal(TETile[][] map) {
        int count = 0;
        for (Point center : roomCenters) {
            if (count == 1) {
                portal = new Portal(center.getX() + 1, center.getY());
                map[portal.getX()][portal.getY()] = Tileset.PORTAL;
            }
            count++;
        }
        return portal;
    }
    public void generatePortal2(TETile[][] map) {
        int count = 0;
        for (Point center : roomCenters) {

            Portal thisPortal = null;
            if (portalsExits == null) {
                portalsExits = new ArrayList<>();
            }
            portalsExits.add(center);
            if (count % 4 == 0 && count > 0) {

                thisPortal = new Portal(center.getX() + 1, center.getY());
                map[thisPortal.getX()][thisPortal.getY()] = Tileset.PORTAL;
                points[thisPortal.getX()][thisPortal.getY()] = new Point(thisPortal.getX(), thisPortal.getY(), "portal");
            } else if (count % 4 == 1) {

                thisPortal = new Portal(center.getX() , center.getY() + 1);
                map[thisPortal.getX()][thisPortal.getY()] = Tileset.PORTAL;
                points[thisPortal.getX()][thisPortal.getY()] = new Point(thisPortal.getX(), thisPortal.getY(), "portal");
            } else if (count % 4 == 2) {

                thisPortal = new Portal(center.getX() - 1, center.getY());
                map[thisPortal.getX()][thisPortal.getY()] = Tileset.PORTAL;
                points[thisPortal.getX()][thisPortal.getY()] = new Point(thisPortal.getX(), thisPortal.getY(), "portal");
            } else if (count % 4 == 3){

                thisPortal = new Portal(center.getX(), center.getY() - 1);
                map[thisPortal.getX()][thisPortal.getY()] = Tileset.PORTAL;
                points[thisPortal.getX()][thisPortal.getY()] = new Point(thisPortal.getX(), thisPortal.getY(), "portal");
            }
            if (portalsEntrances == null) {
                portalsEntrances = new ArrayList<>();
            }
            if (count >= 1) {
                thisPortal.setExit(portalsExits.get(count - 1));
                portalsEntrances.add(thisPortal);
            }
            count += 1;
        }
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }
    public void setMap(TETile[][] map) {
        this.map = map;
    }

    public void setCloud(TETile[][] cloud) {
        this.cloud = cloud;
    }


    public World() {
    }
    public World(Long seed, Set<Light> lights) {
        this.seed = seed;
        this.rooms = new ArrayList<>();
        this.lights = lights;
        this.width = 80;
        this.height = 50;
        this.roomCenters = new HashSet<>();
        this.points = new Point[80][50];
    }

    public World(Long seed) {
        this(seed, new HashSet<>());
    }

    public void addLight(Light light) {
        lights.add(light);
    }
    public void save() { }
    public void generateRoom(TETile[][] map) {
        generateRoomObj();
        for (Room rooms: rooms) {
            for (int i = rooms.getLeft(); i < rooms.getRight(); i++) {
                for (int j = rooms.getLower(); j < rooms.getUpper(); j++) {
                    map[i][j] = Tileset.FLOOR;
                    points[i][j] = new Point(i, j, "floor");
                }
            }
        }

    }
    public void generateRoomObj() {
        Random r = new Random(seed);
        int roomNum = 6 + Math.floorMod(r.nextInt(), 3);
        for (int i = 0; i < roomNum; i++) {
            int x = r.nextInt();
            Room pendingRoom = generateSingleRoom(x);
            while (isRoomOverlapped(pendingRoom)) {
                pendingRoom = generateSingleRoom(r.nextInt());
            }
            rooms.add(pendingRoom);
            roomCenters.add(pendingRoom.gerCenter());
        }
    }

    public void generateHallway(TETile[][] map) {
        Hallway.generate(roomCenters, map);
    }
    public static void generateWall(TETile[][] map) {

        for (int x = 0; x < map.length - 1; x++) {
            for (int y = 0; y < map[0].length - 1; y++) {
                setWall(x, y, map);

            }
        }
    }

    public static void generateCloud(TETile[][] map, TETile[][] cloud, Avatar avatar) {
//        check the boundary
        int size = 3;
        int x = avatar.getX();
        int y = avatar.getY();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cloud[x + i][y + j] = map[x + i][y + j];
                cloud[x + i][y - j] = map[x + i][y - j];
                cloud[x - i][y + j] = map[x - i][y + j];
                cloud[x - i][y - j] = map[x - i][y - j];
            }
        }
    }

    public static void cleanCloud(TETile[][] cloud) {
        for (int i = 1; i < cloud.length; i++) {
            for (int j = 0; j < cloud[0].length; j++) {
                cloud[i][j] = Tileset.NOTHING;
            }
        }
    }
    public static void cleanPartCloud(TETile[][] cloud) {
        for (int i = 1; i < cloud.length; i++) {
            for (int j = 0; j < cloud[0].length - 10; j++) {
                cloud[i][j] = Tileset.NOTHING;
            }
        }
    }
    public void generateLights(TETile[][] map) {
        for (Point point : roomCenters) {
            addLight(new Light(point.getX(), point.getY(), "onLight"));
//            map[point.getX()][point.getY()] = Tileset.WALL;
            Light.generateLight(map, point.getX(), point.getY());
            points[point.getX()][point.getY()] = new Point(point.getX(), point.getY(), "light");
        }
    }

    private Room generateSingleRoom(int roomSeed) {
        Random r = new Random(roomSeed);
        int centerX = 8 + Math.floorMod(r.nextInt(), 64);
        int centerY = 8 + Math.floorMod(r.nextInt(), 34);
        int halfHeight = 1 + Math.abs(Math.floorMod(r.nextInt(), 5));
        int halfWidth = 1 + Math.abs(Math.floorMod(r.nextInt(), 5));
        return new Room(new Point(centerX, centerY), centerY + halfHeight,
                centerY - halfHeight, centerX - halfWidth, centerX + halfWidth);
    }

    private boolean isRoomOverlapped(Room pendingRoom) {
        if (rooms.isEmpty()) {
            return false;
        }
        for (Room existRoom: rooms) {
            if (!(pendingRoom.getLower() > existRoom.getUpper()
                    || pendingRoom.getUpper() < existRoom.getLower()
                    || pendingRoom.getLeft() > existRoom.getRight()
                    || pendingRoom.getRight() < existRoom.getLeft())) {
                return true;
            }
        }
        return false;
    }

    private static void setWall(int x, int y, TETile[][] map) {
        boolean hasNothing = false;
        boolean besideFloor = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (map[x + i][y + j].equals(Tileset.NOTHING)) {
                    hasNothing = true;
                }
                if (map[x + i][y + j].equals(Tileset.FLOOR)) {
                    besideFloor = true;
                }
            }
        }

        if (hasNothing && besideFloor) {

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {

                    if (map[x + i][y + j].equals(Tileset.NOTHING)) {
                        map[x + i][y + j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void updatePoints(TETile inputMap[][]) {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (inputMap[i][j] == Tileset.FLOOR) {
                    points[i][j] = new Point(i, j, "floor");
                } else if (inputMap[i][j] == Tileset.WALL) {
                    points[i][j] = new Point(i, j, "wall");
                }
            }
        }
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setBuffer(TETile buffer) {
        this.buffer = buffer;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Point[][] getPoints() {
        return points;
    }

    private int getFloorNum() {
        int count = 0;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (points[i][j] != null && points[i][j].getType().equals("floor")) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public void generateExitAndTrap(TETile[][] inputMap) {
        int size = getFloorNum();
        Random r = new Random(seed);
        int exitIndex = Math.floorMod(r.nextInt(), size);
        ArrayList<Integer> trapIndex = new ArrayList<>();
        for (int k = 0; k < size / 20; k++) {
            trapIndex.add(Math.floorMod(r.nextInt(), size));
        }
        Collections.sort(trapIndex);
        int count = 0;
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (points[i][j] == null) {
                    continue;
                }
                if (points[i][j].getType().equals("floor")) {
                    if (count == exitIndex) {
                        points[i][j] = new Point(i, j, "exit");

                        inputMap[i][j] = Tileset.EXIT;
                        return;
                    } else if (trapIndex.contains(count)){

                        points[i][j] = new Point(i, j, "trap");

                        inputMap[i][j] = Tileset.TRAP;
                    }
                    count += 1;
                }
            }
        }

    }

}
