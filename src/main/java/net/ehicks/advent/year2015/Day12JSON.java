package net.ehicks.advent.year2015;

import javax.json.*;
import java.io.FileReader;

public class Day12JSON
{
    public static void main(String[] args) throws Exception
    {
        JsonReader jsonReader = Json.createReader(new FileReader("src/main/resources/year2015/advent12.txt"));
        JsonStructure jsonArray = jsonReader.read();

        int total = walkObject(jsonArray);
        System.out.println("total: " + total);
    }

    private static int walkObject(JsonValue jsonValue)
    {
        int total = 0;
        if (jsonValue instanceof JsonArray)
        {
            JsonArray jsonArray = (JsonArray) jsonValue;
            for (int i = 0; i < jsonArray.size(); i++)
                total += walkObject(jsonArray.get(i));
        }
        if (jsonValue instanceof JsonObject)
        {
            JsonObject jsonObject = (JsonObject) jsonValue;

            boolean containsRed = false;
            for (String key : jsonObject.keySet())
                if (containsRed(jsonObject.get(key)))
                    containsRed = true;

            for (String key : jsonObject.keySet())
            {
                if (containsRed)
                    total += 0;
                else
                    total += walkObject(jsonObject.get(key));
            }
        }
        if (jsonValue instanceof JsonNumber)
        {
            JsonNumber jsonNumber = (JsonNumber) jsonValue;
            total += jsonNumber.intValue();
        }
        return total;
    }

    private static boolean containsRed(JsonValue jsonValue)
    {
        if (jsonValue instanceof JsonString)
        {
            JsonString jsonString = (JsonString) jsonValue;
            if (jsonString.getString().equals("red"))
                return true;
        }

        return false;
    }
}
