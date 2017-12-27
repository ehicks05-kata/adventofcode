package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day11HexEd {
    public static void main(String[] args) throws IOException
    {
        String steps = Files.readAllLines(Paths.get("src/main/resources/year2017/11.txt")).get(0);

        int maxDistance = 0;
        int x = 0, y = 0, z = 0;
        for (String step : steps.split(","))
        {
            if (step.equals("n"))  {x +=  0; y +=  1; z += -1;}
            if (step.equals("ne")) {x +=  1; y +=  0; z += -1;}
            if (step.equals("se")) {x +=  1; y += -1; z +=  0;}
            if (step.equals("s"))  {x +=  0; y += -1; z +=  1;}
            if (step.equals("sw")) {x += -1; y +=  0; z +=  1;}
            if (step.equals("nw")) {x += -1; y +=  1; z +=  0;}

            int distance = cubeDistanceFromOrigin(x, y, z);
            if (distance > maxDistance)
                maxDistance = distance;
        };

        int minStepsRequired = cubeDistanceFromOrigin(x, y, z);

        System.out.println("part 1: " + minStepsRequired);
        System.out.println("part 2: " + maxDistance);
    }

    public static int cubeDistanceFromOrigin(int x, int y, int z)
    {
        int bx = 0, by = 0, bz = 0;
        return (Math.abs(x - bx) + Math.abs(y - by) + Math.abs(z - bz)) / 2;
    }
}
