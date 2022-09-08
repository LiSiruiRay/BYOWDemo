# Proj3: BYOW design doc
BYOW: stands for Build Your Own World

# **1. Classes**

All given classes and method are not listed here

All classed listed below are created by us, under Core

### class `Main`: entry of the program

Attributes

- `TETile[][] actualWorld`
- `TETile[][] cloud`

Method

- `void show(Point point)`

### class `World implements Serializable`:

Global frame, to save  and load map information. 

Attributes in World class:

- `Long seed`: AThe seed
- `List<Rooms> rooms`: An
- `Set<Point> light`: store all the lightW
- `int width`
- `int height`
- `Avatar avatar`
- `Set<Point> roomCenter`
- 

Methods in World class:

- `World()`: to generate a random world using saved seed
- `save()`: to save the world as an object
- `generateWalls`

### class `Room`:

- `int getCenter()`:
- `int getLower()`:
- `int getUpper()`
- `int getLeft()`
- `int getRight()`

### class `Hallway`:

- `void createHallway(ArrayList<Point> centers)`: Randomly create hallways connecting rooms

### class `Light implements Serializable`:

- `int x`
- `int y`
- `Light(int x, int y)`
- `void on()`
- `void off()`

### class `Point implements Serializable`:

- `int x`
- `int y`
- `String type`
- `String getType()`
- `void setType()`
- `boolean canStepOn()`

### class `Avatar`:

- `move(Oritation oritation)`:
- 

# **2. Algorithms**

### Room and hallway generation algorithm

The algorithm we designed will produce a set of random centers (5 -10), according to which the room will be built. The hallway will be built according to the centers. The centers will be a Point class and will be generated based on the given seed.

### Walls generation

Use a scanner to scan the floors and generate walls around it

### Avatar moving

Store the tile on which the avatar step on, recover it after avatar moved away.

# **3. Persistence**

## Store:

`long seed`

`Set<Point>`

## Load:

### Generate:

- Map
- Room
- Hallway
- Light //position

### Read:

- avatar
- Light status

# 4. Game:

- turn off all the light in certain time, with cloud shown
- Find the door in certain time, with cloud shown

# Update log:
### Fri Aug 19, 2022: 
- Update:
  - Change the `World` into a class with a neat class inside
- Wanted To Update:
  - Higher speed, less render
  - The use of multi-thread
  - Higher storage usage: Do not store the whole map anymore, only the seed and special points
