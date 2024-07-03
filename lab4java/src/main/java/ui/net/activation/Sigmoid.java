package ui.net.activation;

public class Sigmoid implements Activation {
    @Override
    public double pass(double input) {
        return 1./(1+Math.pow(Math.E, -input));
    }
}
