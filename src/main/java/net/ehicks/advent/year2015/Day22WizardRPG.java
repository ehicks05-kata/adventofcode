package net.ehicks.advent.year2015;

import java.io.IOException;
import java.util.*;

public class Day22WizardRPG
{
    static final Spell magicMissile = new Spell("Magic Missile", 53, 0);
    static final Spell drain = new Spell("Drain", 73, 0);
    static final Spell shield = new Spell("Shield", 113, 6);
    static final Spell poison = new Spell("Poison", 173, 6);
    static final Spell recharge = new Spell("Recharge", 229, 5);
    static final List<Spell> spells = Arrays.asList(magicMissile, drain, shield, poison, recharge);
    static final Random random = new Random();

    public static void main(String[] args) throws IOException
    {
        int leastManaSpent = Integer.MAX_VALUE;

        int simulationsRan = 0;
        while (true)
        {
            FightInfo fightInfo = runSimulation();
            if (fightInfo == null)
                continue;

            int manaSpent = fightInfo.manaSpent;

            if (manaSpent < leastManaSpent)
            {
                leastManaSpent = manaSpent;
                System.out.println(manaSpent);
                for (String spellName : fightInfo.spellsCast)
                    System.out.println(spellName);
                System.out.println();
            }
            simulationsRan++;
            if (simulationsRan % 1_000 == 0)
                System.out.println("sims ran: " + simulationsRan);
        }
    }

    private static FightInfo runSimulation()
    {
        Fighter player = new Fighter("player", 50, 0, 0, 500);
        Fighter boss = new Fighter("boss", 58, 9, 0, 0);
        int manaSpent = 0;
        List<String> spellsCast = new ArrayList<>();
        int turn = 1;
        while (player.hp > 0 && boss.hp > 0)
        {
            //Start Player Turn

            // hard mode
            player.hp -= 1;
            if (player.hp <= 0)
                return null;

            applyEffects(player, boss);
            if (boss.hp <= 0)
                return new FightInfo(manaSpent, spellsCast);

            Spell spellToCast = spells.get(random.nextInt(spells.size()));
            boolean conflictsWithExistingEffect = conflictsWithExistingEffect(player, boss, spellToCast);
            while (conflictsWithExistingEffect)
            {
                spellToCast = spells.get(random.nextInt(spells.size()));
                conflictsWithExistingEffect = conflictsWithExistingEffect(player, boss, spellToCast);
            }

            if (player.mana < spellToCast.mana)
                return null;

            castSpell(player, boss, spellToCast);
            player.mana -= spellToCast.mana;
            manaSpent += spellToCast.mana;

            String message = String.format("turn %3s: %-16s player: %3d hp %3d mp  boss: %3d hp", turn, spellToCast.name, player.hp, player.mana, boss.hp);
            spellsCast.add(message);

            if (boss.hp <= 0)
                return new FightInfo(manaSpent, spellsCast);
            turn++;

            //Start Boss Turn
            applyEffects(player, boss);
            if (boss.hp <= 0)
                return new FightInfo(manaSpent, spellsCast);

            attack(boss, player);

            if (player.hp <= 0)
                return null;
            turn++;
        }

        return null;
    }

    private static boolean conflictsWithExistingEffect(Fighter player, Fighter boss, Spell spell)
    {
        if (spell == poison && hasEffect(boss, poison))
            return true;
        else if (spell == shield && hasEffect(player, shield))
            return true;
        else if (spell == recharge && hasEffect(player, recharge))
            return true;
        return false;
    }

    private static class FightInfo
    {
        int manaSpent = 0;
        List<String> spellsCast = new ArrayList<>();

        public FightInfo(int manaSpent, List<String> spellsCast)
        {
            this.manaSpent = manaSpent;
            this.spellsCast = spellsCast;
        }
    }

    private static void applyEffects(Fighter f1, Fighter f2)
    {
        for (Fighter fighter : Arrays.asList(f1, f2))
            for (Iterator<Spell> i = fighter.activeEffects.iterator(); i.hasNext();)
            {
                Spell spell = i.next();
                applyEffect(fighter, spell);
                spell.duration--;
                if (spell.duration == 0)
                {
                    if (spell.equals(shield))
                        fighter.armor -= 7;

                    i.remove();
                }
            }
    }

    private static void applyEffect(Fighter fighter, Spell spell)
    {
        if (spell.equals(poison))
            fighter.hp -= 3;
        if (spell.equals(recharge))
            fighter.mana += 101;
    }

    private static void castSpell(Fighter f1, Fighter f2, Spell spell)
    {
        if (spell.equals(poison))
            f2.activeEffects.add(new Spell(poison));
        if (spell.equals(shield))
        {
            f1.activeEffects.add(new Spell(shield));
            f1.armor += 7;
        }
        if (spell.equals(recharge))
            f1.activeEffects.add(new Spell(recharge));
        if (spell.equals(magicMissile))
            f2.hp -= 4;
        if (spell.equals(drain))
        {
            f1.hp += 2;
            f2.hp -= 2;
        }
    }

    private static boolean hasEffect(Fighter fighter, Spell spell)
    {
        return fighter.activeEffects.contains(spell);
    }

    private static void attack(Fighter f1, Fighter f2)
    {
        int damageDealt = f1.damage - f2.armor;
        if (damageDealt < 1)
            damageDealt = 1;

        f2.hp -= damageDealt;
    }

    private static class Spell
    {
        String name;
        int mana;
        int duration;

        public Spell(String name, int mana, int duration)
        {
            this.name = name;
            this.mana = mana;
            this.duration = duration;
        }

        public Spell(Spell spell)
        {
            this.name = spell.name;
            this.mana = spell.mana;
            this.duration = spell.duration;
        }

        @Override
        public int hashCode()
        {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Spell))
                return false;
            Spell that = (Spell) obj;
            return this.name.equals(that.name);
        }

        public String toString()
        {
            return name;
        }
    }

    private static class Fighter
    {
        String name;
        int hp;
        int damage;
        int armor;
        int mana;
        List<Spell> activeEffects = new ArrayList<>();

        public Fighter(String name, int hp, int damage, int armor, int mana)
        {
            this.name = name;
            this.hp = hp;
            this.damage = damage;
            this.armor = armor;
            this.mana = mana;
        }

        public String toString()
        {
            return name + " " + hp + "hp, " + mana + " mana ";
        }
    }
}


/*
--- Day 22: Wizard Simulator 20XX ---

Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a wizard.
Of course, he gets stuck on another boss and needs your help again.

In this version, combat still proceeds with the player and the boss taking alternating turns.
The player still goes first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first character at or below 0 hit points loses.

Since you're a wizard, you don't get to wear armor, and you can't attack normally.
However, since you do magic damage, your opponent's armor is ignored,
and so the boss effectively has zero armor as well.
As before, if armor (from a spell, in this case) would reduce damage below 1,
it becomes 1 instead - that is, the boss' attacks always deal at least 1 damage.

On each of your turns, you must select one of your spells to cast.
If you cannot afford to cast any spell, you lose. Spells cost mana; you start with 500 mana,
but have no maximum limit. You must have enough mana to cast a spell,
and its cost is immediately deducted when you cast it.
Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.

Magic Missile costs 53 mana. It instantly does 4 damage.
Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.
Shield costs 113 mana. It starts an effect that lasts for 6 turns. While it is active, your armor is increased by 7.
Poison costs 173 mana. It starts an effect that lasts for 6 turns. At the start of each turn while it is active, it deals the boss 3 damage.
Recharge costs 229 mana. It starts an effect that lasts for 5 turns. At the start of each turn while it is active, it gives you 101 new mana.
Effects all work the same way. Effects apply at the start of both the player's turns and the boss' turns.
Effects are created with a timer (the number of turns they last); at the start of each turn,
after they apply any effect they have, their timer is decreased by one.
If this decreases the timer to zero, the effect ends.
You cannot cast a spell that would start an effect which is already active.
However, effects can be started on the same turn they end.

-- Boss turn --
- Player has 1 hit point, 0 armor, 114 mana
- Boss has 2 hit points
Poison deals 3 damage. This kills the boss, and the player wins.
You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.
What is the least amount of mana you can spend and still win the fight?
(Do not include mana recharge effects as "spending" negative mana.)
  */