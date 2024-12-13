import lib.BigRational;
import lib.InputUtil;
import lib.simplex.ConstraintType;
import lib.simplex.Simplex;
import lib.simplex.Solution;
import lib.simplex.VariableType;

import java.io.IOException;
import java.util.List;

public class Day13 {
    public static void main(String[] args) throws IOException {
        List<Machine> machines = InputUtil.readAsStringGroups("input13.txt").stream().map(Machine::new).toList();
        int first = machines.stream().mapToInt(Machine::solve).sum();
        System.out.println(first);
        long second = machines.stream().mapToLong(Machine::solve2).sum();
        System.out.println(second);
    }

    static class Machine {
        final List<Integer> list;

        Machine(String s) {
            list = InputUtil.extractPositiveIntegers(s);
        }

        int solve() {
            Simplex simplex = new Simplex();
            simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, new BigRational(-3));
            simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, new BigRational(-1));
            simplex.addConstraint(ConstraintType.EQUAL, new BigRational(list.get(4)));
            simplex.addConstraintTerm(new BigRational(list.get(0)), 0);
            simplex.addConstraintTerm(new BigRational(list.get(2)), 1);
            simplex.addConstraint(ConstraintType.EQUAL, new BigRational(list.get(5)));
            simplex.addConstraintTerm(new BigRational(list.get(1)), 0);
            simplex.addConstraintTerm(new BigRational(list.get(3)), 1);
            Solution solution = simplex.solve();
            return solution == null ? 0 : (int)-solution.getValue().longValueExact();
        }

        long solve2() {
            Simplex simplex = new Simplex();
            simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, new BigRational(-3));
            simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, new BigRational(-1));
            simplex.addConstraint(ConstraintType.EQUAL, new BigRational(list.get(4) + 10000000000000L));
            simplex.addConstraintTerm(new BigRational(list.get(0)), 0);
            simplex.addConstraintTerm(new BigRational(list.get(2)), 1);
            simplex.addConstraint(ConstraintType.EQUAL, new BigRational(list.get(5) + 10000000000000L));
            simplex.addConstraintTerm(new BigRational(list.get(1)), 0);
            simplex.addConstraintTerm(new BigRational(list.get(3)), 1);
            Solution solution = simplex.solve();
            return solution == null ? 0 : -solution.getValue().longValueExact();
        }
    }
}
