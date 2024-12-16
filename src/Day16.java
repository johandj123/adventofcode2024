import lib.CharMatrix;
import lib.Direction;
import lib.GraphUtil;
import lib.InputUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day16 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input16.txt"));
        CharMatrix.Position start = charMatrix.find('S').orElseThrow();
        int score = GraphUtil.dijkstra(new Node(start, Direction.EAST), Node::getNeighbours, Node::isEnd);
        System.out.println(score);
        Set<CharMatrix.Position> positions = dijkstraFindAll(new Node(start, Direction.EAST), score);
        System.out.println(positions.size());
//        CharMatrix charMatrix1 = new CharMatrix(charMatrix);
//        positions.forEach(p -> charMatrix1.set(p.getX(), p.getY(), 'O'));
//        System.out.println(charMatrix1);
    }

    public static Set<CharMatrix.Position> dijkstraFindAll(Node start, int score) {
        Map<Node, Integer> distances = new HashMap<>();
        SortedSet<DijkstraNodeDistance<Node>> queue = new TreeSet<>(List.of(new DijkstraNodeDistance<Node>(0, start)));
        Map<Node, Set<Node>> parent = new HashMap<>();
        Set<Node> finalNodes = new HashSet<>();
        while (!queue.isEmpty()) {
            DijkstraNodeDistance<Node> current = queue.first();
            queue.remove(current);
            if (current.distance > score) {
                break;
            }
            if (current.distance == score && current.getNode().isEnd()) {
                finalNodes.add(current.node);
                continue;
            }
            for (Map.Entry<Node, Integer> entry : current.getNode().getNeighbours().entrySet()) {
                Node next = entry.getKey();
                int distance = current.getDistance() + entry.getValue();
                Integer oldDistance = distances.get(next);
                if (oldDistance == null || distance < oldDistance) {
                    if (oldDistance != null) {
                        queue.remove(new DijkstraNodeDistance<>(oldDistance, next));
                    }
                    queue.add(new DijkstraNodeDistance<>(distance, next));
                    distances.put(next, distance);
                    parent.put(next, new HashSet<>(Set.of(current.node)));
                } else if (distance == oldDistance) {
                    parent.get(next).add(current.node);
                }
            }
        }

        Set<Node> nodes = new HashSet<>();
        for (Node node : finalNodes) {
            nodes.addAll(GraphUtil.reachable(node, n -> parent.getOrDefault(n, Collections.emptySet())));
        }
        return nodes.stream().map(node -> node.position).collect(Collectors.toSet());
    }

    private static class DijkstraNodeDistance<Node extends Comparable<Node>> implements Comparable<DijkstraNodeDistance<Node>> {
        private final int distance;
        private final Node node;

        public DijkstraNodeDistance(int distance, Node node) {
            Objects.requireNonNull(node);
            this.distance = distance;
            this.node = node;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DijkstraNodeDistance<?> that = (DijkstraNodeDistance<?>) o;
            return distance == that.distance && node.equals(that.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(distance, node);
        }

        @Override
        public int compareTo(DijkstraNodeDistance<Node> o) {
            if (distance < o.distance) {
                return -1;
            }
            if (distance > o.distance) {
                return 1;
            }
            return node.compareTo(o.node);
        }

        public int getDistance() {
            return distance;
        }

        public Node getNode() {
            return node;
        }
    }

    static class Node implements Comparable<Node> {
        final CharMatrix.Position position;
        final Direction direction;
        static final Comparator<Node> COMPARATOR = Comparator.comparing((Node n) -> n.position).thenComparing(n -> n.direction);

        public Node(CharMatrix.Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        public Map<Node, Integer> getNeighbours() {
            Map<Node, Integer> result = new HashMap<>();
            if (position.add(direction).get() != '#') {
                result.put(new Node(position.add(direction), direction), 1);
            }
            result.put(new Node(position, direction.turnLeft()), 1000);
            result.put(new Node(position, direction.turnRight()), 1000);
            return result;
        }

        public boolean isEnd() {
            return position.get() == 'E';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(position, node.position) && Objects.equals(direction, node.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "position=" + position +
                    ", direction=" + direction +
                    '}';
        }

        @Override
        public int compareTo(Node o) {
            return COMPARATOR.compare(this, o);
        }
    }
}
