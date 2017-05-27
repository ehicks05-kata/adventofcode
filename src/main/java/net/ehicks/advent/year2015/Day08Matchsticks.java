package net.ehicks.advent.year2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day08Matchsticks
{
    public static void main(String[] args) throws Exception
    {
        int totalCodeLength = 0;
        int totalInMemoryLength = 0;

        List<String> instructions = Files.readAllLines(Paths.get("src/main/resources/year2015/advent08.txt"));
        for (String instruction : instructions)
        {
            totalCodeLength += instruction.length();

            String raw = instruction.substring(1, instruction.length() - 1);

            outer:
            while (raw.contains("\\\\") || raw.contains("\\\"") || raw.contains("\\x"))
            {
                if (raw.contains("\\\\"))
                {
                    int index = raw.indexOf("\\\\");
                    raw = raw.substring(0, index) + "\\" + raw.substring(index + 2);
                }
                if (raw.contains("\\\""))
                {
                    int index = raw.indexOf("\\\"");
                    raw = raw.substring(0, index) + "\"" + raw.substring(index + 2);
                }
                int fromIndex = 0;
                int count = 0;
                while (raw.contains("\\x"))
                {
                    int index = raw.indexOf("\\x", fromIndex);
                    if (index + 4 <= raw.length())
                    {
                        String code = raw.substring(index + 2, index + 4);
                        Integer myInt = null;
                        try
                        {
                            myInt = Integer.parseInt(code, 16);
                        }
                        catch (Exception e)
                        {

                        }
                        if (myInt != null)
                        {
                            String newChar = Character.toString((char) myInt.intValue());
                            raw = raw.substring(0, index) + newChar + raw.substring(index + 4);
                        }
                        else
                        {
                            fromIndex = index + 1;
                        }
                    }

                    if (count > 5)
                        break outer;
                    count++;
                }
            }

            System.out.println(/*instruction.raw + " -> " +*/ raw + " (" + raw.length() + ")");
            totalInMemoryLength += raw.length();
        }

        System.out.println("code length: " + totalCodeLength);
        System.out.println("in memory length: " + totalInMemoryLength);
        System.out.println("code length - in memory length " + (totalCodeLength - totalInMemoryLength));
    }
}
