package ui;

import java.util.*;
import java.util.stream.Collectors;

public class Dataset {

    private List<String> targetFeatureValues;
    private String targetFeature;
    private List<List<String>> trainingManual;

    private List<Feature> features = new ArrayList<>();

    public List<Feature> getFeatures() {
        return features;
    }

    public String getTargetFeature() {
        return targetFeature;
    }

    public List<String> getTargetFeatureValues() {
        return targetFeatureValues;
    }

    public List<List<String>> getTrainingManual() {
        return trainingManual;
    }

    public Dataset(List<String> trainingFileData) {

        if (trainingFileData == null || trainingFileData.isEmpty()) {
            System.out.println("Invalid arguments given!");
            return;
        }
        List<String> temp = Arrays.stream(trainingFileData.remove(0).split(",")).collect(Collectors.toList());
        for (String featureName : temp.subList(0, temp.size() - 1)) {
            features.add(new Feature(featureName));
        }
        this.targetFeature = temp.get(temp.size() - 1);

        this.trainingManual = new ArrayList<>();
        trainingFileData.forEach(s -> {
            this.trainingManual.add(Arrays.stream(s.split(",")).collect(Collectors.toList()));
        });
        this.targetFeatureValues = findTargetFeatureValues();

    }

    public Dataset(Dataset parent, Feature feature, FeatureValue featureValue) {

        List<List<String>> trainingData = parent.getTrainingManual();
        List<List<String>> newTrainingData = new ArrayList<>();
        for (List<String> row : trainingData){
            if(row.contains(featureValue.getName())){
                List<String> tempRow = new ArrayList<>();
                for(String element : row){
                    if(!element.equalsIgnoreCase(featureValue.getName())){
                        tempRow.add(element);
                    }
                }
                newTrainingData.add(tempRow);
            }
        }

        this.trainingManual = newTrainingData;
        this.features = parent.getFeatures()
                .stream()
                .filter(feature1 -> !feature1.getName().equalsIgnoreCase(feature.getName()))
                .map(Feature::getName)
                .map(Feature::new)
                .collect(Collectors.toList());
        this.targetFeature = parent.getTargetFeature();
        this.targetFeatureValues = findTargetFeatureValues();
        giveFeaturesValues();
    }

    private List<String> findTargetFeatureValues() {
        Set<String> temp = new HashSet<>();
        for (List<String> row : trainingManual) {
            temp.add(row.get(features.size()));
        }
        return new ArrayList<>(temp);
    }

    public void giveFeaturesValues() {
        for (Feature feature : features) {
            int indexOfFeature = features.indexOf(feature);
            int indexOfTargetValue = features.size();
            for (List<String> row : trainingManual) {
                String value = row.get(indexOfFeature);
                String targetForValue = row.get(indexOfTargetValue);
                feature.appendNewFeatureValue(value, targetFeatureValues);
                feature.incrementFeatureValueTarget(value, targetForValue);
            }
        }
    }

    public double calculateEntropy() {
        return features.get(0).calculateEntropy();
    }


}
