import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day8 {
    public static void main(String[] args) throws IOException {
        Map<Character, List<CharMatrix.Position>> freqs = readInput();
        first(freqs);
        second(freqs);
    }

    private static Map<Character, List<CharMatrix.Position>> readInput() throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input8.txt"), '.');
        Map<Character, List<CharMatrix.Position>> freqs = new HashMap<>();
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                char c = charMatrix.get(x, y);
                if (c != '.') {
                    freqs.computeIfAbsent(c, key -> new ArrayList<>()).add(charMatrix.new Position(x, y));
                }
            }
        }
        return freqs;
    }

    private static void first(Map<Character, List<CharMatrix.Position>> freqs) {
        Set<CharMatrix.Position> antinodes = new HashSet<>();
        for (var entry : freqs.entrySet()) {
            var poslist = entry.getValue();
            for (var a : poslist) {
                for (var b : poslist) {
                    if (!a.equals(b)) {
                        int dx = b.getX() - a.getX();
                        int dy = b.getY() - a.getY();
                        CharMatrix.Position c = b.add(dx, dy);
                        if (c.isValid()) {
                            antinodes.add(c);
                        }
                    }
                }
            }
        }
        System.out.println(antinodes.size());
    }

    private static void second(Map<Character, List<CharMatrix.Position>> freqs) {
        Set<CharMatrix.Position> antinodes = new HashSet<>();
        for (var entry : freqs.entrySet()) {
            var poslist = entry.getValue();
            for (var a : poslist) {
                for (var b : poslist) {
                    if (!a.equals(b)) {
                        int dx = b.getX() - a.getX();
                        int dy = b.getY() - a.getY();
                        CharMatrix.Position c = b;
                        while (c.isValid()) {
                            antinodes.add(c);
                            c = c.add(dx, dy);
                        }
                    }
                }
            }
        }
        System.out.println(antinodes.size());
    }
}
