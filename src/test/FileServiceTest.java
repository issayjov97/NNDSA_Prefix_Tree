package test;

import main.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void readWords() {
        List<String> words = FileService.readWords("src/words.txt");
        assertFalse(words.isEmpty());
    }
}