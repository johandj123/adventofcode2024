import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input24.txt");
        Map<String, Boolean> init = Arrays.stream(input.get(0).split("\n"))
                .map(Day24::parseInit)
                .collect(Collectors.toMap(InitValue::name, InitValue::value));
        List<Gate> gates = Arrays.stream(input.get(1).split("\n"))
                .map(Day24::parseGate)
                .toList();

        first(init, gates);
        second(init, gates);
    }

    private static void first(Map<String, Boolean> init, List<Gate> gates) {
        Map<String, Boolean> values = run(init, gates);
        System.out.println(numberOf(values, "z"));
    }

    private static Map<String, Boolean> run(Map<String, Boolean> init, List<Gate> gates) {
        Map<String, Boolean> values = new HashMap<>(init);
        List<Gate> unprocessedGates = new ArrayList<>(gates);
        boolean change = true;
        while (!unprocessedGates.isEmpty() && change) {
            change = false;
            for (var it = unprocessedGates.iterator(); it.hasNext(); ) {
                Gate gate = it.next();
                Boolean var1 = values.get(gate.name1());
                Boolean var2 = values.get(gate.name2());
                if (var1 != null && var2 != null) {
                    Boolean result = gate.type().apply(var1, var2);
                    values.put(gate.output(), result);
                    it.remove();
                    change = true;
                }
            }
        }
        return unprocessedGates.isEmpty() ? values : null;
    }

    private static long numberOf(Map<String, Boolean> values, String prefix) {
        long first = 0L;
        for (int i = 0; ; i++) {
            String name = String.format(prefix + "%02d", i);
            Boolean value = values.get(name);
            if (value == null) {
                break;
            }
            if (value) {
                first |= (1L << i);
            }
        }
        return first;
    }

    private static void second(Map<String, Boolean> init, List<Gate> gates) {
        Map<String, Gate> gateMap = gates.stream().collect(Collectors.toMap(Gate::output, g -> g));
        for (int i = 0; i <= 44; i++) {
            System.out.println(i + ":");
            String x = String.format("x%02d", i);
            String y = String.format("y%02d", i);
            Optional<Gate> xor = Optional.empty();
            Optional<Gate> and = Optional.empty();
            for (Gate gate : gates) {
                if (Set.of(x, y).equals(Set.of(gate.name1, gate.name2))) {
                    if (gate.type == Type.XOR) {
                        xor = Optional.of(gate);
                    } else if (gate.type == Type.AND) {
                        and = Optional.of(gate);
                    }
                }
            }
            xor.ifPresent(System.out::println);
            and.ifPresent(System.out::println);
        }
        Set<String> allErrors = new HashSet<>();
        for (int i = 2; i <= 44; i++) {
            String z = String.format("z%02d", i);
            String x = z.replace('z', 'x');
            String y = z.replace('z', 'y');
            String x0 = String.format("x%02d", i - 1);
            String y0 = String.format("y%02d", i - 1);
            Set<String> errors = mismatches(gateMap, z,
                    List.of("XOR",
                            List.of("XOR", x, y),
                            List.of("OR",
                                    List.of("AND", x0, y0),
                                    List.of("AND", List.of("XOR", x0, y0), "any")
                    )));
            allErrors.addAll(errors);
            if (!errors.isEmpty()) {
                System.out.println(i + ": " + errors);
            }
        }
        // Answer determined by manually expecting all mismatches found by the code above
        List<String> answer = List.of("z06", "fkp", "z11", "ngr", "z31", "mfm", "bpt", "krj");
        System.out.println(answer.stream().sorted().collect(Collectors.joining(",")));
    }

    private static Set<String> mismatches(Map<String, Gate> gateMap, String out, Object pattern) {
        if ("any".equals(pattern)) {
            return Collections.emptySet();
        }
        if (pattern instanceof String) {
            if (Objects.equals(out, pattern)) {
                return Collections.emptySet();
            }
            return Set.of(out);
        }
        if (!gateMap.containsKey(out)) {
            if (Objects.equals(out, pattern)) {
                return Collections.emptySet();
            }
            return Set.of(out);
        }
        Gate gate = gateMap.get(out);
        String a = gate.name1;
        String b = gate.name2;
        Type type = gate.type;
        List<String> patternList = (List<String>) pattern;
        if (!Objects.equals(patternList.get(0), type.name())) {
            return Set.of(out);
        }
        Set<String> ab = new HashSet<>();
        ab.addAll(mismatches(gateMap, a, patternList.get(1)));
        ab.addAll(mismatches(gateMap, b, patternList.get(2)));
        Set<String> ba = new HashSet<>();
        ba.addAll(mismatches(gateMap, b, patternList.get(1)));
        ba.addAll(mismatches(gateMap, a, patternList.get(2)));
        return ab.size() < ba.size() ? ab : ba;
    }

    static InitValue parseInit(String line) {
        String[] sp = line.split(": ");
        return new InitValue(sp[0], Integer.parseInt(sp[1]) != 0);
    }

    static Gate parseGate(String line) {
        String[] sp = line.split(" ");
        return new Gate(sp[0], sp[2], Type.valueOf(sp[1]), sp[4]);
    }

    record InitValue(String name, boolean value) {
    }

    record Gate(String name1, String name2, Type type, String output) {
    }

    enum Type {
        OR {
            @Override
            boolean apply(boolean value1, boolean value2) {
                return value1 || value2;
            }
        },
        AND {
            @Override
            boolean apply(boolean value1, boolean value2) {
                return value1 && value2;
            }
        },
        XOR {
            @Override
            boolean apply(boolean value1, boolean value2) {
                return value1 ^ value2;
            }
        };

        abstract boolean apply(boolean value1, boolean value2);
    }
}
