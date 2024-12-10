import lib.CharMatrix;
import lib.Direction;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Day10 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input10.txt"));
        int sum = 0;
        int sum2 = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                CharMatrix.Position position = charMatrix.new Position(x, y);
                if (position.get() == '0') {
                    sum += trailheadScore(position);
                    sum2 += trailheadScore2(position);
                }
            }
        }
        System.out.println(sum);
        System.out.println(sum2);
    }

    private static int trailheadScore(CharMatrix.Position position) {
        Set<CharMatrix.Position> r = GraphUtil.reachable(position, Day10::neighbours);
        return (int) r.stream().filter(p -> p.get() == '9').count();
    }

    private static int trailheadScore2(CharMatrix.Position position) {
        Set<CharMatrix.Position> r = GraphUtil.reachable(position, Day10::neighbours);
        return r.stream().filter(p -> p.get() == '9').mapToInt(p -> paths(p, r)).sum();
    }

    private static int paths(CharMatrix.Position position, Set<CharMatrix.Position> r) {
        if (position.get() == '0') {
            return 1;
        }
        char c = (char) (position.get() - 1);
        return Direction.DIRECTIONS.stream()
                .filter(d -> r.contains(position.add(d)))
                .filter(d -> position.add(d).getUnbounded() == c)
                .map(position::add)
                .mapToInt(p -> paths(p, r))
                .sum();
    }

    private static List<CharMatrix.Position> neighbours(CharMatrix.Position position) {
        char c = (char) (position.get() + 1);
        return Direction.DIRECTIONS.stream()
                .filter(d -> position.add(d).getUnbounded() == c)
                .map(position::add)
                .toList();
    }
}
