package net.ehicks.advent.year2015;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day09TravelDistance
{
    private static class Location implements Comparable
    {
        public String name = "";
        public Map<String, Integer> distances = new HashMap<>();

        public Location(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return "Location{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Location location = (Location) o;

            return name.equals(location.name);

        }

        @Override
        public int hashCode()
        {
            return name.hashCode();
        }

        @Override
        public int compareTo(Object o)
        {
            Location that = (Location) o;
            return this.name.compareToIgnoreCase(that.name);
        }
    }

    public static void main(String[] args) throws Exception
    {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/year2015/advent09.txt"));
        List<Location> locations = parseLocations(lines);
        int shortestRouteLength = Integer.MAX_VALUE;
        int longestRouteLength = 0;
        List<Location> shortestRoute = new ArrayList<>();
        List<Location> longestRoute = new ArrayList<>();

        Collections.sort(locations);

        while (locations != null)
        {
            int routeLength = getRouteLength(locations);
            if (routeLength < shortestRouteLength)
            {
                shortestRoute = new ArrayList<>(locations);
                shortestRouteLength = routeLength;
            }
            if (routeLength > longestRouteLength)
            {
                longestRoute = new ArrayList<>(locations);
                longestRouteLength = routeLength;
            }

            locations = CommonCombinatorics.getNextPermutation(locations);
        }

        System.out.println("Distance of shortest route: " + shortestRouteLength);
        printRoute(shortestRoute);

        System.out.println("Distance of longest route: " + longestRouteLength);
        printRoute(longestRoute);
    }

    private static int getRouteLength(List<Location> locations)
    {
        int routeLength = 0;
        Location previous = null;
        for (Location location : locations)
        {
            int length = 0;
            if (previous != null)
            {
                length = location.distances.get(previous.name);
            }
            routeLength += length;
            previous = location;
        }

        return routeLength;
    }

    private static List<Location> parseLocations(List<String> lines)
    {
        List<Location> locations = new ArrayList<>();
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            String location1 = parts[0];
            String location2 = parts[2];
            int distance = Integer.parseInt(parts[4]);

            Location origin = new Location(location1);
            origin.distances.put(location2, distance);

            int indexOfOrigin = locations.indexOf(origin);

            if (indexOfOrigin > -1)
                locations.get(indexOfOrigin).distances.put(location2, distance);
            else
                locations.add(origin);

            Location destination = new Location(location2);
            destination.distances.put(location1, distance);

            int indexOfDestination = locations.indexOf(destination);

            if (indexOfDestination > -1)
                locations.get(indexOfDestination).distances.put(location1, distance);
            else
                locations.add(destination);
        }

        return locations;
    }

    private static void printRoute(List<Location> locations)
    {
        String message = "";
        Location previous = null;
        for (Location location : locations)
        {
            int length = 0;
            if (previous != null)
            {
                length = location.distances.get(previous.name);
            }

            if (message.length() > 0)
                message += " --(" + length + ")--> ";
            message += location.name;
            previous = location;
        }
        System.out.println(message);
    }
}
