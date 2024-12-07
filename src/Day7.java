import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<Line> lines = InputUtil.readAsLines("input7.txt").stream()
                .map(Line::new)
                .toList();
        System.out.println(lines.stream().filter(Line::check).mapToLong(l -> l.test).sum());
        System.out.println(lines.stream().filter(Line::check2).mapToLong(l -> l.test).sum());
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

        public boolean check() {
            for (int i = 0; i < (1 << (values.size() - 1)); i++) {
                long cur = values.get(0);
                for (int j = 0; j < values.size() - 1; j++) {
                    int maskval = i & (1 << j);
                    if (maskval == 0) {
                        cur = cur + values.get(j + 1);
                    } else {
                        cur = cur * values.get(j + 1);
                    }
                }
                if (cur == test) {
                    return true;
                }
            }
            return false;
        }

        public boolean check2() {
            outer:
            for (int i = 0; i < (1 << (2 * (values.size() - 1))); i++) {
                long cur = values.get(0);
                for (int j = 0; j < values.size() - 1; j++) {
                    int maskval = (i >> (2 * j)) & 3;
                    long next = values.get(j + 1);
                    if (maskval == 0) {
                        cur = cur + next;
                    } else if (maskval == 1) {
                        cur = cur * next;
                    } else if (maskval == 2) {
                        cur = Long.parseLong(cur + Long.toString(next));
                    } else {
                        continue outer;
                    }
                }
                if (cur == test) {
                    return true;
                }
            }
            return false;
        }
    }
}
