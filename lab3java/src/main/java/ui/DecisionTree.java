package ui;

import java.util.*;

public class DecisionTree {

    private Integer maxDepth = Integer.MAX_VALUE;
    private TreeNode result;
    private Dataset initialDataset;

    public DecisionTree(String[] args) {
        if (args.length == 3) {
            this.maxDepth = Integer.parseInt(args[2]);
        }
        this.initialDataset = new Dataset(Util.readFile("C:\\Users\\zrinf\\Documents\\autograder\\data\\lab3\\files\\volleyball.csv"));
        fit();
    }

    private void fit() {

        initialDataset.giveFeaturesValues();
        result = ID3DecisionTree(initialDataset, null, initialDataset.getFeatures(), 0);

    }

    private TreeNode ID3DecisionTree(Dataset dataset, Dataset parent, List<Feature> features, int currentTreeDepth) {


        Map<String, Double> infoGainMap = new TreeMap<>();

        double maximumInfoGain = 0.0;
        Feature optimalFeatureToUse = null;
        for (Feature feature : features) {
            double infoGainOfFeature = calculateInformationGain(dataset, feature);
            infoGainMap.put(feature.getName(), infoGainOfFeature);
            if(infoGainOfFeature > maximumInfoGain){
                maximumInfoGain = infoGainOfFeature;
                optimalFeatureToUse = feature;
            }
        }
        infoGainMap.forEach( (k,v) ->  System.out.printf("IG(%s)=%f ",k ,v));
        System.out.println();
        Map<String,TreeNode> subtrees = new TreeMap<>();
        for(FeatureValue value : optimalFeatureToUse.getFeatureValues()) {
            Dataset newDataset = new Dataset(dataset, optimalFeatureToUse, value);
            TreeNode n = ID3DecisionTree(newDataset,dataset,newDataset.getFeatures(),currentTreeDepth+1);
            subtrees.put(value.getName(), n);
        }
        return new TreeNode(optimalFeatureToUse,subtrees);

    }

    private Double calculateInformationGain(Dataset dataset, Feature feature) {
        double infoGain = dataset.calculateEntropy();
        for (FeatureValue featureValue : feature.getFeatureValues()) {
            Dataset newDataset = new Dataset(dataset, feature, featureValue);
            double factor = 0.0;
            if (!newDataset.getTrainingManual().isEmpty()) {
                factor = (double) newDataset.getTrainingManual().size() / dataset.getTrainingManual().size();
            }
            infoGain -= factor * newDataset.calculateEntropy();
        }
        return infoGain;
    }

    public String printResult(){
        return result.toString();
    }
}
