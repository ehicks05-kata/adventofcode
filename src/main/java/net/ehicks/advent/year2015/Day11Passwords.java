package net.ehicks.advent.year2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11Passwords
{
    public static void main(String[] args) throws Exception
    {
        String input = "vzbxkghb";

        input = getNextPassword(input);
        System.out.println(input);
        input = getNextPassword(input);
        System.out.println(input);
    }

    public static String getNextPassword(String input)
    {
        String output = input;

        boolean valid = false;
        while (!valid)
        {
            output = CommonCombinatorics.getNextString(output);
            valid = isValid(output);
        }

        return output;
    }

    private static boolean isValid(String input)
    {
        List<String> password = Arrays.asList(input.split(""));

        // contains three ascending letters in a row (like 'abc')
        boolean foundAscension = false;
        for (int i = 0; i < password.size() - 2; i++)
        {
            char first = password.get(i).charAt(0);
            char second = password.get(i + 1).charAt(0);
            char third = password.get(i + 2).charAt(0);

            boolean isAscending = first == second - 1 && second == third - 1;
            if (isAscending)
                foundAscension = true;
        }
        if (!foundAscension)
            return false;

        // must not contain 'i', 'l', 'o'
        for (String letter : password)
            if (Arrays.asList("i", "l", "o").contains(letter))
                return false;

        // must contain 2 non-overlapping pairs of the same letter (like 'aa' and 'bb')
        List<String> pairsFound = new ArrayList<>();
        for (int i = 0; i < password.size() - 1; i++)
        {
            String first = password.get(i);
            String second = password.get(i + 1);

            boolean isPair = first.equals(second);
            if (isPair && !pairsFound.contains(first))
            {
                pairsFound.add(first);
                i++;
            }
        }
        if (pairsFound.size() < 2)
            return false;

        return true;
    }
}
