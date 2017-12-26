package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8Registers {
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/08.txt"));

        Map<String, Integer> registers = new HashMap<>();
        int highestValueEverHeld = 0;

        for (String row : rows)
        {
            String[] chunks = row.split(" ");
            String register = chunks[0];
            String command = chunks[1];
            String commandValue = chunks[2];
            String condRegister = chunks[4];
            String condConditional = chunks[5];
            String condConditionalValue = chunks[6];

            registers.putIfAbsent(register, 0);
            registers.putIfAbsent(condRegister, 0);

            if (evalCond(registers.get(condRegister), condConditional, Integer.valueOf(condConditionalValue)))
            {
                int value = registers.get(register);
                if (command.equals("inc"))
                    value += Integer.parseInt(commandValue);
                else
                    value -= Integer.parseInt(commandValue);
                registers.put(register, value);

                if (value > highestValueEverHeld)
                    highestValueEverHeld = value;
            }
        };

        int largestRegisterValue = registers.values().stream().max(Integer::compareTo).orElse(0);
        System.out.println("part 1: " + largestRegisterValue);

        System.out.println("part 2: " + highestValueEverHeld);
    }

    public static boolean evalCond(int firstValue, String conditional, int secondValue)
    {
        if (conditional.equals(">"))
            return firstValue > secondValue;
        if (conditional.equals(">="))
            return firstValue >= secondValue;
        if (conditional.equals("<"))
            return firstValue < secondValue;
        if (conditional.equals("<="))
            return firstValue <= secondValue;
        if (conditional.equals("=="))
            return firstValue == secondValue;
        if (conditional.equals("!="))
            return firstValue != secondValue;
        return false;
    }
}
