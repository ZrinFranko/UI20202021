package ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeatureValue {

    private Feature feature;

    private String name;

    private Integer repetitions;

    private Map<String, Integer> conditionTargetValues;


    public FeatureValue(Feature feature, String name, List<String> targetValues) {
        this.feature = feature;
        this.name = name;
        this.conditionTargetValues = new HashMap<>();
        this.repetitions = 1;
        for (String targetValue : targetValues) {
            conditionTargetValues.put(targetValue, 0);
        }
    }

    public Feature getFeature() {
        return feature;
    }

    public String getName() {
        return name;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public Map<String, Integer> getConditionTargetValues() {
        return conditionTargetValues;
    }

    public void incrementRepetition() {
        this.repetitions++;
    }

    public void incrementConditionValueForTargetValue(String targetValue) {
        conditionTargetValues.put(targetValue, conditionTargetValues.get(targetValue) + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("I am a value and my name is: ").append(getName()).append("\n");
        for (Map.Entry entry : conditionTargetValues.entrySet()) {
            sb.append("\tFor target ").append(entry.getKey()).append(" I am showing ").append(entry.getValue()).append(" amount of times.").append("\n");
        }
        sb.append("I am repeated ").append(getRepetitions()).append(" amount of times");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FeatureValue)) return false;
        FeatureValue that = (FeatureValue) o;
        return Objects.equals(feature, that.feature) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feature, name);
    }

    public String findMostCommonResultFromMe(){
        Integer max = 0;
        String maxValue = "";
        for(Map.Entry<String,Integer> entry : conditionTargetValues.entrySet()){
            if(entry.getValue() > max){
                max = entry.getValue();
                maxValue = entry.getKey();
            }
        }
        return maxValue;
    }
}
