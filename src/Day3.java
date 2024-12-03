import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input3.txt");
        calculate(input);
        second(input);
    }

    private static void second(String input) {
        StringBuilder sb = new StringBuilder();
        boolean on = true;
        for (int i = 0; i < input.length(); i++) {
            if (input.startsWith("do()", i)) {
                on = true;
            } else if (input.startsWith("don't()", i)) {
                on = false;
            } else if (on) {
                sb.append(input.charAt(i));
            }
        }
        calculate(sb.toString());
    }

    private static void calculate(String input) {
        List<String> list = InputUtil.extract(input, "mul\\(\\d{1,3},\\d{1,3}\\)");
        long first = 0L;
        for (String s : list) {
            List<Integer> nums = InputUtil.extractPositiveIntegers(s);
            long a = nums.get(0);
            long b = nums.get(1);
            first += (a * b);
        }
        System.out.println(first);
    }
}
