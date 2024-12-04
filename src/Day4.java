import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;

public class Day4 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input4.txt"));
        int first = 0;
        for (int i = 0; i < charMatrix.getHeight(); i++) {
            first += count(charMatrix.getRow(i));
        }
        for (int i = 0; i < charMatrix.getWidth(); i++) {
            first += count(charMatrix.getColumn(i));
        }
        for (int i = 0; i < charMatrix.getWidth(); i++) {
            first += countDiagonal(charMatrix, i, i, 1, -1);
            first += countDiagonal(charMatrix, i, i + 1, 1, -1);
            first += countDiagonal(charMatrix, i, charMatrix.getWidth() - i - 1, 1, 1);
            first += countDiagonal(charMatrix, i, charMatrix.getWidth() - i - 2, 1, 1);
        }
        System.out.println(first);
        int second = 0;
        second += countShapes(charMatrix);
        System.out.println(second);
    }

    private static int countShapes(CharMatrix charMatrix) {
        int second = 0;
        for (int y = 0; y < charMatrix.getHeight() - 2; y++) {
            for (int x = 0; x < charMatrix.getWidth() - 2; x++) {
                if (shape(charMatrix, x, y)) {
                    second++;
                }
            }
        }
        return second;
    }

    private static int count(String s) {
        return InputUtil.extract(s, "XMAS").size() + InputUtil.extract(s, "SAMX").size();
    }

    private static int countDiagonal(CharMatrix charMatrix,int x,int y,int dx,int dy) {
        StringBuilder sb = new StringBuilder();
        CharMatrix.Position position = charMatrix.new Position(x, y);
        while (position.isValid()) {
            position = position.add(-dx, -dy);
        }
        position = position.add(dx, dy);
        while (position.isValid()) {
            sb.append(position.get());
            position = position.add(dx, dy);
        }
        return count(sb.toString());
    }

    private static boolean shape(CharMatrix charMatrix,int x,int y) {
        if (charMatrix.get(x, y) == 'M' &&
                charMatrix.get(x, y + 2) == 'M' &&
                charMatrix.get(x + 2, y) == 'S' &&
                charMatrix.get(x + 2, y + 2) == 'S' &&
                charMatrix.get(x + 1, y + 1) == 'A') return true;
        if (charMatrix.get(x, y) == 'S' &&
                charMatrix.get(x, y + 2) == 'M' &&
                charMatrix.get(x + 2, y) == 'S' &&
                charMatrix.get(x + 2, y + 2) == 'M' &&
                charMatrix.get(x + 1, y + 1) == 'A') return true;
        if (charMatrix.get(x, y) == 'M' &&
                charMatrix.get(x, y + 2) == 'S' &&
                charMatrix.get(x + 2, y) == 'M' &&
                charMatrix.get(x + 2, y + 2) == 'S' &&
                charMatrix.get(x + 1, y + 1) == 'A') return true;
        if (charMatrix.get(x, y) == 'S' &&
                charMatrix.get(x, y + 2) == 'S' &&
                charMatrix.get(x + 2, y) == 'M' &&
                charMatrix.get(x + 2, y + 2) == 'M' &&
                charMatrix.get(x + 1, y + 1) == 'A') return true;
        return false;
    }
}
