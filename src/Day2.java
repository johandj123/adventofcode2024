import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> lines = InputUtil.readAsLines("input2.txt");
        int count = 0;
        int count2 = 0;
        for (String line : lines) {
            List<Integer> list = InputUtil.splitIntoIntegers(line);
            if (safe(list)) count++;
            if (safe2(list)) count2++;
        }
        System.out.println(count);
        System.out.println(count2);
    }

    public static boolean safe2(List<Integer> list) {
        if (safe(list)) return true;
        for (int i = 0; i < list.size(); i++) {
            List<Integer> l = new ArrayList<>(list);
            l.remove(i);
            if (safe(l)) return true;
        }
        return false;
    }

    public static boolean safe(List<Integer> list) {
        return (increasing(list) || decreasing(list)) && deltacheck(list);
    }

    public static boolean increasing(List<Integer> list) {
        List<Integer> l = new ArrayList<>(list);
        l.sort(null);
        return list.equals(l);
    }

    public static boolean decreasing(List<Integer> list) {
        List<Integer> l = new ArrayList<>(list);
        l.sort(Comparator.reverseOrder());
        return list.equals(l);
    }

    public static boolean deltacheck(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int delta = Math.abs(list.get(i) - list.get(i + 1));
            if (delta == 0 || delta > 3) return false;
        }
        return true;
    }
}
