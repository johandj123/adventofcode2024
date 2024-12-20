import lib.CharMatrix;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.Map;

public class Day20 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input20.txt"), '#');
        CharMatrix.Position start = charMatrix.find('S').orElseThrow();
        CharMatrix.Position end = charMatrix.find('E').orElseThrow();
        first(start, end, charMatrix);
        second(start, end, charMatrix);
    }

    private static void first(CharMatrix.Position start, CharMatrix.Position end, CharMatrix charMatrix) {
        int length = GraphUtil.breadthFirstSearch(start, p -> p.getNeighbours().stream().filter(pp -> pp.get() != '#').toList(), p -> p.equals(end));
        int count = 0;
        for (int y = 1; y < charMatrix.getHeight() - 1; y++) {
            for (int x = 1; x < charMatrix.getWidth() - 1; x++) {
                CharMatrix.Position cheat = charMatrix.new Position(x, y);
                if (cheat.get() == '#') {
                    cheat.set('.');
                    int cheatedLength = GraphUtil.breadthFirstSearch(start, p -> p.getNeighbours().stream().filter(pp -> pp.get() != '#').toList(), p -> p.equals(end));
                    int profit = length - cheatedLength;
                    if (profit >= 100) {
                        count++;
                    }
                    cheat.set('#');
                }
            }
        }
        System.out.println(count);
    }

    private static void second(CharMatrix.Position start, CharMatrix.Position end, CharMatrix charMatrix) {
        int length = GraphUtil.breadthFirstSearch(start, Day20::neighbours, p -> p.equals(end));
        Map<CharMatrix.Position, Integer> startDistances = GraphUtil.breadthFirstSearchDistances(start, Day20::neighbours);
        Map<CharMatrix.Position, Integer> endDistances = GraphUtil.breadthFirstSearchDistances(end, Day20::neighbours);
        int saved = 0;
        for (var startCheat : charMatrix) {
            Integer startDistance = startDistances.get(startCheat);
            if (startDistance == null) {
                continue;
            }
            for (int dy = -20; dy <= 20; dy++) {
                for (int dx = -20; dx <= 20; dx++) {
                    var endCheat = startCheat.add(dx, dy);
                    Integer endDistance = endDistances.get(endCheat);
                    if (endDistance == null) {
                        continue;
                    }
                    int cheatDistance = startCheat.manhattanDistance(endCheat);
                    if (cheatDistance > 20) {
                        continue;
                    }
                    int distance = startDistance + cheatDistance + endDistance;
                    if (distance <= length - 100) {
                        saved++;
                    }
                }
            }
        }
        System.out.println(saved);
    }

    private static Iterable<CharMatrix.Position> neighbours(CharMatrix.Position p) {
        return p.getNeighbours().stream().filter(pp -> pp.get() != '#').toList();
    }
}
