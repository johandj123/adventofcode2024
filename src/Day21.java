import lib.InputUtil;
import lib.Position;

import java.io.IOException;
import java.util.*;

public class Day21 {
    static final Map<Character, Position> KEYPAD1;
    static final Map<Character, Position> KEYPAD2;
    static final Map<Memo, Long> MEMO = new HashMap<>();

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
        long first = input.stream().mapToLong(sequence -> calculate(sequence, 2)).sum();
        System.out.println(first);
        long second = input.stream().mapToLong(sequence -> calculate(sequence, 25)).sum();
        System.out.println(second);
    }

    public static long calculate(String sequence, int depth) {
        long x = type1(sequence, depth);
        int y = Integer.parseInt(sequence.substring(0, 3));
        return x * y;
    }

    public static long type1(String sequence, int depth) {
        Set<Position> positions = new HashSet<>(KEYPAD1.values());
        Position position = KEYPAD1.get('A');
        long length = 0;
        for (char c : sequence.toCharArray()) {
            Position dest = KEYPAD1.get(c);
            List<String> optionList = options(position, dest, positions);
            long min = optionList.stream().mapToLong(opt -> type2(opt, depth)).min().orElseThrow();
            length += min;
            position = dest;
        }
        return length;
    }

    public static long type2(String sequence, int depth) {
        if (depth == 0) {
            return sequence.length();
        }
        Long memoResult = MEMO.get(new Memo(sequence, depth));
        if (memoResult != null) {
            return memoResult;
        }

        Set<Position> positions = new HashSet<>(KEYPAD2.values());
        Position position = KEYPAD2.get('A');
        long length = 0;
        for (char c : sequence.toCharArray()) {
            Position dest = KEYPAD2.get(c);
            List<String> optionList = options(position, dest, positions);
            long min = optionList.stream().mapToLong(opt -> type2(opt, depth - 1)).min().orElseThrow();
            length += min;
            position = dest;
        }
        MEMO.put(new Memo(sequence, depth), length);
        return length;
    }

    record Memo(String sequence, int depth) {}

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
