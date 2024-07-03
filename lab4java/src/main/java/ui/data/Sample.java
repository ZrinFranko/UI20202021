package ui.data;

public class Sample {
    private final double[] values;
    private final double label;

    public Sample(String[] featuresAndLabel) {
        values = new double[featuresAndLabel.length - 1];
        for (int i = 0; i < featuresAndLabel.length - 1; i++)
            values[i] = Double.parseDouble(featuresAndLabel[i]);
        label = Double.parseDouble(featuresAndLabel[featuresAndLabel.length - 1]);
    }

    public double[] getValues() {
        return values;
    }

    public double getLabel() {
        return label;
    }

}