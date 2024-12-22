import lib.InputUtil;
import lib.StepUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 {
    public static void main(String[] args) throws IOException {
        List<Integer> list = InputUtil.readAsIntegers("input22.txt");
        first(list);
        second(list);
    }

    private static void second(List<Integer> list) {
        List<Map<List<Integer>, Integer>> maps = list.stream().map(Day22::seqmap).toList();
        Set<List<Integer>> keys = maps.stream().flatMap(m -> m.keySet().stream()).collect(Collectors.toSet());
        int maxBananas = 0;
        for (var key : keys) {
            int bananas = 0;
            for (var map : maps) {
                bananas += map.getOrDefault(key, 0);
            }
            maxBananas = Math.max(maxBananas, bananas);
        }
        System.out.println(maxBananas);
    }

    private static void first(List<Integer> list) {
        long sum = list.stream()
                .mapToLong(secret -> StepUtil.performStepsWithCycleDetection(secret, 2000, Day22::next))
                .sum();
        System.out.println(sum);
    }

    static Map<List<Integer>, Integer> seqmap(int secret) {
        List<Integer> prices = new ArrayList<>(List.of(secret % 10));
        for (int i = 0; i < 2000; i++) {
            secret = next(secret);
            prices.add(secret % 10);
        }
        List<Integer> deltas = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            deltas.add(prices.get(i) - prices.get(i - 1));
        }

        Map<List<Integer>, Integer> map = new HashMap<>();
        for (int i = 3; i < deltas.size(); i++) {
            List<Integer> key = deltas.subList(i - 3, i + 1);
            if (!map.containsKey(key)) {
                map.put(key, prices.get(i + 1));
            }
        }
        return map;
    }

    static int next(int secret) {
        secret = mixandprune(secret, secret * 64);
        secret = mixandprune(secret, secret / 32);
        secret = mixandprune(secret, secret * 2048);
        return secret;
    }

    static int mixandprune(int current,int value) {
        return Math.floorMod(current ^ value, 16777216);
    }
}
