package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day1InverseCaptcha {
    public static void main(String[] args) throws IOException
    {
        String digits = Files.readAllLines(Paths.get("src/main/resources/year2017/01.txt")).get(0);
        int sum = 0;
        for (int i = 0; i < digits.length(); i++)
        {
            int current = Integer.valueOf(digits.substring(i, i + 1));
            int next;
            if (i < digits.length() - 1)
                next = Integer.valueOf(digits.substring(i + 1, i + 2));
            else
                next = Integer.valueOf(digits.substring(0, 1));

            if (current == next)
                sum += current;
        }

        System.out.println("part 1: " + sum);

        sum = 0;
        for (int i = 0; i < digits.length(); i++)
        {
            int current = Integer.valueOf(digits.substring(i, i + 1));
            int next;

            // once we reach halfway, we will just be making the same comparisons again
            if (i == digits.length() / 2)
            {
                sum *= 2;
                break;
            }
            else
                next = Integer.valueOf(digits.substring(i + digits.length() / 2, i + digits.length() / 2 + 1));

            if (current == next)
                sum += current;
        }

        System.out.println("part 2: " + sum);
    }
}
