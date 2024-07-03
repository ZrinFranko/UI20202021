package ui.net;

import ui.data.Dataset;
import ui.data.Sample;

import java.util.List;

public class FullyConnectedNetwork {

    private final FullyConnectedLayer[] network;
    private double fitnessCache = 0.0;

    FullyConnectedNetwork(FullyConnectedLayer[] network) {
        this.network = network;
    }

    public FullyConnectedNetwork(FullyConnectedNetwork lparent, FullyConnectedNetwork rparent) {
        network = new FullyConnectedLayer[lparent.network.length];
        for (int i = 0; i < network.length; i++)
            network[i] = new FullyConnectedLayer(lparent.network[i], rparent.network[i]);
    }

    public double forwardPass(Sample inputData) {
        double[] layerResult = network[0].forwardPass(inputData.getValues());
        for (int i = 1; i < network.length; i++) {
            layerResult = network[i].forwardPass(layerResult);
        }
        return layerResult[0];
    }

    public void mutate(double mutationProbability, double standardDeviation) {
        for (FullyConnectedLayer layer : network) layer.mutate(mutationProbability, standardDeviation);
    }

    public double error(Dataset inputData) {
        List<Sample> dataList = inputData.getDataList();
        double sum = dataList
                .stream()
                .mapToDouble(sample -> Math.pow((sample.getLabel()-forwardPass(sample)), 2.0))
                .sum();
        double error = sum / dataList.size();
        fitnessCache = (1. / error);
        return error;
    }

    public double getFitness() {
        return fitnessCache;
    }

    public double getError() {
        return (1. / fitnessCache);
    }

}