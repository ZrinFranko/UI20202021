package ui;

import java.util.Comparator;
//Izrada klase na temelju klase laboratorijske vjezbe 1 koju sam predao godine 2020./2021. JMBAG 0036515289
public class BasicNode implements Comparable<BasicNode>{
    protected String name;
    protected BasicNode parent;
    protected double distance;
    public BasicNode(){}
    public BasicNode(String name,double distance) {
        super();
        this.name = name;
        this.distance = distance;
    }
    public BasicNode(String name, BasicNode parent,double distance) {
        super();
        this.name = name;
        this.parent = parent;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setParent(BasicNode parent) {
        this.parent = parent;
    }

    public BasicNode getParent() {
        return parent;
    }

    public double getDistance(){ return distance;}

    public double getDistanceFromStart(){
        double i = getDistance();
        BasicNode current = this.getParent();
        while(current != null) {
            i+= current.getDistance();
            current = current.getParent();
        }
        return i;
    }

    public int countPassedStates(){
        int i = 1;
        BasicNode current = this.getParent();
        while(current != null) {
            i++;
            current = current.getParent();
        }
        return i;
    }
    @Override
    public String toString(){
        if(parent==null){
            return name;
        }else {
            return parent + " => " + name;
        }
    }

    @Override
    public int compareTo(BasicNode o) {
        return Double.compare(this.distance,o.getDistance());
    }
}
