package lib;

import java.util.List;

public record Direction(int x, int y) {
    public static Direction NORTH = new Direction(0, -1);
    public static Direction SOUTH = new Direction(0, 1);
    public static Direction WEST = new Direction(-1, 0);
    public static Direction EAST = new Direction(1, 0);

    public static Direction UP = NORTH;
    public static Direction DOWN = SOUTH;
    public static Direction LEFT = WEST;
    public static Direction RIGHT = EAST;

    public static List<Direction> DIRECTIONS = List.of(NORTH, SOUTH, WEST, EAST);

    public static List<Direction> DIRECTIONS_WITH_DIAGONAL = List.of(NORTH, SOUTH, WEST, EAST, new Direction(1, 1), new Direction(-1, 1), new Direction(1, -1), new Direction(-1, -1));

    public Direction turnLeft() {
        return new Direction(y, -x);
    }

    public Direction turnRight() {
        return new Direction(-y, x);
    }
}
