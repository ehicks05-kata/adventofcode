package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day16Sue
{
    private static class Sue
    {
        public int number;

        public int children = -1;
        public int cats = -1;
        public int samoyeds = -1;
        public int pomeranians = -1;
        public int akitas = -1;
        public int vizslas = -1;
        public int goldfish = -1;
        public int trees = -1;
        public int cars = -1;
        public int perfumes = -1;

        public Sue(int number)
        {
            this.number = number;
        }
    }

    public static void main(String[] args) throws Exception
    {
        List<Sue> sues = parseInputFile("src/main/resources/year2015/advent16.txt");

        Sue masterSue = new Sue(-1);
        masterSue.children    = 3;
        masterSue.cats        = 7;
        masterSue.samoyeds    = 2;
        masterSue.pomeranians = 3;
        masterSue.akitas      = 0;
        masterSue.vizslas     = 0;
        masterSue.goldfish    = 5;
        masterSue.trees       = 3;
        masterSue.cars        = 2;
        masterSue.perfumes    = 1;

        int correctSue = 0;
        for (Sue sue : sues)
        {
            boolean isSue = isSue(masterSue, sue);
            if (isSue)
                correctSue = sue.number;
        }

        System.out.println("Correct Sue: " + correctSue);
    }

    private static boolean isSue(Sue masterSue, Sue sue)
    {
        if (sue.children    != -1) if (sue.children    != masterSue.children) return false;
        if (sue.cats        != -1) if (sue.cats        <= masterSue.cats) return false;
        if (sue.samoyeds    != -1) if (sue.samoyeds    != masterSue.samoyeds) return false;
        if (sue.pomeranians != -1) if (sue.pomeranians >= masterSue.pomeranians) return false;
        if (sue.akitas      != -1) if (sue.akitas      != masterSue.akitas) return false;
        if (sue.vizslas     != -1) if (sue.vizslas     != masterSue.vizslas) return false;
        if (sue.goldfish    != -1) if (sue.goldfish    >= masterSue.goldfish) return false;
        if (sue.trees       != -1) if (sue.trees       <= masterSue.trees) return false;
        if (sue.cars        != -1) if (sue.cars        != masterSue.cars) return false;
        if (sue.perfumes    != -1) if (sue.perfumes    != masterSue.perfumes) return false;

        return true;
    }

    private static List<Sue> parseInputFile(String filename) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Sue> sues = new ArrayList<>();
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            Sue sue = new Sue(Integer.parseInt(parts[1].replace(":", "")));

            String prop1Name = parts[2].replace(":", "");
            int prop1Value = Integer.parseInt(parts[3].replace(",", ""));

            String prop2Name = parts[4].replace(":", "");
            int prop2Value = Integer.parseInt(parts[5].replace(",", ""));

            String prop3Name = parts[6].replace(":", "");
            int prop3Value = Integer.parseInt(parts[7].replace(",", ""));

            applyProperty(sue, prop1Name, prop1Value);
            applyProperty(sue, prop2Name, prop2Value);
            applyProperty(sue, prop3Name, prop3Value);
            sues.add(sue);
        }

        return sues;
    }

    private static void applyProperty(Sue sue, String propertyName, int propertyValue)
    {
        if (propertyName.equals("children")) sue.children       = propertyValue;
        if (propertyName.equals("cats")) sue.cats               = propertyValue;
        if (propertyName.equals("samoyeds")) sue.samoyeds       = propertyValue;
        if (propertyName.equals("pomeranians")) sue.pomeranians = propertyValue;
        if (propertyName.equals("akitas")) sue.akitas           = propertyValue;
        if (propertyName.equals("vizslas")) sue.vizslas         = propertyValue;
        if (propertyName.equals("goldfish")) sue.goldfish       = propertyValue;
        if (propertyName.equals("trees")) sue.trees             = propertyValue;
        if (propertyName.equals("cars")) sue.cars               = propertyValue;
        if (propertyName.equals("perfumes")) sue.perfumes       = propertyValue;
    }
}
