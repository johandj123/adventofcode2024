import lib.Graph;
import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input23.txt");
        Graph<String> graph = new Graph<>();
        for (String line : input) {
            String[] sp = line.split("-");
            graph.addLinkBidirectional(sp[0], sp[1]);
        }

        first(graph);
        second(graph);
    }

    private static void second(Graph<String> graph) {
        Set<Set<String>> cliques = BronKerbosch2(graph);
        int largest = cliques.stream().mapToInt(Set::size).max().orElseThrow();
        List<String> list = cliques.stream()
                .filter(s -> s.size() == largest)
                .map(ArrayList::new)
                .map(s -> s.stream().sorted().collect(Collectors.joining(",")))
                .toList();
        list.forEach(System.out::println);
    }

    private static Set<Set<String>> BronKerbosch2(Graph<String> graph) {
        Set<Set<String>> cliques = new HashSet<>();
        BronKerbosch2(graph, new HashSet<>(), new HashSet<>(graph.getNodes()), new HashSet<>(), cliques);
        return cliques;
    }

    private static void BronKerbosch2(Graph<String> graph, Set<String> R, Set<String> P, Set<String> X, Set<Set<String>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            cliques.add(R);
            return;
        }
        String u = !P.isEmpty() ? P.iterator().next() : X.iterator().next();
        Set<String> Pu = new HashSet<>(P);
        Pu.removeAll(graph.getNeighbours(u));
        for (String v : Pu) {
            Set<String> R2 = new HashSet<>(R);
            R2.add(v);
            Set<String> n = graph.getNeighbours(v);
            Set<String> P2 = P.stream().filter(n::contains).collect(Collectors.toSet());
            Set<String> X2 = X.stream().filter(n::contains).collect(Collectors.toSet());
            BronKerbosch2(graph, R2, P2, X2, cliques);
            P.add(v);
            X.remove(v);
        }
    }

    private static void first(Graph<String> graph) {
        Set<Set<String>> threeSets = new HashSet<>();
        for (String a : graph.getNodes()) {
            for (String b : graph.getNodes()) {
                for (String c : graph.getNodes()) {
                    if (!a.equals(b) && !b.equals(c) && !a.equals(c) &&
                    graph.getNeighbours(a).contains(b) &&
                    graph.getNeighbours(b).contains(c) &&
                    graph.getNeighbours(a).contains(c)) {
                        threeSets.add(Set.of(a, b, c));
                    }
                }
            }
        }

        int count = 0;
        for (var set : threeSets) {
            if (set.stream().anyMatch(s -> s.startsWith("t"))) {
                count++;
            }
        }
        System.out.println(count);
    }
}
