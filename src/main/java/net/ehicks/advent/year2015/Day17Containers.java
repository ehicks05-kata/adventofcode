package net.ehicks.advent.year2015;

import java.util.*;

public class Day17Containers
{
    public static void main(String[] args)
    {
        List<Integer> availableContainers = Arrays.asList(50,44,11,49,42,46,18,32,26,40,21,7,18,43,10,47,36,24,22,40);
        Collections.sort(availableContainers);
        int liters = 150;
        int containerCombos = 0;

        List<List<Integer>> allSuccessfulCombos = new ArrayList<>();

        for (int a = -1; a < availableContainers.size(); a++)
        {
            System.out.println(a);
            for (int b = a; b < availableContainers.size(); b++)
                for (int c = b; c < availableContainers.size(); c++)
                    for (int d = c; d < availableContainers.size(); d++)
                        for (int e = d; e < availableContainers.size(); e++)
                            for (int f = e; f < availableContainers.size(); f++)
                                for (int g = f; g < availableContainers.size(); g++)
                                    for (int h = g; h < availableContainers.size(); h++)
                                    {
                                        List<Integer> chosenContainerIndexes = new ArrayList<>();
                                        chosenContainerIndexes.add(a);
                                        chosenContainerIndexes.add(b);
                                        chosenContainerIndexes.add(c);
                                        chosenContainerIndexes.add(d);
                                        chosenContainerIndexes.add(e);
                                        chosenContainerIndexes.add(f);
                                        chosenContainerIndexes.add(g);
                                        chosenContainerIndexes.add(h);

                                        if (!isUniqueContainerIndexes(availableContainers, chosenContainerIndexes))
                                            continue;

                                        if (matchesCapacity(availableContainers, chosenContainerIndexes, liters))
                                        {
                                            containerCombos++;
                                            allSuccessfulCombos.add(chosenContainerIndexes);
                                        }
                                    }
        }

        Map<Integer, Integer> containersUsedToSolutions = new HashMap<>();
        for (List<Integer> successfulCombo : allSuccessfulCombos)
        {
            int containersUsed = getNumberOfContainersUsed(successfulCombo);
            if (containersUsedToSolutions.get(containersUsed) == null)
                containersUsedToSolutions.put(containersUsed, 1);
            else
            {
                int previous = containersUsedToSolutions.get(containersUsed);
                containersUsedToSolutions.put(containersUsed, previous + 1);
            }
        }

        System.out.println("Number of ways :" + containerCombos);

        for (Integer key : containersUsedToSolutions.keySet())
            System.out.println(key + ":" + containersUsedToSolutions.get(key));
    }

    private static int getNumberOfContainersUsed(List<Integer> successfulCombo)
    {
        int containersUsed = 0;
        for (Integer containerIndex : successfulCombo)
        {
            if (containerIndex >= 0)
                containersUsed++;
        }
        return containersUsed;
    }

    private static boolean isUniqueContainerIndexes(List<Integer> availableContainers, List<Integer> chosenContainerIndexes)
    {
        for (int i = 0; i < availableContainers.size(); i++)
            if (Collections.frequency(chosenContainerIndexes, i) > 1)
                return false;
        return true;
    }
    private static boolean matchesCapacity(List<Integer> availableContainers, List<Integer> chosenContainerIndexes, int liters)
    {
        int capacity = 0;
        for (Integer chosenContainerIndex : chosenContainerIndexes)
            if (chosenContainerIndex > -1)
                capacity += availableContainers.get(chosenContainerIndex);

        return capacity == liters;
    }
}
