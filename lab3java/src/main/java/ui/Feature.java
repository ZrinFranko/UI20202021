package ui;

import java.util.*;

public class Feature {

    private String name;

    private List<FeatureValue> featureValues;

    public Feature(String name) {
        this.name = name;
        this.featureValues = new ArrayList<>();
    }

    public boolean appendNewFeatureValue(String name, List<String> targetValues) {
        FeatureValue temp = new FeatureValue(this, name, targetValues);
        if (!featureValues.contains(temp)) {
            return featureValues.add(temp);
        } else {
            featureValues.get(featureValues.indexOf(temp)).incrementRepetition();
        }
        return false;
    }

    public List<FeatureValue> getFeatureValues() {
        return featureValues;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return Objects.equals(name, feature.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("My name is: ").append(getName()).append("\n").append("My possible values are: \n");
        for (FeatureValue featureValue : getFeatureValues()) {
            sb.append(featureValue.toString()).append("\n");
        }
        return sb.toString();
    }

    public void incrementFeatureValueTarget(String value, String targetForValue) {
        for (FeatureValue featureValue : getFeatureValues()) {
            if (featureValue.getName().equalsIgnoreCase(value)) {
                featureValue.incrementConditionValueForTargetValue(targetForValue);
            }
        }
    }

    public double calculateEntropy() {
        double setAmount = 0;
        double sum = 0;
        Map<String, Integer> endResultValues = new HashMap<>();

        for (FeatureValue featureValue : getFeatureValues()) {
            for (Map.Entry<String, Integer> entry : featureValue.getConditionTargetValues().entrySet()) {
                endResultValues.put(entry.getKey(), endResultValues.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        for (Integer integer : endResultValues.values()) {
            setAmount += integer;
        }
        for (Integer count : endResultValues.values()) {
            double probability = count / setAmount;
            sum -= probability * (Math.log(probability) / Math.log(2));
        }
        return sum;

    }

    public FeatureValue getMostCommon(){
        FeatureValue tempMaxValue = null;
        int tempMax = 0;
        for(FeatureValue featureValue : getFeatureValues()){
            if(featureValue.getRepetitions() > tempMax){
                tempMaxValue = featureValue;
                tempMax = featureValue.getRepetitions();
            }
        }
        return tempMaxValue;
    }

}
