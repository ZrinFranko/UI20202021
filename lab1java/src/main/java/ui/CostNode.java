package ui;

import java.util.Map;

public class CostNode extends Node implements Comparable{

    private double cost;

    public CostNode(String name, Map<String, Double> children, Node parent,double cost) {
        super(name, children, parent);
        this.cost = cost;
    }
    public CostNode() {}

    public double getCost() {
        return cost;
    }

    @Override
    public CostNode getParent() {
        return (CostNode)super.getParent();
    }

    @Override
    public int compareTo(Object o) {
        CostNode cn = (CostNode)o;
        return Double.compare(this.getCost(), cn.getCost());
    }



}
