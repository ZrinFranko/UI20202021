package ui.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Dataset {

    private List<String> header;
    private List<Sample> data;

    private Dataset() {
    }

    public Dataset(String path) {
        try {
            List<String[]> fileLines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
            header = Arrays.asList(fileLines.remove(0));
            data = fileLines.stream().map(Sample::new).collect(Collectors.toList());
        } catch (IOException exception) {
            System.out.println("Error while reading data collection");
            System.exit(2);
        }
    }

    public static Dataset emptyDataset() {
        return new Dataset();
    }

    public int getFeatureSize() {
        return header.size() - 1;
    }

    public List<Sample> getDataList() {
        return data;
    }

}