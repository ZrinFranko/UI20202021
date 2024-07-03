package ui.net;

import ui.net.activation.Activation;

import java.util.Random;

class FullyConnectedLayer {
    private static final double DEFAULT_DEVIATION = 0.01;
    private static final Random RANDOM_GENERATOR = new Random();

    private final double[][] weights;
    private final double[] bias;
    private final Activation activation;


    private FullyConnectedLayer(Activation function, int input, int output) {
        weights = new double[output][input];
        bias = new double[output];
        activation = function;
    }

    public FullyConnectedLayer(int inputSize, int outputSize, Activation function) {
        this(function, inputSize, outputSize);

        for (int i = 0; i < outputSize(); i++) {
            bias[i] = RANDOM_GENERATOR.nextGaussian() * DEFAULT_DEVIATION;
            for (int j = 0; j < inputSize(); j++)
                weights[i][j] = RANDOM_GENERATOR.nextGaussian() * DEFAULT_DEVIATION;
        }
    }

    public FullyConnectedLayer(FullyConnectedLayer leftLayer, FullyConnectedLayer rightLayer) {
        this(leftLayer.activation, leftLayer.inputSize(), leftLayer.outputSize());
        for (int i = 0; i < outputSize(); i++) {
            bias[i] = average(leftLayer.bias[i], rightLayer.bias[i]);
            for (int j = 0; j < inputSize(); j++)
                weights[i][j] = average(leftLayer.weights[i][j], rightLayer.weights[i][j]);
        }
    }

    private static double average(double left, double right) {
        return (left + right) / 2;
    }

    public double[] forwardPass(double[] input) {
        double[] output = new double[outputSize()];

        for (int i = 0; i < outputSize(); i++) {
            double sum = 0;
            for (int j = 0; j < inputSize(); j++) {
                sum += weights[i][j] * input[j];
            }
            output[i] = sum + bias[i];
            output[i] = activation.pass(output[i]);
        }
        return output;
    }

    public void mutate(double mutationProbability, double standardDeviation) {
        for (int i = 0; i < outputSize(); i++) {
            if (RANDOM_GENERATOR.nextDouble() < mutationProbability)
                bias[i] += RANDOM_GENERATOR.nextGaussian() * standardDeviation;
            for (int j = 0; j < inputSize(); j++)
                if (RANDOM_GENERATOR.nextDouble() < mutationProbability)
                    weights[i][j] += RANDOM_GENERATOR.nextGaussian() * standardDeviation;
        }
    }

    private int outputSize() {
        return bias.length;
    }

    private int inputSize() {
        return weights[0].length;
    }
}