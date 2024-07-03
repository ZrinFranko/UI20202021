package ui;

import ui.data.Dataset;
import ui.net.FullyConnectedConfig;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputParser {

    public static Config parse(String[] arguments) {
        Dataset train = Dataset.emptyDataset();
        Dataset test = Dataset.emptyDataset();
        int elitism = 1;
        int iterationCount = 2000;
        int popsize = 2;
        double mutation = 0.1;
        double stdev = 0.1;
        String nn = "5s";

        if (arguments.length % 2 != 0) {
            System.out.println("Incorrect input");
            System.exit(1);
        }

        for (int i = 0; i < arguments.length; i += 2) {
            if (arguments[i].equals("--train")) {
                train = new Dataset(arguments[i + 1]);
            } else if (arguments[i].equals("--test")) {
                test = new Dataset(arguments[i + 1]);
            } else if (arguments[i].equals("--nn")) {
                nn = arguments[i + 1];
            } else if (arguments[i].equals("--popsize")) {
                popsize = Integer.parseInt(arguments[i + 1]);
            } else if (arguments[i].equals("--elitism")) {
                elitism = Integer.parseInt(arguments[i + 1]);
            } else if (arguments[i].equals("--p")) {
                mutation = Double.parseDouble(arguments[i + 1]);
            } else if (arguments[i].equals("--K")) {
                stdev = Double.parseDouble(arguments[i + 1]);
            } else if (arguments[i].equals("--iter")) {
                iterationCount = Integer.parseInt(arguments[i + 1]);
            } else {
                System.out.println("Incorrect input");
                System.exit(1);
            }
        }

        List<Integer> dimensionPerLayer = Stream.of(nn.split("s")).map(Integer::parseInt).collect(Collectors.toList());
        FullyConnectedConfig fcc = new FullyConnectedConfig(train.getFeatureSize(), dimensionPerLayer, mutation, stdev);
        RunConfig rc = new RunConfig(train, test, popsize, elitism, iterationCount);
        return new Config(fcc, rc);
    }

}
