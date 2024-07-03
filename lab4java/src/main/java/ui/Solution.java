package ui;

import ui.net.FullyConnectedFactory;
import ui.net.FullyConnectedNetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solution {


    public static void main(String... args) {
        Config config = InputParser.parse(args);

        RunConfig runConfig = config.getRunConfig();
        FullyConnectedFactory networkFactory = new FullyConnectedFactory(config.getFullyConnectedConfig());

        List<FullyConnectedNetwork> currentPopulation = generateInitialPopulation(runConfig, networkFactory);
        List<FullyConnectedNetwork> nextPopulation = new ArrayList<>(runConfig.getPopsize());

        iterateThroughPopulations(runConfig, nextPopulation, currentPopulation, config);

        double testErr = nextPopulation.get(0).error(runConfig.getTestDataset());
        System.out.printf("[Test error]:  %.6f\n", testErr);
    }

    private static void iterateThroughPopulations(RunConfig runConfig, List<FullyConnectedNetwork> nextPopulation, List<FullyConnectedNetwork> currentPopulation, Config config) {
        for (int i = 1; i <= runConfig.getIterationCount(); i++) {
            nextPopulation.clear();

            if (i % 2000 == 0) {
                System.out.printf("[Train error @%d]: %.6f\n", i, currentPopulation.get(0).getError());
            }

            fillNextPopulation(nextPopulation, currentPopulation, config);

            currentPopulation.sort(Comparator.comparingDouble(FullyConnectedNetwork::getFitness).reversed());
            for (int j = 0; j < runConfig.getElitism(); j++) {
                nextPopulation.add(currentPopulation.get(j));
            }

            List<FullyConnectedNetwork> temp = currentPopulation;
            currentPopulation = nextPopulation;
            nextPopulation = temp;
        }
    }

    private static List<FullyConnectedNetwork> generateInitialPopulation(RunConfig runConfig, FullyConnectedFactory networkFactory) {
        List<FullyConnectedNetwork> currentPopulation = new ArrayList<>(runConfig.getPopsize());

        for (int i = 0; i < runConfig.getPopsize(); i++) {
            FullyConnectedNetwork neuralNet = networkFactory.createFullyConnectedNetwork();
            neuralNet.error(runConfig.getTestDataset());
            currentPopulation.add(neuralNet);
        }
        return currentPopulation;
    }

    private static void fillNextPopulation(List<FullyConnectedNetwork> nextPopulation, List<FullyConnectedNetwork> currentPopulation, Config config) {
        while (nextPopulation.size() < config.getRunConfig().getPopsize()) {
            FullyConnectedNetwork net1 = roulletteWheelSelection(currentPopulation);
            FullyConnectedNetwork net2;
            do {
                net2 = roulletteWheelSelection(currentPopulation);
            } while (net1 == net2);

            FullyConnectedNetwork crossingNet = new FullyConnectedNetwork(net1, net2);
            crossingNet.mutate(config.getFullyConnectedConfig().getMutation(), config.getFullyConnectedConfig().getStdDev());
            crossingNet.error(config.getRunConfig().getTestDataset());
            nextPopulation.add(crossingNet);
        }
    }

    private static FullyConnectedNetwork roulletteWheelSelection(List<FullyConnectedNetwork> currentPopulation) {
        double sum = currentPopulation.stream().mapToDouble(FullyConnectedNetwork::getFitness).sum();
        double chosen = Math.random() * sum;
        double area = 0;
        for (FullyConnectedNetwork nn : currentPopulation) {
            area += nn.getFitness();
            if (chosen < area) return nn;
        }
        return null;
    }

}
