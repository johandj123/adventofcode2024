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

    private static void second(Graph<String> graph) {
        Set<Set<String>> cliques = graph.maxCliques();
        int largest = cliques.stream().mapToInt(Set::size).max().orElseThrow();
        List<String> list = cliques.stream()
                .filter(clique -> clique.size() == largest)
                .map(ArrayList::new)
                .map(s -> s.stream().sorted().collect(Collectors.joining(",")))
                .toList();
        list.forEach(System.out::println);
    }
}
