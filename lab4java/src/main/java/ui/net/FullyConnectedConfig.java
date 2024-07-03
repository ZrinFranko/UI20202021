package ui.net;

import java.util.List;

public class FullyConnectedConfig {
    private final int input;
    private final List<Integer> dimensions;
    private final double mutation;
    private final double stdDev;

    public FullyConnectedConfig(int input, List<Integer> dimensions, double mutation, double stdDev) {
        this.dimensions = dimensions;
        this.input = input;
        this.mutation = mutation;
        this.stdDev = stdDev;
    }

    public List<Integer> getDimensions() {
        return dimensions;
    }

    public int getInput() {
        return input;
    }

    public double getMutation() {
        return mutation;
    }

    public double getStdDev() {
        return stdDev;
    }
}
