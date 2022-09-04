package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    public static List<String> readWords(String filePath) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.ready()) {
                String line = reader.readLine();
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}
