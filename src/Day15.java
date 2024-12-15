import lib.CharMatrix;
import lib.Direction;
import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Day15 {
    static Map<Character, Direction> MOVES = Map.of('^', Direction.NORTH, 'v', Direction.SOUTH, '<', Direction.WEST, '>', Direction.EAST);

    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input15.txt");
        CharMatrix charMatrix = new CharMatrix(input.get(0), '.');
        String moves = input.get(1).replace("\n", "");

        first(charMatrix, moves);

        charMatrix = enlarge(new CharMatrix(input.get(0), '.'));
        second(charMatrix, moves);
    }

    private static CharMatrix enlarge(CharMatrix charMatrix) {
        CharMatrix result = new CharMatrix(charMatrix.getHeight(), charMatrix.getWidth() * 2, '.');
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                char c = charMatrix.get(x, y);
                String s = switch (c) {
                    case '#' -> "##";
                    case 'O' -> "[]";
                    case '.' -> "..";
                    case '@' -> "@.";
                    default -> throw new IllegalArgumentException();
                };
                result.set(x * 2, y, s.charAt(0));
                result.set(x * 2 + 1, y, s.charAt(1));
            }
        }
        return result;
    }

    private static void second(CharMatrix charMatrix, String moves) {
//        System.out.println(charMatrix);
        CharMatrix.Position position = charMatrix.find('@').get();
        for (char c : moves.toCharArray()) {
//            System.out.println("Move " + c);
            Direction direction = MOVES.get(c);
            if (direction.x() != 0) {
                CharMatrix.Position scan = position;
                do {
                    scan = scan.add(direction);
                } while (scan.getUnbounded() == '[' || scan.getUnbounded() == ']');
                if (scan.get() == '.') {
                    CharMatrix.Position cur = scan;
                    do {
                        CharMatrix.Position next = cur.add(direction.turnLeft().turnLeft());
                        cur.set(next.get());
                        cur = next;
                    } while (!cur.equals(position));
                    position.set('.');
                    position = position.add(direction);
                }
            } else {
                CharMatrix.Position next = position.add(direction);
                if (next.get() == '.') {
                    position.set('.');
                    next.set('@');
                    position = next;
                } else if (next.get() != '#' && canMoveBig(next, direction)) {
                    moveBig(next, direction);
                    position.set('.');
                    next.set('@');
                    position = next;
                }
            }
//            System.out.println(charMatrix);
        }

        int sum = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '[') {
                    sum += (y * 100 + x);
                }
            }
        }
        System.out.println(sum);
    }

    private static boolean canMoveBig(CharMatrix.Position position, Direction direction) {
        if (position.getUnbounded() == ']') {
            position = position.add(-1, 0);
        }
        for (int i = 0; i < 2; i++) {
            CharMatrix.Position next = position.add(direction).add(i, 0);
            if (next.getUnbounded() == '#') {
                return false;
            }
            if (next.getUnbounded() == '[' || next.getUnbounded() == ']') {
                if (!canMoveBig(next, direction)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void moveBig(CharMatrix.Position position, Direction direction) {
        if (position.getUnbounded() == ']') {
            position = position.add(-1, 0);
        }
        for (int i = 0; i < 2; i++) {
            CharMatrix.Position next = position.add(direction).add(i, 0);
            if (next.getUnbounded() == '[' || next.getUnbounded() == ']') {
                moveBig(next, direction);
            }
            next.set(i == 0 ? '[' : ']');
            position.add(i, 0).set('.');
        }
    }

    private static void first(CharMatrix charMatrix, String moves) {
        CharMatrix.Position position = charMatrix.find('@').get();
        for (char c : moves.toCharArray()) {
            Direction direction = MOVES.get(c);
            CharMatrix.Position scan = position;
            do {
                scan = scan.add(direction);
            } while (scan.getUnbounded() == 'O');
            if (scan.get() == '.') {
                scan.set('O');
                position.set('.');
                position = position.add(direction);
                position.set('@');
            }
//            System.out.println("Move " +  c);
//            System.out.println(charMatrix);
        }

        int sum = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == 'O') {
                    sum += (y * 100 + x);
                }
            }
        }
        System.out.println(sum);
    }
}
