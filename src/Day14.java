import lib.CharMatrix;
import lib.Counter;
import lib.InputUtil;
import lib.Position;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14 {
    public static final int WIDTH = 101;
    public static final int HEIGHT = 103;

    public static void main(String[] args) throws IOException {
        List<Bot> bots = InputUtil.readAsLines("input14.txt").stream().map(Bot::new).toList();
        Counter<Integer> counter = new Counter<>();
        bots.stream()
                .map(b -> b.predict(100))
                .map(Day14::quadrant)
                .filter(q -> q > 0)
                .forEach(counter::inc);
        System.out.println(counter.values().stream().reduce((a, b) -> a * b).get());
        for (int t = 0; ; t++) {
            int tt = t;
            Set<Position> set = bots.stream().map(b -> b.predict(tt)).collect(Collectors.toSet());;
            if (set.size() == bots.size()) {
                System.out.println(t);
                System.out.println(matrixAt(bots, t));
                break;
            }
        }
    }

    private static CharMatrix matrixAt(List<Bot> bots, int t) {
        CharMatrix charMatrix = new CharMatrix(HEIGHT, WIDTH, '.');
        bots.stream().map(b -> b.predict(t)).forEach(p -> charMatrix.set(p.x(), p.y(), '#'));
        return charMatrix;
    }

    public static int quadrant(Position position) {
        int h = rel(position.x(), WIDTH / 2);
        int v = rel(position.y(), HEIGHT / 2);
        if (h == -1 || v == -1) {
            return 0;
        }
        return 1 + v * 2 + h;
    }

    public static int rel(int x,int b) {
        if (x < b) {
            return 0;
        } else if (x > b) {
            return 1;
        } else {
            return -1;
        }
    }

    static class Bot {
        Position p;
        Position v;

        Bot(String line) {
            List<Integer> l = InputUtil.extract(line, "-?\\d+").stream().map(Integer::parseInt).toList();
            p = new Position(l.get(0), l.get(1));
            v = new Position(l.get(2), l.get(3));
        }

        Position predict(int t) {
            return new Position(Math.floorMod(p.x() + t * v.x(), WIDTH), Math.floorMod(p.y() + t * v.y(), HEIGHT));
        }

        @Override
        public String toString() {
            return "Bot{" +
                    "p=" + p +
                    ", v=" + v +
                    '}';
        }
    }
}
