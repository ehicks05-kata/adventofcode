package net.ehicks.advent.year2017;

public class Day3SpiralMemory {
    public static void main(String[] args)
    {
        int input = 265149;

        // Configuration
        int length = (int) Math.ceil(Math.sqrt((double) input));
        if (length % 2 == 0) length++;

        int[][] spiral = buildSpiral(length);
        printSpiral(spiral);
        int distance = getDistance(spiral, input);

        System.out.println("part 1: " + distance);

        int firstSquareLargerThanPuzzleInput = buildSpiralPart2(length, input);
        System.out.println("part 2: " + firstSquareLargerThanPuzzleInput);
    }

    private static int getDistance(int[][] spiral, int target)
    {
        for (int row = 0; row < spiral.length; row++)
            for (int col = 0; col < spiral.length; col++)
                if (spiral[row][col] == target)
                    return Math.abs(row - spiral.length / 2) + Math.abs(col - spiral.length / 2);
        return 0;
    }

    private static void printSpiral(int[][] spiral)
    {
        for (int[] row : spiral) {
            for (int cell : row)
                System.out.print(padToLength(String.valueOf(cell)) + " ");
            System.out.println();
        }
    }

    private static String padToLength(String input)
    {
        while (input.length() < 6)
            input = " " + input;
        return input;
    }

    // this method was pilfered from my solution to project euler #28
    private static int[][] buildSpiral(int length)
    {
        int[][] spiral = new int[length][length];

        int midway = length / 2;
        int counter = 1;
        spiral[midway][midway] = counter++;

        String direction = "east";
        int row = midway;
        int column = midway + 1;

        while (column >= 0 && column < spiral.length && row >= 0 && row < spiral.length)
        {
            spiral[row][column] = counter++;

            direction = updateDirection(spiral, direction, row, column);

            if (direction.equals("east")) column++;
            else if (direction.equals("south")) row++;
            else if (direction.equals("west")) column--;
            else if (direction.equals("north")) row--;
        }

        return spiral;
    }

    // this method was pilfered from my solution to project euler #28
    private static int buildSpiralPart2(int length, int input)
    {
        int[][] spiral = new int[length][length];

        int midway = length / 2;
        spiral[midway][midway] = 1;

        String direction = "east";
        int row = midway;
        int column = midway + 1;

        while (column >= 0 && column < spiral.length && row >= 0 && row < spiral.length)
        {
            int sum = 0;
            if (row > 0 && column > 0) sum += spiral[row - 1][column - 1];
            if (row > 0) sum += spiral[row - 1][column];
            if (row > 0 && column < spiral.length - 1) sum += spiral[row - 1][column + 1];
            if (column > 0) sum += spiral[row][column - 1];
            if (column < spiral.length - 1) sum += spiral[row][column + 1];
            if (row < spiral.length - 1 && column > 0) sum += spiral[row + 1][column - 1];
            if (row < spiral.length - 1) sum += spiral[row + 1][column];
            if (row < spiral.length - 1 && column < spiral.length - 1) sum += spiral[row + 1][column + 1];

            spiral[row][column] = sum;

            if (sum > input)
                return sum;

            direction = updateDirection(spiral, direction, row, column);

            if (direction.equals("east")) column++;
            else if (direction.equals("south")) row++;
            else if (direction.equals("west")) column--;
            else if (direction.equals("north")) row--;
        }

        return 0;
    }

    private static String updateDirection(int[][] spiral, String direction, int row, int column) {
        switch (direction) {
            case "east":
                // can we turn left?
                if (spiral[row - 1][column] == 0)
                    direction = "north";
                break;
            case "south":
                // can we turn left?
                if (spiral[row][column + 1] == 0)
                    direction = "east";
                break;
            case "west":
                // can we turn left?
                if (spiral[row + 1][column] == 0)
                    direction = "south";
                break;
            case "north":
                // can we turn left?
                if (spiral[row][column - 1] == 0)
                    direction = "west";
                break;
        }
        return direction;
    }
}
