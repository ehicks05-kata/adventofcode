package net.ehicks.advent.year2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7RecursiveCircus {
    public static void main(String[] args) throws IOException
    {
        List<String> rows = Files.readAllLines(Paths.get("src/main/resources/year2017/07.txt"));

        Program root = parsePrograms(rows);
        System.out.println("part 1: " + root.name);

//        System.out.println("part 2: " + infiniteCycleSize);
    }

    public static Program parsePrograms(List<String> rows)
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
            program.children = childrenNames;

            programs.add(program);
        }

        Program root = findRootProgram(programs);

        return root;
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
