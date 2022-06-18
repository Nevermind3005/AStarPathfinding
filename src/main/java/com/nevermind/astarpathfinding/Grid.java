package com.nevermind.astarpathfinding;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Grid extends Canvas {

    public Box[] boxes;
    public Node[] nodes;
    private Group group;
    private int nodeSize;
    private int nodeWidth;
    private int nodeHeight;
    private double nodeGap;
    private Node startNode;
    private Node endNode;

    public Grid(Group group) {
        this.boxes = new Box[16 * 16];
        this.nodes = new Node[16 * 16];
        this.group = group;
        this.nodeSize = 600 / 16;
        this.nodeWidth = 16;
        this.nodeHeight = 16;
        this.nodeGap = 4;
        this.startNode = null;
        this.endNode = null;
        generateBoxes();
        generateNodes();
        connectNeighbourNodes();
    }

    private void generateBoxes() {
        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeHeight; y++) {
                this.nodes[y * nodeWidth + x] = new Node(x ,y);
            }
        }
    }

    private void generateNodes() {
        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeHeight; y++) {
                this.boxes[y * nodeWidth + x] = new Box(x * nodeSize + nodeGap, y * nodeSize + nodeGap, nodeSize, nodeSize);
                boxes[y * nodeWidth + x].draw(Color.CORAL, Color.PINK);
                group.getChildren().add(boxes[y * nodeWidth + x]);
            }
        }
        startNode = nodes[nodeWidth / 2 * nodeWidth + 1];
        endNode = nodes[nodeWidth / 2 * nodeWidth + nodeHeight - 2];
    }

    private void connectNeighbourNodes() {
        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeHeight; y++) {
                if (x < nodeWidth - 1) {
                    nodes[y * nodeWidth + x].neighbours.add(nodes[y * nodeWidth + (x  + 1)]);
                }
                if (x > 0) {
                    nodes[y * nodeWidth + x].neighbours.add(nodes[y * nodeWidth + (x  - 1)]);
                }
                if (y < nodeHeight - 1) {
                    nodes[y * nodeWidth + x].neighbours.add(nodes[(y + 1) * nodeWidth + x]);
                }
                if (y > 0) {
                    nodes[y * nodeWidth + x].neighbours.add(nodes[(y - 1) * nodeWidth + x]);
                }
            }
        }
    }

    //Redraws the grid
    public void updateBoxes() {
        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeWidth; y++) {
                boxes[y * nodeWidth + x].draw(Color.CORAL, Color.PINK);
                if (nodes[y * nodeWidth + x].visited) {
                    boxes[y * nodeWidth + x].draw(Color.LIGHTCORAL, Color.PINK);
                }
                if (nodes[y * nodeWidth + x].equals(startNode)) {
                    boxes[y * nodeWidth + x].draw(Color.GREEN, Color.PINK);
                }
                if (nodes[y * nodeWidth + x].equals(endNode)) {
                    boxes[y * nodeWidth + x].draw(Color.RED, Color.PINK);
                }
                if (nodes[y * nodeWidth + x].isBorder) {
                    boxes[y * nodeWidth + x].draw(Color.BLACK, Color.PINK);
                }
            }
        }
    }

    public void drawPath() {
        if (endNode != null) {
            Node p = endNode.parent;
            while (p != null && p != startNode) {
                boxes[p.y * nodeWidth + p.x].draw(Color.ORCHID,Color.WHITE);
                p = p.parent;
            }
        }
    }

    //
    public void setMousePos(double mouseX, double mouseY) {
        int x = (int) (mouseX / nodeSize);
        int y = (int) (mouseY / nodeSize);
        if (Input.keys.contains(KeyCode.S)) {
            startNode = nodes[y * nodeWidth + x];
            updateBoxes();
            findPath();
        } else if (Input.keys.contains(KeyCode.E)) {
            endNode = nodes[y * nodeWidth + x];
            updateBoxes();
            findPath();
        } else if (x < nodeWidth && y < nodeHeight) {
            nodes[y * nodeWidth + x].isBorder = !nodes[y * nodeWidth + x].isBorder;
            updateBoxes();
            findPath();
        }
    }

    //Returns the distance between two nodes
    private double distance(Node a, Node b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    //A* path finding alg
    public void findPath() {
        //Reset all nodes to default state
        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeHeight; y++) {
                nodes[y * nodeWidth + x].visited = false;
                nodes[y * nodeWidth + x].parent = null;
                nodes[y * nodeWidth + x].localGoal = Double.POSITIVE_INFINITY;
                nodes[y * nodeWidth + x].globalGoal = Double.POSITIVE_INFINITY;
            }
        }
        Node currentNode = startNode;
        startNode.localGoal = 0.0d;
        startNode.globalGoal = distance(startNode, endNode);
        LinkedList<Node> notVisited = new LinkedList<>();
        notVisited.add(startNode);
        while (!notVisited.isEmpty() && currentNode != endNode) {
            Collections.sort(notVisited, Node::compareTo);
            while (!notVisited.isEmpty() && notVisited.getFirst().visited) {
                notVisited.removeFirst();
            }
            if (notVisited.isEmpty()) {
                break;
            }
            currentNode = notVisited.getFirst();
            currentNode.visited = true;
            for (Node neighbourNode:currentNode.neighbours) {
                if (!neighbourNode.visited && neighbourNode.isBorder == false) {
                    notVisited.addLast(neighbourNode);
                }
                double possiblyLowerGoal = currentNode.localGoal + distance(currentNode, neighbourNode);
                if (possiblyLowerGoal < neighbourNode.localGoal) {
                    neighbourNode.parent = currentNode;
                    neighbourNode.localGoal = possiblyLowerGoal;
                    neighbourNode.globalGoal = neighbourNode.localGoal + distance(neighbourNode, endNode);
                }
            }
        }
        updateBoxes();
        drawPath();
    }
}
