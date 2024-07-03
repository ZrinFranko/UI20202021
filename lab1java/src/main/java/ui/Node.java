package ui;

import java.util.*;

public class Node {

    private String name;
    private Node parent;
    private Map<String,Double> children;
    private double distanceToPickedChild;

    public Node() {

    }

    public Node(String name,Map<String,Double> children){
        this.name = name;
        this.children = children;

    }

    public Node(String name,Map<String,Double> children, Node parent) {
        this.name = name;
        this.children = children;
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        return Objects.equals(name, other.name);
    }

    public String getName() {
        return this.name;
    }

    public Node getParent() {
        return this.parent;
    }

    public Map<String,Double> getChildren() {
        return this.children;
    }

    public int sizeOfAncestry() {
        int i = 1;
        Node curr = this.getParent();
        while(curr != null) {
            i++;
            curr = curr.getParent();
        }
        return i;
    }

    public double distanceToMe() {
        double tempDistance = 0.0;
        String currentChild = this.getName();
        Node currParent = this.getParent();
        while(currParent != null) {
            tempDistance += currParent.getChildren().get(currentChild);
            currentChild = currParent.getName();
            currParent = currParent.getParent();
        }
        return tempDistance;
    }

    public String pathToMe() {
        StringBuilder sb = new StringBuilder();
        Deque<String> names = new LinkedList<>();
        names.addLast(this.getName());
        Node current = this.getParent();
        while(current != null) {
            names.addFirst(current.getName());
            current = current.getParent();
        }
        while(!names.isEmpty())
            sb.append(names.removeFirst()).append(" => ");
        return sb.substring(0,sb.length()-3);
    }

}
