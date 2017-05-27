package net.ehicks.advent.year2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day15MilkAndCookies
{
    private static class Ingredient
    {
        public String name = "";
        public int capacity;
        public int durability;
        public int flavor;
        public int texture;
        public int calories;

        public Ingredient(String name)
        {
            this.name = name;
        }
    }

    public static void main(String[] args) throws Exception
    {
        List<Ingredient> ingredients = parseInputFile("src/main/resources/year2015/advent15.txt");
        int highestScore = 0;

        for (int i = 0; i <= 100; i++)
        {
            for (int j = 0; j <= 100; j++)
            {
                for (int k = 0; k <= 100; k++)
                {
                    for (int l = 0; l <= 100; l++)
                    {
                        if (i + j + k + l != 100) continue;

                        Map<Ingredient, Integer> recipe = new HashMap<>();
                        recipe.put(ingredients.get(0), i);
                        recipe.put(ingredients.get(1), j);
                        recipe.put(ingredients.get(2), k);
                        recipe.put(ingredients.get(3), l);

                        int score = getScore(recipe);
                        if (score > highestScore)
                            highestScore = score;
                    }
                }
            }
        }

        System.out.println("Highest Score: " + highestScore);
    }

    private static int getScore(Map<Ingredient, Integer> recipe)
    {
        int score = 0;

        int capacity = 0;
        int durability = 0;
        int flavor = 0;
        int texture = 0;
        int calories = 0;

        for (Ingredient ingredient : recipe.keySet())
        {
            capacity += ingredient.capacity * recipe.get(ingredient);
            durability += ingredient.durability * recipe.get(ingredient);
            flavor += ingredient.flavor * recipe.get(ingredient);
            texture += ingredient.texture * recipe.get(ingredient);
            calories += ingredient.calories * recipe.get(ingredient);
        }

        if (capacity < 0) capacity = 0;
        if (durability < 0) durability = 0;
        if (flavor < 0) flavor = 0;
        if (texture < 0) texture = 0;
        if (calories < 0) calories = 0;

        if (calories != 500) return 0;

        score = capacity * durability * flavor * texture;
        return score;
    }

    private static List<Ingredient> parseInputFile(String filename) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Ingredient> ingredients = new ArrayList<>();
        for (String line : lines)
        {
            String[] parts = line.split(" ");
            Ingredient ingredient = new Ingredient(parts[0].replace(":", ""));
            ingredient.capacity = Integer.parseInt(parts[2].replace(",", ""));
            ingredient.durability = Integer.parseInt(parts[4].replace(",", ""));
            ingredient.flavor = Integer.parseInt(parts[6].replace(",", ""));
            ingredient.texture = Integer.parseInt(parts[8].replace(",", ""));
            ingredient.calories = Integer.parseInt(parts[10].replace(",", ""));
            ingredients.add(ingredient);
        }

        return ingredients;
    }
}
