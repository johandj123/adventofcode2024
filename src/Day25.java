import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day25 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input25.txt");
        List<List<Integer>> locks = new ArrayList<>();
        List<List<Integer>> keys = new ArrayList<>();
        for (String s : input) {
            CharMatrix charMatrix = new CharMatrix(s);
            if (charMatrix.get(0, 0) == '#') {
                List<Integer> heights = determineHeights(charMatrix);
                locks.add(heights);
            } else {
                List<Integer> heights = determineHeights(charMatrix.mirrorVertical());
                keys.add(heights);
            }
        }
        int count = 0;
        for (var lock : locks) {
            label:
            for (var key : keys) {
                for (int i = 0; i < lock.size(); i++) {
                    if (lock.get(i) + key.get(i) > 5) {
                        continue label;
                    }
                }
                count++;
            }
        }
        System.out.println(count);
    }

    private static List<Integer> determineHeights(CharMatrix charMatrix) {
        List<Integer> heights = new ArrayList<>();
        for (int x = 0; x < charMatrix.getWidth(); x++) {
            int y = 1;
            while (charMatrix.get(x, y) == '#') {
                y++;
            }
            heights.add(y - 1);
        }
        return heights;
    }
}
