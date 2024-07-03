package ui;

import ui.net.FullyConnectedConfig;

public class Config {
    private final FullyConnectedConfig fullyConnectedConfig;
    private final RunConfig runConfig;

    public Config(FullyConnectedConfig fullyConnectedConfig, RunConfig runConfig) {
        this.fullyConnectedConfig = fullyConnectedConfig;
        this.runConfig = runConfig;
    }

    public FullyConnectedConfig getFullyConnectedConfig() {
        return fullyConnectedConfig;
    }

    public RunConfig getRunConfig() {
        return runConfig;
    }
}
