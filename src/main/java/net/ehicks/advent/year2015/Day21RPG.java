package net.ehicks.advent.year2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day21RPG
{
    public static void main(String[] args) throws IOException
    {
        List<Item> weapons = new ArrayList<>();
        weapons.add(new Item("Dagger", 8, 4, 0));
        weapons.add(new Item("Shortsword", 10, 5, 0));
        weapons.add(new Item("Warhammer", 25, 6, 0));
        weapons.add(new Item("Longsword", 40, 7, 0));
        weapons.add(new Item("Greataxe", 74, 8, 0));

        List<Item> armors = new ArrayList<>();
        armors.add(new Item("Leather", 13, 0, 1));
        armors.add(new Item("Chainmail", 31, 0, 2));
        armors.add(new Item("Splintmail", 53, 0, 3));
        armors.add(new Item("Bandedmail", 75, 0, 4));
        armors.add(new Item("Platemail", 102, 0, 5));

        List<Item> rings = new ArrayList<>();
        rings.add(new Item("Damage +1", 25, 1, 0));
        rings.add(new Item("Damage +2", 50, 2, 0));
        rings.add(new Item("Damage +3", 100, 3, 0));
        rings.add(new Item("Defense +1", 20, 0, 1));
        rings.add(new Item("Defense +2", 40, 0, 2));
        rings.add(new Item("Defense +3", 80, 0, 3));

        int cheapestWin = Integer.MAX_VALUE;
        int costliestLoss = 0;

        for (int i = 0; i < weapons.size(); i++)
            for (int j = -1; j < armors.size(); j++)
                for (int k = -1; k < rings.size(); k++)
                    for (int l = -1; l < rings.size(); l++)
                    {
                        if (k > -1 && k == l) continue;

                        List<Item> outfit = new ArrayList<>();
                        outfit.add(weapons.get(i));
                        if (j > -1) outfit.add(armors.get(j));
                        if (k > -1) outfit.add(rings.get(k));
                        if (l > -1) outfit.add(rings.get(l));

                        int cost = 0;
                        int damageBonus = 0;
                        int armorBonus = 0;

                        for (Item item : outfit)
                        {
                            cost += item.cost;
                            damageBonus += item.damage;
                            armorBonus += item.armor;
                        }

                        Fighter player = new Fighter("player", 100, damageBonus, armorBonus);
                        Fighter boss = new Fighter("boss", 100, 8, 2);
                        String winner = resolveFight(player, boss);
                        if (winner.equals("player"))
                        {
                            if (cost < cheapestWin)
                            {
                                cheapestWin = cost;
                                System.out.println("win: " + cheapestWin);
                            }
                        }
                        if (winner.equals("boss"))
                        {
                            if (cost > costliestLoss)
                            {
                                costliestLoss = cost;
                                System.out.println("loss: " + costliestLoss);
                            }
                        }
                    }

        System.out.println("cheapest win: " + cheapestWin);
        System.out.println("costliest loss: " + costliestLoss);
    }

    private static String resolveFight(Fighter f1, Fighter f2) throws IOException
    {
        while (f1.hp > 0 && f2.hp > 0)
        {
            attack(f1, f2);
            if (f2.hp <= 0) return f1.name;
            attack(f2, f1);
            if (f1.hp <= 0) return f2.name;
        }

        return null;
    }

    private static void attack(Fighter f1, Fighter f2)
    {
        int damageDealt = f1.damage - f2.armor;
        if (damageDealt < 1)
            damageDealt = 1;

        f2.hp -= damageDealt;
    }

    private static class Item
    {
        String name;
        int cost;
        int damage;
        int armor;

        public Item(String name, int cost, int damage, int armor)
        {
            this.name = name;
            this.cost = cost;
            this.damage = damage;
            this.armor = armor;
        }
    }

    private static class Fighter
    {
        String name;
        int hp;
        int damage;
        int armor;

        public Fighter(String name, int hp, int damage, int armor)
        {
            this.name = name;
            this.hp = hp;
            this.damage = damage;
            this.armor = armor;
        }
    }
}


/*
--- Day 21: RPG Simulator 20XX ---

Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss. He needs to know what equipment to buy at the shop. He hands you the controller.

In this game, the player (you) and the enemy (the boss) take turns attacking. The player always goes first. Each attack reduces the opponent's hit points by at least 1. The first character at or below 0 hit points loses.

Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor score. An attacker always does at least 1 damage. So, if the attacker has a damage score of 8, and the defender has an armor score of 3, the defender loses 5 hit points. If the defender had an armor score of 300, the defender would still lose 1 hit point.

Your damage score and armor score both start at zero. They can be increased by buying items in exchange for gold. You start with no items and have as much gold as you need. Your total damage or armor is equal to the sum of those stats from all of your items. You have 100 hit points.

Here is what the item shop is selling:

Weapons:    Cost  Damage  Armor
Dagger        8     4       0
Shortsword   10     5       0
Warhammer    25     6       0
Longsword    40     7       0
Greataxe     74     8       0

Armor:      Cost  Damage  Armor
Leather      13     0       1
Chainmail    31     0       2
Splintmail   53     0       3
Bandedmail   75     0       4
Platemail   102     0       5

Rings:      Cost  Damage  Armor
Damage +1    25     1       0
Damage +2    50     2       0
Damage +3   100     3       0
Defense +1   20     0       1
Defense +2   40     0       2
Defense +3   80     0       3
You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one. You can buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one of each item, so you can't buy, for example, two rings of Damage +3.

For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage, and 2 armor:

The player deals 5-2 = 3 damage; the boss goes down to 9 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 6 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 6 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 4 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 3 hit points.
The boss deals 7-5 = 2 damage; the player goes down to 2 hit points.
The player deals 5-2 = 3 damage; the boss goes down to 0 hit points.
In this scenario, the player wins! (Barely.)

You have 100 hit points. The boss's actual stats are in your puzzle input. What is the least amount of gold you can spend and still win the fight?
  */