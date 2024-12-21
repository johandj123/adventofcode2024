import lib.InputUtil;
import lib.Position;

import java.io.IOException;
import java.util.*;

public class Day21 {
    static final Map<Character, Position> KEYPAD1;
    static final Map<Character, Position> KEYPAD2;

    static {
        KEYPAD1 = new HashMap<>();
        KEYPAD1.put('7', new Position(0, 0));
        KEYPAD1.put('8', new Position(1, 0));
        KEYPAD1.put('9', new Position(2, 0));
        KEYPAD1.put('4', new Position(0, 1));
        KEYPAD1.put('5', new Position(1, 1));
        KEYPAD1.put('6', new Position(2, 1));
        KEYPAD1.put('1', new Position(0, 2));
        KEYPAD1.put('2', new Position(1, 2));
        KEYPAD1.put('3', new Position(2, 2));
        KEYPAD1.put('0', new Position(1, 3));
        KEYPAD1.put('A', new Position(2, 3));
        KEYPAD2 = new HashMap<>();
        KEYPAD2.put('^', new Position(1, 0));
        KEYPAD2.put('A', new Position(2, 0));
        KEYPAD2.put('<', new Position(0, 1));
        KEYPAD2.put('v', new Position(1, 1));
        KEYPAD2.put('>', new Position(2, 1));
    }

    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input21.txt");
        int first = input.stream().mapToInt(Day21::first).sum();
        System.out.println(first);
    }

    public static int first(String sequence) {
        Set<String> set = type(KEYPAD2, type(KEYPAD2, type(KEYPAD1, sequence)));
        int x = set.stream().mapToInt(String::length).min().orElseThrow();

        int y = Integer.parseInt(sequence.substring(0, 3));
        System.out.println(x + ";" + y);
        return x * y;
    }

    public static Set<String> type(Map<Character, Position> keypad, Set<String> sequences) {
        Set<String> result = new HashSet<>();
        for (String sequence : sequences) {
            result.addAll(type(keypad, sequence));
        }
        return result;
    }

    public static Set<String> type(Map<Character, Position> keypad, String sequence) {
        Set<Position> positions = new HashSet<>(keypad.values());
        List<List<String>> parts = new ArrayList<>();
        Position position = keypad.get('A');
        for (char c : sequence.toCharArray()) {
            Position dest = keypad.get(c);
            parts.add(options(position, dest, positions));
            position = dest;
        }
        Set<String> result = new HashSet<>();
        makeResult(result, "", parts);
        return result;
    }

    private static void makeResult(Set<String> result, String prefix, List<List<String>> parts) {
        if (parts.isEmpty()) {
            result.add(prefix);
            return;
        }
        List<String> headList = parts.get(0);
        for (String head : headList) {
            makeResult(result, prefix + head, parts.subList(1, parts.size()));
        }
    }

    public static List<String> options(Position src, Position dest, Set<Position> positions) {
        if (src.equals(dest)) {
            return List.of("A");
        }
        List<String> result = new ArrayList<>();
        if (src.x() < dest.x() && positions.contains(src.add(1, 0))) {
            result.addAll(options(src.add(1, 0), dest, positions).stream().map(s -> ">" + s).toList());
        }
        if (src.x() > dest.x() && positions.contains(src.add(-1, 0))) {
            result.addAll(options(src.add(-1, 0), dest, positions).stream().map(s -> "<" + s).toList());
        }
        if (src.y() < dest.y() && positions.contains(src.add(0, 1))) {
            result.addAll(options(src.add(0, 1), dest, positions).stream().map(s -> "v" + s).toList());
        }
        if (src.y() > dest.y() && positions.contains(src.add(0, -1))) {
            result.addAll(options(src.add(0, -1), dest, positions).stream().map(s -> "^" + s).toList());
        }
        return result;
    }
}
