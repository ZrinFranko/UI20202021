package ui;

import java.util.Comparator;
import java.util.Map;

public class HeuristicNode extends CostNode {

    private double heuristic;

    public HeuristicNode() {
        super();
    }

    public HeuristicNode(String name, Map<String, Double> children, Node parent, double cost,double heuristic) {
        super(name, children, parent, cost);
        this.heuristic = heuristic;
    }

    public double getHeuristic() {
        return this.heuristic;
    }

    @Override
    public HeuristicNode getParent() {
        return (HeuristicNode) super.getParent();
    }

    public static final Comparator<HeuristicNode> COMPARE_BY_COST = (n1,n2) -> Double.compare(n1.getCost(), n2.getCost());
    public static final Comparator<HeuristicNode> COMPARE_BY_TOTAL = (n1,n2) -> Double.compare(n1.getHeuristic()+n1.distanceToMe(), n2.getHeuristic()+n2.distanceToMe());
    public static final Comparator<HeuristicNode> COMPARE_BY_HEURISTICS = (n1,n2) -> Double.compare(n1.getHeuristic(), n2.getHeuristic());

}
