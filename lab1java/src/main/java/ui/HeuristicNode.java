package ui;

import java.util.Comparator;
//Izrada klase na temelju klase laboratorijske vjezbe 1 koju sam predao godine 2020./2021. JMBAG 0036515289
public class HeuristicNode extends ui.BasicNode {
    private double heuristicD;
    public HeuristicNode(String name, HeuristicNode parent, double distance, double heuristicD) {
        super(name, parent, distance);
        this.heuristicD = heuristicD;
    }
    public double heuristicD() { return heuristicD; }
    @Override
    public HeuristicNode getParent() { return (HeuristicNode) super.getParent(); }
    @Override
    public String toString() {
        return super.toString();
    }
    public static final Comparator<HeuristicNode> COMPARE_BY_TOTAL=(n1, n2) -> Double.compare(n1.heuristicD(), n2.heuristicD());
}

