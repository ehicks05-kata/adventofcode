package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day06Lights
{
    public static void main(String[] args) throws IOException
    {
        getLights(1000);
    }

    public static void getLights(int gridSize) throws IOException
    {
        int[][] grid = initGrid(gridSize);
        List<Instruction> instructions = getInstructions();

        for (Instruction instruction : instructions)
            applyInstruction(grid, instruction);

        int totalBrightness = getTotalBrightness(grid);
        int highestBrightness = getHighestIndividualBrightness(grid);

        System.out.println(totalBrightness + " total brightness");
        System.out.println(highestBrightness + " highest individual brightness");
    }

    public static int[][] initGrid(int gridSize)
    {
        int[][] grid = new int[gridSize][gridSize];
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[0].length; col++)
                grid[row][col] = 0;
        return grid;
    }

    public static List<Instruction> getInstructions()
    {
        List<String> instructs = new ArrayList<>();
        try
        {
            instructs = Files.readAllLines(Paths.get("src/main/resources/year2015/advent06.txt"));
        }
        catch (IOException e)
        {

        }
        List<Instruction> instructions = new ArrayList<>();
        for (String instruct : instructs)
        {
            Instruction instruction = new Instruction();
            if (instruct.startsWith("turn on"))
            {
                instruction.command = "on";
                instruct = instruct.replace("turn on ", "");
            }
            if (instruct.startsWith("turn off"))
            {
                instruction.command = "off";
                instruct = instruct.replace("turn off ", "");
            }
            if (instruct.startsWith("toggle"))
            {
                instruction.command = "toggle";
                instruct = instruct.replace("toggle ", "");
            }

            String[] instructionChunks = instruct.split(" ");
            String from = instructionChunks[0];
            String to = instructionChunks[2];

            String[] fromChunks = from.split(",");
            String[] toChunks = to.split(",");

            instruction.fromX = Integer.parseInt(fromChunks[0]);
            instruction.fromY = Integer.parseInt(fromChunks[1]);

            instruction.toX = Integer.parseInt(toChunks[0]);
            instruction.toY = Integer.parseInt(toChunks[1]);

            instructions.add(instruction);
        }

        return instructions;
    }

    public static void applyInstruction(int[][] grid, Instruction instruction)
    {
        for (int row = instruction.fromX; row <= instruction.toX; row++)
            for (int col = instruction.fromY; col <= instruction.toY; col++)
            {
                if (instruction.command.equals("on"))
                    grid[row][col] = grid[row][col] + 1;
                if (instruction.command.equals("off"))
                {
                    if (grid[row][col] > 0)
                        grid[row][col] = grid[row][col] - 1;
                }
                if (instruction.command.equals("toggle"))
                    grid[row][col] = grid[row][col] + 2;
            }
    }

    private static int getTotalBrightness(int[][] grid)
    {
        int total = 0;
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[0].length; col++)
                total += grid[row][col];

        return total;
    }

    private static int getHighestIndividualBrightness(int[][] grid)
    {
        int highest = 0;
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[0].length; col++)
                if (grid[row][col] > highest)
                    highest = grid[row][col];

        return highest;
    }

    public static class Instruction
    {
        String command = "";
        int fromX = 0;
        int fromY = 0;
        int toX = 0;
        int toY = 0;
    }
}
