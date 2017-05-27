package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day23Registers
{
    public static void main(String[] args) throws IOException
    {
        List<Instruction> instructions = parseInput("src/main/resources/year2015/advent23.txt");

        int a = 1;
        int b = 0;

        int i = 0;
        while (i < instructions.size())
        {
            Instruction instruction = instructions.get(i);

            String command = instruction.command;
            String register = instruction.register;
            int offset = instruction.offset;

            if (command.equals("hlf"))
            {
                if (register.equals("a")) a /= 2;
                if (register.equals("b")) b /= 2;
            }
            if (command.equals("tpl"))
            {
                if (register.equals("a")) a *= 3;
                if (register.equals("b")) b *= 3;
            }
            if (command.equals("inc"))
            {
                if (register.equals("a")) a++;
                if (register.equals("b")) b++;
            }
            if (command.equals("jmp"))
            {
                i += offset;
                continue;
            }
            if (command.equals("jie"))
            {
                boolean isEven = false;
                if (register.equals("a") && a % 2 == 0) isEven = true;
                if (register.equals("b") && b % 2 == 0) isEven = true;

                if (isEven)
                {
                    i += offset;
                    continue;
                }
            }
            if (command.equals("jio"))
            {
                boolean isOne = false;
                if (register.equals("a") && a == 1) isOne = true;
                if (register.equals("b") && b == 1) isOne = true;

                if (isOne)
                {
                    i += offset;
                    continue;
                }
            }

            i++;
        }

        System.out.println(b);
    }

    private static List<Instruction> parseInput(String filename) throws IOException
    {
        List<Instruction> instructions = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(filename));
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            String command = parts[0];
            String register = "";
            int offset = 0;

            if (Arrays.asList("hlf", "tpl", "inc").contains(command))
            {
                register = parts[1];
            }
            if (Arrays.asList("jio", "jie").contains(command))
            {
                register = parts[1].replace(",", "");
                offset = Integer.parseInt(parts[2]);
            }
            if (command.equals("jmp"))
            {
                register = null;
                offset = Integer.parseInt(parts[1]);
            }
            instructions.add(new Instruction(command, register, offset));
        }

        return instructions;
    }

    private static class Instruction
    {
        String command = "";
        String register = "";
        int offset = 0;

        public Instruction(String command, String register, int offset)
        {
            this.command = command;
            this.register = register;
            this.offset = offset;
        }

        @Override
        public String toString()
        {
            return "Instruction{" +
                    "command='" + command + '\'' +
                    ", register='" + register + '\'' +
                    ", offset=" + offset +
                    '}';
        }
    }
}

/*
hlf r sets register r to half its current value, then continues with the next instruction.
tpl r sets register r to triple its current value, then continues with the next instruction.
inc r increments register r, adding 1 to it, then continues with the next instruction.
jmp offset is a jump; it continues with the instruction offset away relative to itself.
jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
*/