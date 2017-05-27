package net.ehicks.advent.year2015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonCombinatorics
{
    public static String getNextString(String s)
    {
        int length = s.length();
        char c = s.charAt(length - 1);

        if (c == 'z')
            return length > 1 ? getNextString(s.substring(0, length - 1)) + 'a' : "aa";

        return s.substring(0, length - 1) + ++c;
    }

    public static <T extends Comparable<T>> List<T> getNextPermutation(List<T> digits)
    {
        // Find the largest index k such that a[k] < a[k + 1]. If no such index exists, the permutation is the last permutation.
        int k = -1;
        for(int i = 0; i < digits.size() - 1; i++)
        {
            if(digits.get(i).compareTo(digits.get(i + 1)) < 0) k = i;
        }
        if(k == -1)
        {
            System.out.println("k was -1...");
//            reverse(digits);
            return null;
        }

        //Find the largest index l such that a[k] < a[l]. Since k + 1 is such an index, l is well defined and satisfies k < l.
        int l = 0;
        for(int i = 0; i < digits.size(); i++)
        {
            if(digits.get(k).compareTo(digits.get(i)) < 0) l = i;
        }
        //Swap a[k] with a[l].
        if(k != -1) swapItems(digits, k, l);
        //Reverse the sequence from a[k + 1] up to and including the final element a[n].
        if(k != -1)
        {
            List<T> part1 = innerList(digits, 0, k + 1);
            List<T> part2 = innerList(digits, k + 1, digits.size());
            Collections.reverse(part2);
            part1.addAll(part2);
            //System.out.println("K: " + k + " L: " + l);
            return part1;
        }

        return digits;
    }

    private static <T extends Comparable<T>> List<T> innerList(List<T> original, int start, int end)
    {
        int size = end - start;
        List<T> inner = generateArray(size);
        for(int i = 0; i < size; i++)
        {
            inner.set(i, original.get(start));
            start++;
        }
        return inner;
    }

    private static <T extends Comparable<T>> List<T> generateArray(int size)
    {
        List<T> digits = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            T comparable = (T) new Integer(i);
            digits.add(comparable);
        }
        return digits;
    }

    private static void print(List list)
    {
        for (Object o : list)
            System.out.print(o + " ");
        System.out.println();
    }

    private static <T extends Comparable<T>> void swapItems(List<T> list, Integer index1, Integer index2)
    {
        T firstItem = list.get(index1);
        T secondItem = list.get(index2);

        list.set(index1, secondItem);
        list.set(index2, firstItem);
    }
}