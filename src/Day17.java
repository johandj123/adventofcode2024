import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {
    long[] register;
    List<Integer> program;
    int ip = 0;
    List<Integer> output = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input17.txt");
        long[] reg = InputUtil.extractPositiveIntegers(input.get(0)).stream().mapToLong(x -> x).toArray();
        List<Integer> program = InputUtil.extractPositiveIntegers(input.get(1));
        first(reg, program);
        second(program);
    }

    private static void first(long[] reg, List<Integer> program) {
        Day17 c = new Day17(reg, program);
        c.run();
        System.out.println(c.output.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")));
    }

    private static void second(List<Integer> program) {
        solve(0, program);
    }

    public static boolean solve(long A, List<Integer> program) {
        if (program.isEmpty()) {
            System.out.println(A);
            return true;
        }
        int w = program.get(program.size() - 1);
        A = A << 3;
        int v = 0;
        for (; v < 8; v++) {
            int B = v ^ 5;
            int C = (int) (((A | v) >> B) & 0x7);
            if ((B ^ C ^ 6) == w) {
                boolean ok = solve(A | v, program.subList(0, program.size() - 1));
                if (ok) {
                    return true;
                }
            }
        }
        return false;
    }

    public Day17(long[] register, List<Integer> program) {
        this.register = register;
        this.program = program;
    }

    public void run() {
        while (ip < program.size()) {
            int opcode = program.get(ip++);
            int literalOperand = program.get(ip++);
            long comboOperand = switch (literalOperand) {
                case 0, 1, 2, 3 -> literalOperand;
                case 4, 5, 6 -> register[literalOperand - 4];
                default -> throw new IllegalStateException();
            };
            switch (opcode) {
                case 0:
                    register[0] = register[0] / (1L << comboOperand);
                    break;
                case 1:
                    register[1] = register[1] ^ literalOperand;
                    break;
                case 2:
                    register[1] = comboOperand & 0x7;
                    break;
                case 3:
                    if (register[0] != 0) {
                        ip = literalOperand;
                    }
                    break;
                case 4:
                    register[1] = register[1] ^ register[2];
                    break;
                case 5:
                    output.add((int) (comboOperand & 0x7));
                    break;
                case 6:
                    register[1] = register[0] / (1L << comboOperand);
                    break;
                case 7:
                    register[2] = register[0] / (1L << comboOperand);
                    break;
            }
        }
    }
}
