package net.ehicks.advent.year2015;

import java.io.IOException;
import java.util.*;

public class Day24QuantumEntanglement
{
    static final List<Integer> allPackages = Arrays.asList(1,3,5,11,13,17,19,23,29,31,37,41,43,47,53,59,67,71,73,79,83,89,97,101,103,107,109,113);
    public static void main(String[] args) throws IOException
    {

        int totalWeight = allPackages.stream().reduce((integer, integer2) -> integer + integer2).get();
        System.out.println(totalWeight);
        int weightPerGroup = totalWeight / 4;
        System.out.println(weightPerGroup);

        long lowestQE = Long.MAX_VALUE;

        // find an arrangement where the fewest packages are in group 1
        List<List<Integer>> possibleGroup1Configurations = new ArrayList<>();
        for (int i = 0; i < allPackages.size(); i++)
            for (int j = 0; j < allPackages.size(); j++)
                for (int k = 0; k < allPackages.size(); k++)
                    for (int l = 0; l < allPackages.size(); l++)
                        for (int m = 0; m < allPackages.size(); m++)
                        {
                            List<Integer> chosenPackageIndexes = new ArrayList<>();
                            chosenPackageIndexes.add(i);
                            chosenPackageIndexes.add(j);
                            chosenPackageIndexes.add(k);
                            chosenPackageIndexes.add(l);
                            chosenPackageIndexes.add(m);

                            if (!isUniquePackageIndexes(chosenPackageIndexes))
                                continue;

                            int sum = 0;
                            for (Integer chosenPackageIndex : chosenPackageIndexes)
                                sum += allPackages.get(chosenPackageIndex);

                            if (sum == weightPerGroup)
                            {
                                List<Integer> chosenPackages = new ArrayList<>();
                                chosenPackages.add(allPackages.get(i));
                                chosenPackages.add(allPackages.get(j));
                                chosenPackages.add(allPackages.get(k));
                                chosenPackages.add(allPackages.get(l));
                                chosenPackages.add(allPackages.get(m));
                                long qe = getQE(chosenPackages);
                                if (qe < lowestQE)
                                {
                                    lowestQE = qe;
                                    System.out.print("QE: " + qe + "  ");
                                    printList(chosenPackages);
                                }
                            }

                        }

//        for (List<Integer> possibleConfig : possibleGroup1Configurations)
//        {
//            printList(possibleConfig);
//        }

    }

    private static boolean isUniquePackageIndexes(List<Integer> chosenPackageIndexes)
    {
        for (int i = 0; i < allPackages.size(); i++)
            if (Collections.frequency(chosenPackageIndexes, i) > 1)
                return false;
        return true;
    }

    private static void printList(List<Integer> config)
    {
        config.stream().forEach((integer) -> System.out.print(integer + ","));
        System.out.println();
    }

    private static long getQE(List<Integer> packages)
    {
        long qe = 0;
        for (Integer apackage : packages)
        {
            if (qe == 0)
                qe = apackage;
            else
                qe *= apackage;
        }
        return qe;
    }
}