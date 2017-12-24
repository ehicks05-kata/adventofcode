package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day4HighEntropyPassphrases {
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/04.txt"));

        int validPassphrases = rows.stream().mapToInt(row -> {
            List<String> words = Arrays.asList(row.split(" "));
            Set<String> uniqueWords = new HashSet<>(words);
            if (words.size() == uniqueWords.size())
                return 1;
            else
                return 0;
        }).sum();

        System.out.println("part 1: " + validPassphrases);

        validPassphrases = rows.stream().mapToInt(row -> {
            List<String> words = Arrays.asList(row.split(" "));
            Set<String> uniqueWords = new HashSet<>();
            words.forEach(word -> {
                String[] letters = word.split("");
                Arrays.sort(letters);
                String sortedWord = Arrays.toString(letters);
                uniqueWords.add(sortedWord);
            });

            if (words.size() == uniqueWords.size())
                return 1;
            else
                return 0;
        }).sum();

        System.out.println("part 2: " + validPassphrases);
    }
}
