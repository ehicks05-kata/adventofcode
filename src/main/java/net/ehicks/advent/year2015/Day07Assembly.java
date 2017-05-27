package net.ehicks.advent.year2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day07Assembly
{
    public static void main(String[] args) throws Exception
    {
        assemble();
    }

    public static void assemble() throws Exception
    {
        List<Instruction> instructions = getInstructions("src/main/resources/year2015/advent07.txt");
        Map<String, Integer> wireValues = new HashMap<>();

        /* Part 2:
           Now, take the signal you got on wire a, override wire b to that signal,
           and reset the other wires (including wire a).
           What new signal is ultimately provided to wire a? */
        for (Instruction instruction : instructions)
        {
            if (instruction.output.equals("b"))
                instruction.input1 = "16076";
        }


        while (instructions.size() > 0)
        {
            for (Iterator<Instruction> i = instructions.iterator(); i.hasNext();)
            {
                Instruction instruction = i.next();
                String in1 = instruction.input1;
                String in2 = instruction.input2;

                if (wireValues.containsKey(in1))
                {
                    instruction.input1 = String.valueOf(wireValues.get(in1));
                    instruction.input1Number = true;
                }
                if (wireValues.containsKey(in2))
                {
                    instruction.input2 = String.valueOf(wireValues.get(in2));
                    instruction.input2Number = true;
                }

                boolean allInputsKnown = instruction.input1Number && (in2.length() == 0 || instruction.input2Number);
                if (allInputsKnown)
                {
                    applyInstruction(instruction, wireValues);
                    i.remove();
                }
            }
        }

        System.out.println("Signal on wire 'a': " + wireValues.get("a"));
    }

    public static void applyInstruction(Instruction instruction, Map<String, Integer> wireValues)
    {
        String gate = instruction.gate;
        if (gate.equals("AND") || gate.equals("OR"))
        {
            String in1 = getBinaryString(instruction.input1);
            String in2 = getBinaryString(instruction.input2);
            wireValues.put(instruction.output, andOr(in1, in2, gate));
        }
        if (gate.equals("NOT"))
        {
            String in1 = getBinaryString(instruction.input1);
            wireValues.put(instruction.output, not(in1));
        }
        if (gate.equals("LSHIFT") || gate.equals("RSHIFT"))
        {
            String in1 = getBinaryString(instruction.input1);
            Integer in2 = Integer.parseInt(instruction.input2);
            wireValues.put(instruction.output, shift(in1, in2, gate));
        }
        if (gate.equals(""))
        {
            wireValues.put(instruction.output, Integer.parseInt(instruction.input1));
        }
    }

    private static String getBinaryString(String decimalString)
    {
        return Integer.toString(Integer.parseInt(decimalString), 2);
    }

    private static Integer shift(String binary1, Integer amount, String direction)
    {
        String result = binary1;

        for (int i = 0; i < amount; i++)
        {
            if (direction.equals("LSHIFT"))
                result = result + "0";

            if (direction.equals("RSHIFT"))
                result = "0" + result.substring(0, result.length() - 1);
        }

        return Integer.parseInt(result, 2);
    }

    private static Integer andOr(String binary1, String binary2, String gate)
    {
        String result = "";

        while (binary1.length() != binary2.length())
        {
            if (binary1.length() < binary2.length()) binary1 = "0" + binary1;
            if (binary1.length() > binary2.length()) binary2 = "0" + binary2;
        }

        for (int i = 0; i < binary1.length(); i++)
        {
            String digit1 = binary1.substring(i, i + 1);
            String digit2 = binary2.substring(i, i + 1);

            if (gate.equals("AND"))
            {
                if (digit1.equals("1") && digit2.equals("1"))
                    result += "1";
                else
                    result += "0";
            }
            if (gate.equals("OR"))
            {
                if (digit1.equals("1") || digit2.equals("1"))
                    result += "1";
                else
                    result += "0";
            }
        }

        return Integer.parseInt(result, 2);
    }

    private static Integer not(String binary1)
    {
        String result = "";

        while (binary1.length() < 16)
            binary1 = "0" + binary1;

        for (int i = 0; i < binary1.length(); i++)
        {
            String digit1 = binary1.substring(i, i + 1);
            if (digit1.equals("1"))
                result += "0";
            else
                result += "1";
        }

        return Integer.parseInt(result, 2);
    }

    public static List<Instruction> getInstructions(String path) throws Exception
    {
        List<String> instructs = Files.readAllLines(Paths.get(path));
        List<String> gates = Arrays.asList("AND", "OR", "NOT", "LSHIFT", "RSHIFT");

        List<Instruction> instructions = new ArrayList<>();
        for (String instruct : instructs)
        {
            Instruction instruction = new Instruction();
            instruction.raw = instruct;

            String[] instructionChunks = instruct.split(" -> ");
            String inputs = instructionChunks[0];
            String output = instructionChunks[1];

            for (String inputChunk : inputs.split(" "))
            {
                if (gates.contains(inputChunk))
                    instruction.gate = inputChunk;
                else
                {
                    if (instruction.input1.length() == 0)
                    {
                        instruction.input1 = inputChunk;
                        if (tryParseInt(inputChunk) != null)
                            instruction.input1Number = true;
                    }
                    else
                    {
                        instruction.input2 = inputChunk;
                        if (tryParseInt(inputChunk) != null)
                            instruction.input2Number = true;
                    }
                }
            }

            instruction.output = output;

            instructions.add(instruction);
        }

        return instructions;
    }

    private static Integer tryParseInt(String input)
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (Exception e)
        {
//            System.out.println(e.getMessage());
        }
        return null;
    }

    public static class Instruction
    {
        String raw = "";
        String gate = "";
        String input1 = "";
        boolean input1Number = false;
        String input2 = "";
        boolean input2Number = false;
        String output = "";
    }
}
