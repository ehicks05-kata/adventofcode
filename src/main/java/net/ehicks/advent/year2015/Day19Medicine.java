package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19Medicine
{
    public static void main(String[] args) throws IOException
    {
        Map<String, List<String>> replacements = new HashMap<>();
        String medicineMolecule = parseFile("src/main/resources/year2015/advent19.txt", replacements);

        // part 1
//        runPart1(replacements, medicineMolecule);

        // part2
        Random random = new Random();

        int fewestSteps = Integer.MAX_VALUE;
        List<String> reactants = new ArrayList<>(replacements.keySet());

        for (int i = 0; i < 10000; i++)
        {
            String moleculeCopy = medicineMolecule;
            int iterationsAtThisLength = 0;

            boolean reachedGoal = false;
            boolean stuck = false;

            int steps = 0;
            while (!reachedGoal && !stuck)
            {
                int length = moleculeCopy.length();
                String reactant = reactants.get(random.nextInt(reactants.size()));

                List<String> products = replacements.get(reactant);
                for (String product : products)
                {
                    int productLength = product.length();
                    int indexOfProduct = moleculeCopy.indexOf(product);
                    while (indexOfProduct != -1)
                    {
                        moleculeCopy = moleculeCopy.substring(0, indexOfProduct) + reactant + moleculeCopy.substring(indexOfProduct + productLength);
                        indexOfProduct = moleculeCopy.indexOf(product);
                        steps++;
                    }
                }

                if (moleculeCopy.length() == length)
                    iterationsAtThisLength++;
                if (iterationsAtThisLength > 10000)
                    stuck = true;

                if (moleculeCopy.equals("e"))
                    reachedGoal = true;
            }

            if (reachedGoal && steps < fewestSteps)
            {
                fewestSteps = steps;
                System.out.println(fewestSteps);
            }
        }

        System.out.println(fewestSteps);
    }

    private static void runPart1(Map<String, List<String>> replacements, String medicineMolecule)
    {
        List<String> distinctMolecules = new ArrayList<>();

        List<String> moleculeParts = Arrays.asList(medicineMolecule.split("(?=[A-Z])"));
        for (int i = 0; i < moleculeParts.size(); i++)
        {
            String chemical = moleculeParts.get(i);
            if (replacements.containsKey(chemical))
            {
                for (String product : replacements.get(chemical))
                {
                    List<String> moleculePartsCopy = new ArrayList<>(moleculeParts);
                    moleculePartsCopy.remove(i);
                    moleculePartsCopy.add(i, product);
                    String newMolecule = listToString(moleculePartsCopy);
                    if (!distinctMolecules.contains(newMolecule))
                        distinctMolecules.add(newMolecule);
                }
            }
        }

        System.out.println(distinctMolecules.size());
    }

    private static String listToString(List<String> list)
    {
        String result = "";
        for (String element : list)
            result += element;
        return result;
    }

    private static String parseFile(String filename, Map<String, List<String>> replacements) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        for (String line : lines)
        {
            if (line.length() == 0)
                continue;

            if (line.length() > 20)
                return line;

            String[] parts = line.split(" ");
            String reactant = parts[0];
            String product = parts[2];
            if (!replacements.containsKey(reactant))
            {
                replacements.put(reactant, new ArrayList<>(Arrays.asList(product)));
            }
            else
                replacements.get(reactant).add(product);
        }

        return null;
    }
}


/*
  --- Day 19: Medicine for Rudolph ---

Rudolph the Red-Nosed Reindeer is sick! His nose isn't shining very brightly, and he needs medicine.

Red-Nosed Reindeer biology isn't similar to regular reindeer biology; Rudolph is going to need custom-made medicine.
Unfortunately, Red-Nosed Reindeer chemistry isn't similar to regular reindeer chemistry, either.

The North Pole is equipped with a Red-Nosed Reindeer nuclear fusion/fission plant, capable of constructing any Red-Nosed Reindeer
molecule you need. It works by starting with some input molecule and then doing a series of replacements, one per step, until it has the right molecule.

However, the machine has to be calibrated before it can be used. Calibration involves determining the number of molecules that can be generated in one step from a given starting point.

For example, imagine a simpler machine that supports only the following replacements:

H => HO
H => OH
O => HH
Given the replacements above and starting with HOH, the following molecules could be generated:

HOOH (via H => HO on the first H).
HOHO (via H => HO on the second H).
OHOH (via H => OH on the first H).
HOOH (via H => OH on the second H).
HHHH (via O => HH).
So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice) after one replacement from HOH.
Santa's favorite molecule, HOHOHO, can become 7 distinct molecules (over nine replacements: six from H, and three from O).

The machine replaces without regard for the surrounding characters. For example, given the string H2O, the transition H => OO would result in OO2O.

Your puzzle input describes all of the possible replacements and, at the bottom, the medicine molecule for which you need to calibrate the machine.
How many distinct molecules can be created after all the different ways you can do one replacement on the medicine molecule?
  */