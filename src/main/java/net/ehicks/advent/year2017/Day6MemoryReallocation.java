package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6MemoryReallocation {
    public static void main(String[] args) throws IOException
    {
        String row = Files.readAllLines(Paths.get("src/main/resources/year2017/06.txt")).get(0);
        List<Integer> memoryBanks = Arrays.stream(row.split("\t")).map(Integer::valueOf).collect(Collectors.toList());
        int cyclesBeforeLoopDetected = getCyclesBeforeLoopDetected(memoryBanks);

        System.out.println("part 1: " + cyclesBeforeLoopDetected);

        int infiniteCycleSize = getInfiniteCycleSize(memoryBanks);
        System.out.println("part 2: " + infiniteCycleSize);
    }

    public static int getCyclesBeforeLoopDetected(List<Integer> memoryBanks)
    {
        int cycles = 0;
        List<List<Integer>> history = new ArrayList<>(Arrays.asList(memoryBanks));

        List<Integer> previousMemoryBanks = new ArrayList<>(memoryBanks);
        while (true) {
            List<Integer> result = performCycle(previousMemoryBanks);
            cycles++;
            if (history.contains(result))
                return cycles;
            else
                history.add(result);

            previousMemoryBanks = result;
        }
    }

    public static int getInfiniteCycleSize(List<Integer> memoryBanks)
    {
        boolean infiniteCycleDetected = false;
        int cyclesInInfiniteCycle = 0;
        List<List<Integer>> history = new ArrayList<>(Arrays.asList(memoryBanks));

        List<Integer> previousMemoryBanks = new ArrayList<>(memoryBanks);
        while (true) {
            List<Integer> result = performCycle(previousMemoryBanks);
            cyclesInInfiniteCycle++;
            if (history.contains(result))
            {
                if (infiniteCycleDetected)
                    return cyclesInInfiniteCycle;
                else
                {
                    infiniteCycleDetected = true;
                    cyclesInInfiniteCycle = 0;
                    history = new ArrayList<>(Arrays.asList(result));
                }
            }
            else
                history.add(result);

            previousMemoryBanks = result;
        }
    }

    public static List<Integer> performCycle(List<Integer> initialMemoryBanks)
    {
        List<Integer> banks = new ArrayList<>(initialMemoryBanks);
        int largestBankIndex = getLargestBankIndex(banks);
        int amountToDistribute = banks.get(largestBankIndex);
        banks.set(largestBankIndex, 0);

        int index = (largestBankIndex + 1) % banks.size();
        while (amountToDistribute > 0)
        {
            banks.set(index, banks.get(index) + 1);
            amountToDistribute--;
            index = (index + 1) % banks.size();
        }

        return banks;
    }

    private static int getLargestBankIndex(List<Integer> banks) {
        int largestBankIndex = 0;
        int largestBank = 0;
        for (int i = 0; i < banks.size(); i++)
            if (banks.get(i) > largestBank)
            {
                largestBankIndex = i;
                largestBank = banks.get(i);
            }
        return largestBankIndex;
    }
}
