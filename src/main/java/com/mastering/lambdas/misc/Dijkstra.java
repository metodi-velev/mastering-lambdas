package com.mastering.lambdas.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dijkstra {
    private final Integer[][] matrix = {
            {1, 3, 1},
            {1, 5, 1},
            {1, 6, 1}
    };

    private final Integer[][] matrix2 = {
            {1, 3, 21},
            {1, 5, 6},
            {14, 3, 10}
    };

    public void calculateShortestPath(Node source) {
        source.setDistance(1);
        Set<Node> settledNodes = new HashSet<>();
        Queue<Node> unsettledNodes = new PriorityQueue<>(Collections.singleton(source));
        while (!unsettledNodes.isEmpty()) {
            Node currentNode = unsettledNodes.poll();
            currentNode.getAdjacentNodes()
                    .entrySet().stream()
                    .filter(entry -> !settledNodes.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNode);
                        unsettledNodes.add(entry.getKey());
                    });
            settledNodes.add(currentNode);
        }
    }

    private void evaluateDistanceAndPath(Node adjacentNode, Integer edgeWeight, Node sourceNode) {
        Integer newDistance = sourceNode.getDistance() + edgeWeight;
        if (newDistance < adjacentNode.getDistance()) {
            adjacentNode.setDistance(newDistance);
            adjacentNode.setShortestPath(
                    Stream.concat(sourceNode.getShortestPath().stream(), Stream.of(sourceNode)).toList()
            );
        }
    }

    private static void printPaths(List<Node> nodes) {
        nodes.forEach(node -> {
            String path = node.getShortestPath().stream()
                    .map(Node::getName)
                    .collect(Collectors.joining(" -> "));
            System.out.println(path.isBlank()
                    ? "%s : %s".formatted(node.getName(), node.getDistance())
                    : "%s -> %s : %s".formatted(path, node.getName(), node.getDistance()));
        });
    }

    public static void main(String[] args) {
        Dijkstra dijkstra = new Dijkstra();
        Integer[][] matrix = dijkstra.matrix;

        Node node00 = new Node(String.valueOf(matrix[0][0]));
        Node node01 = new Node(String.valueOf(matrix[0][1]));
        Node node02 = new Node(String.valueOf(matrix[0][2]));
        Node node10 = new Node(String.valueOf(matrix[1][0]));
        Node node11 = new Node(String.valueOf(matrix[1][1]));
        Node node12 = new Node(String.valueOf(matrix[1][2]));
        Node node20 = new Node(String.valueOf(matrix[2][0]));
        Node node21 = new Node(String.valueOf(matrix[2][1]));
        Node node22 = new Node(String.valueOf(matrix[2][2]));

        final Integer[][] matrix2 = {
                {1, 3, 1},
                {1, 5, 1},
                {1, 6, 1}
        };
        node00.addAdjacentNode(node01, matrix[0][1]);
        node00.addAdjacentNode(node10, matrix[1][0]);

        node01.addAdjacentNode(node02, matrix[0][2]);
        node01.addAdjacentNode(node11, matrix[1][1]);

        node02.addAdjacentNode(node12, matrix[1][2]);

        node10.addAdjacentNode(node11, matrix[1][1]);
        node10.addAdjacentNode(node20, matrix[2][0]);

        node11.addAdjacentNode(node12, matrix[1][2]);
        node11.addAdjacentNode(node21, matrix[2][1]);

        node12.addAdjacentNode(node22, matrix[2][2]);

        node20.addAdjacentNode(node21, matrix[2][1]);

        node21.addAdjacentNode(node22, matrix[2][2]);

        dijkstra.calculateShortestPath(node00);
        //printPaths(Arrays.asList(node00, node01, node02, node10, node11, node12, node20, node21, node22));
        printPaths(List.of(node22));
    }
}

@Getter
@Setter
@RequiredArgsConstructor
class Node implements Comparable<Node> {
    private final String name;
    private Integer distance = Integer.MAX_VALUE;
    private List<Node> shortestPath = new LinkedList<>();
    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addAdjacentNode(Node node, int weight) {
        adjacentNodes.put(node, weight);
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.distance, node.getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return Objects.equals(getName(), node.getName()) && Objects.equals(getDistance(), node.getDistance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDistance());
    }
}
