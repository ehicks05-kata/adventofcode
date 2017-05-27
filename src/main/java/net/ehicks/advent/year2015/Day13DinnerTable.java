package net.ehicks.advent.year2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day13DinnerTable
{
    private static class Person implements Comparable
    {
        public String name = "";
        public Map<String, Integer> relationships = new HashMap<>();

        public Person(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            return name.equals(person.name);

        }

        @Override
        public int hashCode()
        {
            return name.hashCode();
        }

        @Override
        public int compareTo(Object o)
        {
            Person that = (Person) o;
            return this.name.compareToIgnoreCase(that.name);
        }
    }

    public static void main(String[] args) throws Exception
    {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/year2015/advent13.txt"));
        List<Person> persons = parsePeople(lines);
        int worstArrangementHappiness = Integer.MAX_VALUE;
        int bestArrangementHappiness = 0;
        List<Person> worstArrangement = new ArrayList<>();
        List<Person> bestArrangement = new ArrayList<>();

        Collections.sort(persons);

        while (persons != null)
        {
            int arrangementHappiness = getArrangementHappiness(persons);
            if (arrangementHappiness < worstArrangementHappiness)
            {
                worstArrangement = new ArrayList<>(persons);
                worstArrangementHappiness = arrangementHappiness;
            }
            if (arrangementHappiness > bestArrangementHappiness)
            {
                bestArrangement = new ArrayList<>(persons);
                bestArrangementHappiness = arrangementHappiness;
            }

            persons = CommonCombinatorics.getNextPermutation(persons);
        }

        System.out.println("Happiness of worst arrangement: " + worstArrangementHappiness);
        printArrangement(worstArrangement);

        System.out.println("Happiness of best arrangement: " + bestArrangementHappiness);
        printArrangement(bestArrangement);
    }

    private static int getArrangementHappiness(List<Person> persons)
    {
        int totalHappiness = 0;
        for (int i = 0; i < persons.size(); i++)
        {
            Person current = persons.get(i);
            Person previous;
            if (i > 0)
                previous = persons.get(i - 1);
            else
                previous = persons.get(persons.size() - 1);

            Person next;
            if (i < persons.size() - 1)
                next = persons.get(i + 1);
            else
                next = persons.get(0);

            int happiness = 0;

            happiness += current.relationships.get(previous.name);
            happiness += current.relationships.get(next.name);

            totalHappiness += happiness;
        }

        return totalHappiness;
    }

    private static List<Person> parsePeople(List<String> lines)
    {
        List<Person> persons = new ArrayList<>();
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            int amount = getAmount(parts);

            Person person = new Person(parts[0]);
            String person2 = parts[10].replace(".", "");

            person.relationships.put(person2, amount);
            person.relationships.put("ehicks", 0);

            int indexOfPerson = persons.indexOf(person);

            if (indexOfPerson > -1)
                persons.get(indexOfPerson).relationships.put(person2, amount);
            else
                persons.add(person);
        }

        Person ehicks = new Person("ehicks");
        for (Person person : persons)
            ehicks.relationships.put(person.name, 0);

        persons.add(ehicks);

        return persons;
    }

    private static int getAmount(String[] parts)
    {
        String verb = parts[2];
        String rawAmount = parts[3];

        int amount = Integer.parseInt(rawAmount);
        if (verb.equals("lose"))
            amount = -1 * amount;
        return amount;
    }

    private static void printArrangement(List<Person> persons)
    {
        String message = "";
        for (int i = 0; i < persons.size(); i++)
        {
            Person person = persons.get(i);
            Person previous;
            if (i > 0)
                previous = persons.get(i - 1);
            else
                previous = persons.get(persons.size() - 1);

            Person next;
            if (i < persons.size() - 1)
                next = persons.get(i + 1);
            else
                next = persons.get(0);

            int prevHappiness = person.relationships.get(previous.name);
            int nextHappiness = person.relationships.get(next.name);

            message += " <-" + prevHappiness + "-" + person.name + "-" + nextHappiness + "-> ";
        }
        System.out.println(message);
    }
}
