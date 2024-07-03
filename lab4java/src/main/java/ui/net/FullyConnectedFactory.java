package ui.net;

import ui.net.activation.Activation;
import ui.net.activation.Identitiy;
import ui.net.activation.Sigmoid;

public class FullyConnectedFactory {
    private static final Activation SIGMOID = new Sigmoid();
    private static final Activation IDENTITY = new Identitiy();

    private final FullyConnectedConfig config;

    public FullyConnectedFactory(FullyConnectedConfig config) {
        this.config = config;
    }

    public FullyConnectedNetwork createFullyConnectedNetwork() {
        FullyConnectedLayer[] network = new FullyConnectedLayer[config.getDimensions().size() + 1];
        network[0] = new FullyConnectedLayer(config.getInput(), config.getDimensions().get(0), SIGMOID);
        int i;
        for (i = 1; i < config.getDimensions().size(); i++)
            network[i] = new FullyConnectedLayer(config.getDimensions().get(i - 1), config.getDimensions().get(i), SIGMOID);
        network[i] = new FullyConnectedLayer(config.getDimensions().get(i - 1), 1, IDENTITY);
        return new FullyConnectedNetwork(network);
    }

}
