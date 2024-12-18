import lib.*;

import java.io.IOException;
import java.util.List;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<Position> input = InputUtil.readAsLines("input18.txt").stream()
                .map(Day18::parse)
                .toList();
        CharMatrix charMatrix = new CharMatrix(71, 71, '.');
        for (var p : input.subList(0, 1024)) {
            charMatrix.set(p.x(),  p.y(), '#');
        }
//        System.out.println(charMatrix);
        int length = GraphUtil.breadthFirstSearch(charMatrix.new Position(0, 0),
                p -> Direction.DIRECTIONS.stream().map(p::add).filter(x -> x.isValid() && x.get() == '.').toList(),
                p -> charMatrix.new Position(70, 70).equals(p));
        System.out.println(length);

        for (int i = 1024; i < input.size(); i++) {
            var pp = input.get(i);
            charMatrix.set(pp.x(),  pp.y(), '#');
            try {
                GraphUtil.breadthFirstSearch(charMatrix.new Position(0, 0),
                        p -> Direction.DIRECTIONS.stream().map(p::add).filter(x -> x.isValid() && x.get() == '.').toList(),
                        p -> charMatrix.new Position(70, 70).equals(p));
            } catch (IllegalStateException e) {
                System.out.println(pp.x() + "," + pp.y());
                break;
            }
        }
    }

    private static Position parse(String line) {
        List<Integer> l = InputUtil.extractPositiveIntegers(line);
        return new Position(l.get(0), l.get(1));
    }
}
