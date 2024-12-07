import lib.CartesianProductUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = InputUtil.readAsLines("input7.txt").stream()
                .map(Line::new)
                .toList();
        System.out.println(lines.stream().filter(x -> x.check(List.of(
                Long::sum, (a, b) -> a * b
        ))).mapToLong(l -> l.test).sum());

        System.out.println(lines.stream().filter(x -> x.check(List.of(
                Long::sum, (a, b) -> a * b, (a, b) -> Long.parseLong(a + Long.toString(b))
        ))).mapToLong(l -> l.test).sum());
    }

    static class Line {
        long test;
        List<Integer> values;

        Line(String l) {
            String[] sp = l.split(": ");
            test = Long.parseLong(sp[0]);
            values = InputUtil.extractPositiveIntegers(sp[1]);
        }

        @Override
        public String toString() {
            return "Line{" +
                    "test=" + test +
                    ", values=" + values +
                    '}';
        }

        public boolean check(List<BiFunction<Long, Long, Long>> ops) {
            return CartesianProductUtil.cartesianProductStream(values.size() - 1, ops).anyMatch(this::test);
        }

        private boolean test(List<BiFunction<Long, Long, Long>> l) {
            long cur = values.get(0);
            for (int j = 0; j < values.size() - 1; j++) {
                cur = l.get(j).apply(cur, (long) values.get(j + 1));
            }
            return cur == test;
        }
    }
}
