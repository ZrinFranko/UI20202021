package ui;

import ui.data.Dataset;

public class RunConfig {
    private final Dataset trainDataset;
    private final Dataset testDataset;
    private final int popsize;
    private final int elitism;
    private final int iterationCount;

    public RunConfig(Dataset trainDataset, Dataset testDataset, int popsize, int elitism, int iterationCount) {
        this.elitism = elitism;
        this.iterationCount = iterationCount;
        this.popsize = popsize;
        this.testDataset = testDataset;
        this.trainDataset = trainDataset;
    }

    public int getElitism() {
        return elitism;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getPopsize() {
        return popsize;
    }

    public Dataset getTestDataset() {
        return testDataset;
    }

    public Dataset getTrainDataset() {
        return trainDataset;
    }
}