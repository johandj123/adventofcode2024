import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day11 {
    public static void main(String[] args) throws IOException {
        List<Long> input = InputUtil.readAsIntegers("input11.txt").stream().map(x -> (long) x).toList();
        first(input);
        second(input);
    }

    private static void first(List<Long> input) {
        for (int i = 0; i < 25; i++) {
            input = blink(input);
        }
        System.out.println(input.size());
    }

    private static void second(List<Long> input) {
        Counter<Long> list = new Counter<>();
        for (long i : input) list.inc(i);
        for (int i = 0; i < 75; i++) {
            list = blink2(list);
        }
        System.out.println(list.size());
    }

    private static List<Long> blink(List<Long> input) {
        List<Long> result = new ArrayList<>();
        for (long i : input) {
            if (i == 0L) {
                result.add(1L);
            } else if ((Long.toString(i).length() % 2) == 0) {
                String s = Long.toString(i);
                result.add(Long.parseLong(s.substring(0, s.length() / 2)));
                result.add(Long.parseLong(s.substring(s.length() / 2)));
            } else {
                result.add(i * 2024);
            }
        }
        return result;
    }

    private static Counter<Long> blink2(Counter<Long> input) {
        Counter<Long> result = new Counter<>();
        for (var entry : input.entrySet()) {
            long i = entry.getKey();
            long count = entry.getValue();
            if (i == 0L) {
                result.add(1L, count);
            } else if ((Long.toString(i).length() % 2) == 0) {
                String s = Long.toString(i);
                result.add(Long.parseLong(s.substring(0, s.length() / 2)), count);
                result.add(Long.parseLong(s.substring(s.length() / 2)), count);
            } else {
                result.add(i * 2024, count);
            }
        }
        return result;
    }

    private static class Counter<T> {
        private final Map<T, Long> map = new HashMap<>();

        public void inc(T key) {
            map.put(key, map.getOrDefault(key, 0L) + 1);
        }

        public void add(T key, long count) {
            map.put(key, map.getOrDefault(key, 0L) + count);
        }

        public Set<Map.Entry<T, Long>> entrySet() {
            return map.entrySet();
        }

        public long size() {
            return map.values().stream().mapToLong(x -> x).sum();
        }
    }
}
