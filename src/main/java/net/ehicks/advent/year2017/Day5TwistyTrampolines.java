package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day5TwistyTrampolines {
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/05.txt"));
        int stepstoExit = getStepstoExit(rows, false);

        System.out.println("part 1: " + stepstoExit);

        stepstoExit = getStepstoExit(rows, true);
        System.out.println("part 2: " + stepstoExit);
    }

    private static int getStepstoExit(List<String> rows, boolean part2) {
        List<Integer> instructions = rows.stream().map(Integer::valueOf).collect(Collectors.toList());
        int position = 0;
        int stepsToExit = 0;

        while (true)
        {
            if (position < 0 || position >= instructions.size())
                break;
            int jumpDelta = instructions.get(position);

            int newInstruction = jumpDelta + 1;
            if (part2 && jumpDelta >= 3)
                newInstruction = jumpDelta - 1;
            instructions.set(position, newInstruction);
            position += jumpDelta;
            stepsToExit++;
        }
        return stepsToExit;
    }
}
