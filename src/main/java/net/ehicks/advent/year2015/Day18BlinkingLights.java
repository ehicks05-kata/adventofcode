package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day18BlinkingLights
{
    public static void main(String[] args) throws IOException
    {
        boolean[][] grid = initGrid("src/main/resources/year2015/advent18.txt");

        for (int i = 0; i < 100; i++)
        {
            grid = animateGrid(grid);
        }

        System.out.println(getNumberOfLightsOn(grid));
    }

    public static boolean[][] animateGrid(boolean[][] grid) throws IOException
    {
        boolean[][] result = new boolean[grid.length][grid.length];
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid.length; col++)
                result[row][col] = grid[row][col];

        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid.length; col++)
            {
                int neighborsOn = getNeighborsOn(grid, row, col);

                if (grid[row][col] && !(neighborsOn == 2 || neighborsOn == 3))
                    result[row][col] = false;
                if (!grid[row][col] && neighborsOn == 3)
                    result[row][col] = true;
            }

        result = forceCornersOn(result);

        return result;
    }

    private static int getNeighborsOn(boolean[][] grid, int row, int col)
    {
        int lightsOn = 0;

        if (row > 0)
        {
            if (col > 0)
                if (grid[row - 1][col - 1]) lightsOn++;

            if (grid[row - 1][col]) lightsOn++;

            if (col < grid.length - 1)
                if (grid[row - 1][col + 1]) lightsOn++;
        }

        if (col < grid.length - 1)
            if (grid[row][col + 1]) lightsOn++;

        if (row < grid.length - 1)
        {
            if (col > 0)
                if (grid[row + 1][col - 1]) lightsOn++;

            if (grid[row + 1][col]) lightsOn++;

            if (col < grid.length - 1)
                if (grid[row + 1][col + 1]) lightsOn++;
        }

        if (col > 0)
            if (grid[row][col - 1]) lightsOn++;

        return lightsOn;
    }

    public static boolean[][] initGrid(String filename) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(filename));

        boolean[][] grid = new boolean[lines.size()][lines.size()];

        for (int row = 0; row < lines.size(); row++)
        {
            String line = lines.get(row);
            String[] parts = line.split("");

            for (int col = 0; col < parts.length; col++)
            {
                if (parts[col].equals("."))
                    grid[row][col] = false;
                if (parts[col].equals("#"))
                    grid[row][col] = true;
            }
        }

        grid = forceCornersOn(grid);

        return grid;
    }

    private static boolean[][] forceCornersOn(boolean[][] grid)
    {
        int maxIndex = grid.length - 1;
        grid[0][0] = true;
        grid[maxIndex][0] = true;
        grid[0][maxIndex] = true;
        grid[maxIndex][maxIndex] = true;
        return grid;
    }

    private static int getNumberOfLightsOn(boolean[][] grid)
    {
        int total = 0;
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid.length; col++)
                if (grid[row][col])
                    total++;
        return total;
    }
}
