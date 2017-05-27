package net.ehicks.advent.year2015;

public class Day10LookAndSay
{
    public static void main(String[] args) throws Exception
    {
        String input = "1113222113";

        for (int i = 0; i < 50; i++)
        {
            input = doLookAndSay(input);
            System.out.println(i);
        }

        System.out.println(input.length());
    }

    public static String doLookAndSay(String input)
    {
        StringBuilder output = new StringBuilder();

        String lastDigit = "";
        int reps = 1;

        String[] inputDigits = input.split("");

        for (int i = 0; i < input.length(); i++)
        {
            String digit = inputDigits[i];
            if (lastDigit.length() == 0)
            {
                lastDigit = digit;
                continue;
            }

            if (digit.equals(lastDigit))
                reps++;

            if (!digit.equals(lastDigit))
            {
                output.append(String.valueOf(reps));
                output.append(lastDigit);
                reps = 1;
            }

            // process final digit
            if (i == input.length() - 1)
            {
                output.append(String.valueOf(reps));
                output.append(digit);
            }

            lastDigit = digit;
        }

        return output.toString();
    }
}
