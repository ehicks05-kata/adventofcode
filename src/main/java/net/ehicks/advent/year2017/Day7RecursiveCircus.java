package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day7RecursiveCircus {
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/07.txt"));

        List<Program> programs = parsePrograms(rows);
        Program root = findRootProgram(programs);

        System.out.println("part 1: " + root);

        getTowerWeights(programs, root);

        String misweightedProgram = findMisweightedProgram(programs, root);

        System.out.println("part 2: (answer of '1505' was found by stepping through debugger)");
    }


    public static String findMisweightedProgram(List<Program> programs, Program root)
    {
        String misweightedProgram = "";

        Map<Integer, List<String>> weightToName = new HashMap<>();
        for (String childName : root.children)
        {
            Program child = getProgramByName(programs, childName);
            List<String> names = weightToName.get(child.towerWeight);
            if (names == null)
                names = new ArrayList<>();
            names.add(child.name);
            weightToName.put(child.towerWeight, names);
        }

        weightToName.entrySet().removeIf(integerListEntry -> integerListEntry.getValue().size() > 1);
        List<String> possibleMisweightedChildNames = weightToName.values().stream()
                .flatMap(List::stream).collect(Collectors.toList());

        for (String childName : possibleMisweightedChildNames)
        {
            Program child = getProgramByName(programs, childName);
            misweightedProgram = findMisweightedProgramInner(programs, child);
        }

        return misweightedProgram;
    }

    public static String findMisweightedProgramInner(List<Program> programs, Program root)
    {
        if (root.children.isEmpty())
            return "";

        String misweightedProgram = "";

        Map<Integer, List<String>> weightToName = new HashMap<>();
        for (String childName : root.children)
        {
            Program child = getProgramByName(programs, childName);
            List<String> names = weightToName.get(child.towerWeight);
            if (names == null)
                names = new ArrayList<>();
            names.add(child.name);
            weightToName.put(child.towerWeight, names);
        }

        weightToName.entrySet().removeIf(integerListEntry -> integerListEntry.getValue().size() > 1);
        List<String> possibleMisweightedChildNames = weightToName.values().stream()
                .flatMap(List::stream).collect(Collectors.toList());

        for (String childName : possibleMisweightedChildNames)
        {
            Program child = getProgramByName(programs, childName);
            misweightedProgram = findMisweightedProgramInner(programs, child);
        }

        return misweightedProgram;
    }

    public static void getTowerWeights(List<Program> programs, Program root)
    {
        int totalWeight = 0;

        for (String childName : root.children)
        {
            Program child = getProgramByName(programs, childName);
            totalWeight += getTowerWeightsInner(programs, child);
        }
        root.towerWeight = totalWeight;
    }

    public static int getTowerWeightsInner(List<Program> programs, Program root)
    {
        if (root.children.isEmpty())
            return root.weight;

        int totalChildrenWeight = 0;

        for (String childName : root.children)
        {
            Program child = getProgramByName(programs, childName);
            totalChildrenWeight += getTowerWeightsInner(programs, child);
        }
        root.towerWeight += totalChildrenWeight;
        return root.towerWeight;
    }

    public static List<Program> parsePrograms(List<String> rows)
    {
        List<Program> programs = new ArrayList<>();

        for (String row : rows)
        {
            List<String> childrenNames = new ArrayList<>();

            String name = row.substring(0, row.indexOf(" "));
            row = row.substring(row.indexOf(" ") + 1);

            int weight = Integer.parseInt(row.substring(row.indexOf("(") + 1, row.indexOf(")")));
            row = row.substring(row.indexOf(")") + 1);

            if (row.length() > 0)
            {
                row = row.replace(" -> ", "");
                childrenNames = Arrays.asList(row.split(", "));
            }

            Program program = new Program(name);
            program.weight = weight;
            program.towerWeight = weight;
            program.children = childrenNames;

            programs.add(program);
        }

        return programs;
    }

    private static Program findRootProgram(List<Program> programs) {
        List<String> programsListedAsChildren = new ArrayList<>();

        programs.forEach(program -> programsListedAsChildren.addAll(program.children));

        return programs.stream().map(Program::getName)
                .filter(programName -> !programsListedAsChildren.contains(programName))
                .findFirst().map(programName -> getProgramByName(programs, programName)).orElse(null);
    }

    public static Program getProgramByName(List<Program> programs, String name)
    {
        for (Program program : programs)
            if (program.name.equals(name))
                return program;
        return null;
    }

    public static class Program
    {
        String name;
        int weight;
        int towerWeight;
        List<String> children = new ArrayList<>();

        public Program(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Program{" +
                    "name='" + name + '\'' +
                    ", weight=" + weight +
                    ", children=" + children +
                    '}';
        }

        public String getName() {
            return name;
        }
    }
}
