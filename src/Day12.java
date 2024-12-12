import lib.CharMatrix;
import lib.Direction;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input12.txt"), '.');
        int sum = 0;
        int sum2 = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                char c = position.get();
                if (c != '.') {
                    Set<CharMatrix.Position> region = GraphUtil.reachable(position,
                            p -> Direction.DIRECTIONS.stream()
                                    .map(p::add)
                                    .filter(p1 -> p1.getUnbounded() == c)
                                    .toList());
                    int area = region.size();
                    int perimeter = (int) region.stream()
                            .flatMap(p -> Direction.DIRECTIONS.stream().map(p::add))
                            .filter(p -> !region.contains(p))
                            .count();
                    Set<Border> border = region.stream()
                            .flatMap(p -> Direction.DIRECTIONS.stream().map(d -> new Border(p.add(d), d)))
                            .filter(r -> !region.contains(r.position))
                            .collect(Collectors.toSet());
                    sum += (area * perimeter);
                    int sides = 0;
                    while (!border.isEmpty()) {
                        extractFence(border);
                        sides++;
                    }
                    sum2 += (area * sides);
                    region.forEach(p -> p.set('.'));
                }
            }
        }
        System.out.println(sum);
        System.out.println(sum2);
    }

    static void extractFence(Set<Border> border) {
        Border start = border.iterator().next();
        border.remove(start);
        Border cur = start;
        while (true) {
            cur = new Border(cur.position.add(start.direction.turnLeft()), cur.direction);
            if (!border.contains(cur)) {
                break;
            }
            border.remove(cur);
        }
        cur = start;
        while (true) {
            cur = new Border(cur.position.add(start.direction.turnRight()), cur.direction);
            if (!border.contains(cur)) {
                break;
            }
            border.remove(cur);
        }
    }

    record Border(CharMatrix.Position position,Direction direction) {}
}
