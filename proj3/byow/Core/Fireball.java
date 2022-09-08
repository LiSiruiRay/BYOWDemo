package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

public class Fireball extends Avatar implements Serializable {

    public Fireball(int x, int y) {
        super(x, y);
    }

    public TETile moveTo(int x, int y, TETile[][] map, TETile before) {
        if (map[x][y].equals(Tileset.WALL)) {
            return before;
        }
        map[this.x][this.y] = before;
        TETile buffer = map[x][y];
        this.x = x;
        this.y = y;
        map[x][y] = Tileset.FIREBALL;

        return buffer;
    }
}
