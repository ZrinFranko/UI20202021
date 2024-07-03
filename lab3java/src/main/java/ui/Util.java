package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Util {


    public static List<String> readFile(String pathToFile) {

        Path document = Path.of(pathToFile);
        if (document.toFile().canRead()) {
            try {
                return Files.readAllLines(document);
            } catch (IOException e) {
                System.out.println("Given file is not readable");
            }
        } else {
            System.out.println("Given file is not readable");
            return null;
        }

        return List.of();
    }
}
