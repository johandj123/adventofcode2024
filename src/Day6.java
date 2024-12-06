import lib.CharMatrix;
import lib.Direction;
import lib.InputUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input6.txt"));
        var position = charMatrix.find('^').get();
        position.set('.');
        first(position);
        second(position, charMatrix);
    }

    private static void first(CharMatrix.Position position) {
        Set<CharMatrix.Position> visited = new HashSet<>();
        Direction direction = Direction.UP;
        while (position.isValid()) {
            visited.add(position);
            var next = position.add(direction);
            if (next.getUnbounded() == '#') {
                direction = direction.turnRight();
            } else {
                position = next;
            }
        }
        System.out.println(visited.size());
    }

    private static void second(CharMatrix.Position position, CharMatrix charMatrix) {
        Set<CharMatrix.Position> result = new HashSet<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (x != position.getX() || y != position.getY()) {
                    CharMatrix.Position obstruct = charMatrix.new Position(x, y);
                    if (obstruct.get() == '.') {
                        obstruct.set('O');
                        if (loop(position)) {
                            result.add(obstruct);
                        }
                        obstruct.set('.');
                    }
                }
            }
        }
        System.out.println(result.size());
    }

    private static boolean loop(CharMatrix.Position position) {
        Set<Pair> visited = new HashSet<>();
        Direction direction = Direction.UP;
        while (position.isValid()) {
            Pair pair = new Pair(position, direction);
            if (!visited.add(pair)) {
                return true;
            }
            var next = position.add(direction);
            if (next.getUnbounded() == '#' || next.getUnbounded() == 'O') {
                direction = direction.turnRight();
            } else {
                position = next;
            }
        }
        return false;
    }

    record Pair(CharMatrix.Position position,Direction direction) {}
}
