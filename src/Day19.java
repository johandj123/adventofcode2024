import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input19.txt");
        List<String> patterns = Arrays.stream(input.get(0).split(", ")).toList();
        List<String> targets = Arrays.stream(input.get(1).split("\n")).toList();
        System.out.println(targets.stream().filter(t -> canMake(t, patterns)).count());
        Map<String, Long> memo = new HashMap<>();
        System.out.println(targets.stream().mapToLong(t -> count(t, patterns, memo)).sum());
    }

    public static boolean canMake(String target, List<String> patterns) {
        if (target.isEmpty()) {
            return true;
        }
        for (String pattern : patterns) {
            if (target.startsWith(pattern)) {
                if (canMake(target.substring(pattern.length()), patterns)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long count(String target, List<String> patterns, Map<String, Long> memo) {
        if (target.isEmpty()) {
            return 1;
        }
        Long m = memo.get(target);
        if (m != null) {
            return m;
        }
        long sum = 0;
        for (String pattern : patterns) {
            if (target.startsWith(pattern)) {
                sum += count(target.substring(pattern.length()), patterns, memo);
            }
        }
        memo.put(target, sum);
        return sum;
    }
}
