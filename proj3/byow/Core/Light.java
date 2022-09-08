package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.Serializable;

public class Light extends Point implements Serializable {
    public Light (int x, int y, String type) {
        super(x, y, type);
    }
    public Light(int x, int y) {
        this(x, y, "light");
    }

    public static TETile off(TETile[][] map, int x, int y, TETile buffer) {
        System.out.println("tile: " + map[x][y]);
        System.out.println("light: " + Tileset.LIGHT);
        if (buffer.equals(Tileset.LIGHT)) {
            System.out.println("this is light");
            map[x][y] = new TETile('✶', Color.white, Color.BLACK, "off light");
            int counter = 0;
            for (int i = 1; i < 4; i++) {
                for (int m = 0; m < 4; m++) {
                    for (int j = 0; j < i * 2; j++) {
                        if (m == 0) {
                            if (!canGenerateLight(map, x - i + j, y + i)) {
                                System.out.println("cannot off");
                                continue;
                            }
                            map[x - i + j][y + i] = Tileset.FLOOR;
                        } else if (m == 1) {
                            if (!canGenerateLight(map, x + i, y + i - j)) {
                                continue;
                            }
                            map[x + i][y + i - j] = Tileset.FLOOR;
                        } else if (m == 2) {
                            if (!canGenerateLight(map, x - i + j + 1, y - i)) {
                                continue;
                            }
                            map[x - i + j + 1][y - i] = Tileset.FLOOR;
                        } else {
                            if (!canGenerateLight(map, x - i, y - i + j)) {
                                continue;
                            }
                            map[x - i][y - i + j] = Tileset.FLOOR;
                        }
                    }
                }
                counter++;
            }
            return new TETile('✶', Color.white, Color.BLACK, "off light");
        }
        return buffer;
    }
    public static TETile on(TETile[][] map, int x, int y, TETile buffer) {
        if (buffer.equals(Tileset.OFFLIGHT)) {

            light(map, x, y, "turning on");
            return Tileset.LIGHT;
        }
        return buffer;
    }

    public static void generateLight(TETile[][] map, int x, int y) {
        Light light = new Light(x, y);
        light(map, x, y, "generating light");
    }
    public static void light(TETile[][] map, int x, int y, String condition) {
        if (condition.equals("generating light")) {
            map[x][y] = Tileset.LIGHT;
        }

        int counter = 0;
        for (int i = 1; i < 4; i++) {
//            System.out.println("in to loop");
            int color = 205 - (counter * i * i * i * 5);
            if (color < 0) {
                color = 0;
            }
            int color2 = counter * i * i * i;
            if (color2 > 225) {
                color2 = 225;
            }

            TETile teTile = new TETile('·', new Color(128, 192, 128), new Color(color2, color, color), "light's wave");
            for (int m = 0; m < 4; m++) {
                for (int j = 0; j < i * 2; j++) {
                    if (m == 0) {

                        if (!canGenerateLight(map, x - i + j, y + i)) {
//                            System.out.println("cannot on");
                            continue;
                        }
                        map[x - i + j][y + i] = teTile;
//                        System.out.println(teTile);
                    } else if (m == 1) {
                        if (!canGenerateLight(map, x + i, y + i - j)) {
                            continue;
                        }
                        map[x + i][y + i - j] = teTile;
                    } else if (m == 2) {
                        if (!canGenerateLight(map, x - i + j + 1, y - i)) {
                            continue;
                        }
                        map[x - i + j + 1][y - i] = teTile;
                    } else {
                        if (!canGenerateLight(map, x - i, y - i + j)) {
                            continue;
                        }
                        map[x - i][y - i + j] = teTile;
                    }
                }
            }
            counter++;
        }
    }
    public static boolean canGenerateLight(TETile[][] map, int x, int y) {
        if (x < 0) {
            return false;
        }
        if (y < 0) {
            return false;
        }
        if (x > map.length - 1) {
            return false;
        }

        if (y > map[0].length - 1) {
            return false;
        }

        if (!map[x][y].equals(Tileset.FLOOR)) {
            if (map[x][y].equals(Tileset.LIGHT) || map[x][y].equals(Tileset.LIGHTWAVE)) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }
//    @Override
//    public boolean equals(Object o) {
//        if (o == null) return false;
//        Light light = (Light) o;
//        return getX() == light.getX() && getY() == light.getY();
//    }
}
