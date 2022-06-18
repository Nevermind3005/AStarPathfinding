package com.nevermind.astarpathfinding;

import java.util.Collections;
import java.util.LinkedList;

public class AStar {

    private Node[] nodes;
    private int nodeWidth;
    private int nodeHeight;
    private Node startNode;
    private Node endNode;
    private Grid grid;

    public AStar(Node[] nodes, int nodeWidth, int nodeHeight, Node startNode, Node endNode, Grid grid) {
        this.nodes = nodes;
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.startNode = startNode;
        this.endNode = endNode;
        this.grid = grid;
    }

    //Returns distance between two nodes
    private double distance(Node a, Node b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    //A* path finding algorithm
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
            //Sorts not visited nodes based on length between nodes
            Collections.sort(notVisited, Node::compareTo);
            //Removes visited nodes from list
            while (!notVisited.isEmpty() && notVisited.getFirst().visited) {
                notVisited.removeFirst();
            }
            if (notVisited.isEmpty()) {
                break;
            }
            currentNode = notVisited.getFirst();
            currentNode.visited = true;
            for (Node neighbourNode:currentNode.neighbours) {
                //Adds neighbour node if it was not visited and is not border
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
        grid.updateBoxes();
        grid.drawPath();
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
}
