import lib.CollectionUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input1.txt");
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (String line : input) {
            String[] p = line.split(" +");
            left.add(Integer.parseInt(p[0]));
            right.add(Integer.parseInt(p[1]));
        }
        left.sort(null);
        right.sort(null);
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            sum += Math.abs(left.get(i) - right.get(i));
        }
        System.out.println(sum);
        var hist = CollectionUtil.calculateHistogram(right);
        long second = 0;
        for (int i : left) {
            second = second + (long) i * hist.get(i);
        }
        System.out.println(second);
    }
}
