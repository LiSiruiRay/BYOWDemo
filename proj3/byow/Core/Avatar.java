package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Avatar extends Point implements Serializable {
    public Avatar(int x, int y) {
        super(x, y, "avatar");
    }

    public static Avatar generateAvatar(List<Room> rooms) {
        Point center = rooms.get(0).gerCenter();
        Avatar avatar = new Avatar(center.x, center.y);
        return avatar;
    }

    public TETile moveTo(int x, int y, TETile[][] map, TETile before) {
        if (map[x][y].equals(Tileset.WALL)) {
            return before;
        }
        map[this.x][this.y] = before;
        TETile buffer = map[x][y];
        this.x = x;
        this.y = y;
        map[x][y] = Tileset.AVATAR;

        return buffer;
    }

}
