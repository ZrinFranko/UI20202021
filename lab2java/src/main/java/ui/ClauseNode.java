package ui;

import java.util.*;
import java.util.stream.Collectors;

//Kod uzet od prošlogodišnje laboratorijske vježbe koju sam radio 0036515289

public class ClauseNode implements Comparable<ClauseNode>{
    protected int index;
    protected String value;
    protected List<ClauseNode> parents = new ArrayList<>();
    protected Set<String> elements = new HashSet<>();
    public ClauseNode(String value,int index){
        this.value = value;
        this.index = index;
        elements = Arrays.stream(this.value.split(" v ")).collect(Collectors.toSet());
    }

    public void addParent(ClauseNode p){
        parents.add(p);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Set<String> getElements() {
        return elements;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(index + ". ")
                .append(value);
        if(this.parents.size() != 0) {
            sb.append(" (");
            int j = 0;
            for (ClauseNode p : parents) {
                if(j == 0) {
                    sb.append(p.getIndex() + ", ");
                    j++;
                }else{
                    sb.append(p.getIndex());
                }
            }
            sb.append(")");
        }
        sb.append("\n");
        return  sb.toString();

    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(ClauseNode o) {
        if(this.getIndex() > o.getIndex())
            return 1;
        else if(this.getIndex() == o.getIndex())
            return 0;
        else
            return -1;
    }
}