import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input5.txt");
        List<Order> order = Arrays.stream(input.get(0).split("\n"))
                .map(Order::new).toList();
        List<List<Integer>> values = Arrays.stream(input.get(1).split("\n"))
                .map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).toList())
                .toList();
        int first = 0;
        int second = 0;
        for (var l : values) {
            if (checkOrder(l, order)) {
                first += l.get(l.size() / 2);
            } else {
                l = fixOrder(l, order);
                second += l.get(l.size() / 2);
            }
        }
        System.out.println(first);
        System.out.println(second);
    }

    private static boolean checkOrder(List<Integer> l,List<Order> order) {
        for (Order o : order) {
            int i1 = l.indexOf(o.a);
            int i2 = l.indexOf(o.b);
            if (i1 != -1 && i2 != -1 && i1 > i2) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> fixOrder(List<Integer> l,List<Order> order) {
        l = new ArrayList<>(l);
        boolean changed;

        do {
            changed = false;
            for (Order o : order) {
                int i1 = l.indexOf(o.a);
                int i2 = l.indexOf(o.b);
                if (i1 != -1 && i2 != -1 && i1 > i2) {
                    l.remove(i2);
                    l.add(i1, o.b);
                    changed = true;
                }
            }
        } while (changed);
        return l;
    }

    private static class Order {
        int a;
        int b;

        Order(String s) {
            String[] sp = s.split("\\|");
            a = Integer.parseInt(sp[0]);
            b = Integer.parseInt(sp[1]);
        }
    }
}
