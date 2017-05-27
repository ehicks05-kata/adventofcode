package net.ehicks.advent.year2015;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Day20InfiniteElves
{
    public static void main(String[] args) throws IOException
    {
        int numberOfPresentsToReach = 29_000_000;
        int firstHouseWithXPresents = 0;
        int mostPresentsReceived = 0;

        List<Integer> elves = new ArrayList<>();
        for (int i = 1; i < numberOfPresentsToReach / 10; i++)
            elves.add(i);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        houseLoop:
        for (int house = 1; house < 1_000_000; house++)
        {
            if (house % 2 != 0 || house % 3 != 0 || house % 5 != 0 || house % 7 != 0)
                continue;

            int presentsForHouse = 0;
            for (int elf : elves)
            {
                if (elf > house)
                {
                    if (presentsForHouse > mostPresentsReceived)
                    {
                        mostPresentsReceived = presentsForHouse;
                        System.out.println("house " + decimalFormat.format(house) + " got " + decimalFormat.format(presentsForHouse) + " presents.");
                    }

                    if (presentsForHouse >= numberOfPresentsToReach)
                    {
                        firstHouseWithXPresents = house;
                        break houseLoop;
                    }

                    continue houseLoop;
                }
                if (house % elf == 0)
                {
                    if (house > elf * 50)
                    {
//                        System.out.println("no delivery by elf " + elf + " to house " + house);
                    }
                    if (house <= elf * 50)
                        presentsForHouse += elf * 11;
                }
            }
        }

        System.out.println(firstHouseWithXPresents);
    }
}