package net.ehicks.advent.year2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day01
{
    public static void main(String[] args) throws IOException
    {
        solvePart1();
        solvePart2();
    }

    private static void solvePart1() throws IOException
    {
        int sum = Files.readAllLines(Paths.get("src/main/resources/year2019/01.txt"))
                .stream()
                .mapToInt(Day01::massToFuel)
                .sum();

        System.out.println("part 1: " + sum);
    }

    private static void solvePart2() throws IOException
    {
        int sum = Files.readAllLines(Paths.get("src/main/resources/year2019/01.txt"))
                .stream()
                .mapToInt(Day01::massToFuel2)
                .sum();

        System.out.println("part 2: " + sum);
    }

    private static int massToFuel(String mass)
    {
        try
        {
            return ((Integer.parseInt(mass) / 3) - 2);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    private static int massToFuel2(String mass)
    {
        try
        {
            int sum = ((Integer.parseInt(mass) / 3) - 2);

            int totalSum = 0;

            while (sum > 0)
            {
                totalSum += sum;
                sum = (sum / 3) - 2;
            }

            return totalSum;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return 0;
    }
}