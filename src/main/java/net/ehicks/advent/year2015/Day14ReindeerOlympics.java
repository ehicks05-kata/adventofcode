package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day14ReindeerOlympics
{
    private static class Deer
    {
        public String name = "";
        public int flySpeed;
        public int flyTime;
        public int restTime;
        public int points = 0;

        public Deer(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "Deer{" +
                    "name='" + name + '\'' +
                    ", flySpeed=" + flySpeed +
                    ", flyTime=" + flyTime +
                    ", restTime=" + restTime +
                    ", points=" + points +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception
    {
        List<Deer> deers = parseDeerFile("src/main/resources/year2015/advent14.txt");
        int time = 2503;

        for (int i = 1; i <= time; i++)
        {
            List<Deer> winnersThisSecond = new ArrayList<>();
            int winningDistanceThisSecond = 0;
            for (Deer deer : deers)
            {
                int distance = getDistanceAfterTime(deer, i);
                if (distance == winningDistanceThisSecond)
                    winnersThisSecond.add(deer);
                if (distance > winningDistanceThisSecond)
                {
                    winningDistanceThisSecond = distance;
                    winnersThisSecond.clear();
                    winnersThisSecond.add(deer);
                }

            }

            for (Deer deer : winnersThisSecond)
                deer.points++;
        }

        int mostPoints = 0;
        for (Deer deer : deers)
        {
            if (deer.points > mostPoints)
                mostPoints = deer.points;
            System.out.println(deer);
        }

        System.out.println("Winning reindeer points: " + mostPoints);
    }

    private static int getDistanceAfterTime(Deer deer, int time)
    {
        int timeForOneCycle = deer.flyTime + deer.restTime;
        int completeCycles = time / timeForOneCycle;

        int distanceOfCompleteCycles = deer.flySpeed * deer.flyTime * completeCycles;
        int distance = distanceOfCompleteCycles;

        int remainingTime = time - (timeForOneCycle * completeCycles);

        int additionalFlyTime = remainingTime < deer.flyTime ? remainingTime : deer.flyTime;
        int additionalDistance = additionalFlyTime * deer.flySpeed;
        distance += additionalDistance;

        System.out.println(deer + " ---> " + distance);
        System.out.println("....Time Per Cycle: " + timeForOneCycle);
        System.out.println("....Complete Cycles: " + completeCycles + "  (" + time + "/" + timeForOneCycle + ")");
        System.out.println("....Distance of Complete Cycles: " + distanceOfCompleteCycles  + "  (" + deer.flySpeed + "*" + completeCycles + ")");
        System.out.println("....Remaining Time: " + remainingTime);
        System.out.println("....Additional Fly Time: " + additionalFlyTime);
        System.out.println("....Additional Distance: " + additionalDistance);
        return distance;
    }

    private static List<Deer> parseDeerFile(String filename) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Deer> deers = new ArrayList<>();
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            Deer deer = new Deer(parts[0]);
            deer.flySpeed = Integer.parseInt(parts[3]);
            deer.flyTime = Integer.parseInt(parts[6]);
            deer.restTime = Integer.parseInt(parts[13]);
            deers.add(deer);
        }

        return deers;
    }
}
