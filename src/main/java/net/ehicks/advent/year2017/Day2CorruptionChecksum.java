package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day2CorruptionChecksum
{
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/02.txt"));

        int sum = rows.stream()
                .map(Day2CorruptionChecksum::getDifferenceBetweenMaxAndMin)
                .reduce(Integer::sum)
                .orElseThrow(IOException::new);

        System.out.println("part 1: " + sum);

        sum = rows.stream()
                .map(Day2CorruptionChecksum::getResultOfCleanDivision)
                .reduce(Integer::sum)
                .orElseThrow(IOException::new);

        System.out.println("part 2: " + sum);
    }

    private static Integer getDifferenceBetweenMaxAndMin(String row) {
        List<String> cells = Arrays.asList(row.split("\t"));

        int min = cells.stream().map(Integer::valueOf).min(Integer::compareTo).orElse(0);
        int max = cells.stream().map(Integer::valueOf).max(Integer::compareTo).orElse(0);

        return max - min;
    }

    private static Integer getResultOfCleanDivision(String row) {
        List<String> cells = Arrays.asList(row.split("\t"));

        for (int i = 0; i < cells.size(); i++)
            for (int j = i + 1; j < cells.size(); j++)
            {
                int num1 = Integer.valueOf(cells.get(i));
                int num2 = Integer.valueOf(cells.get(j));

                if (num1 % num2 == 0)
                    return num1 / num2;
                if (num2 % num1 == 0)
                    return num2 / num1;
            }

        return 0;
    }
}
