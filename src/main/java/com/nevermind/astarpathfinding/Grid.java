package com.nevermind.astarpathfinding;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

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
    private AStar aStar;

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
        aStar = new AStar(nodes, nodeWidth, nodeHeight, startNode, endNode, this);
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
            aStar.setStartNode(startNode);
            aStar.findPath();
        } else if (Input.keys.contains(KeyCode.E)) {
            endNode = nodes[y * nodeWidth + x];
            updateBoxes();
            aStar.setEndNode(endNode);
            aStar.findPath();
        } else if (x < nodeWidth && y < nodeHeight) {
            nodes[y * nodeWidth + x].isBorder = !nodes[y * nodeWidth + x].isBorder;
            updateBoxes();
            aStar.findPath();
        }
    }
}
