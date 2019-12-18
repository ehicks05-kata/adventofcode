package net.ehicks.advent.year2019

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class ICTest {

    @Test
    fun day2Part1() {
        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/02.txt")))
        ic.machineState.mem[1] = 12
        ic.machineState.mem[2] = 2
        ic.run()

        Assertions.assertEquals(3058646L, ic.machineState.mem[0]!!)
    }

    @Test
    fun day2Part2() {
        val nounVerb = Pair(89L, 76L)

        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/02.txt")))
        ic.machineState.mem[1] = nounVerb.first
        ic.machineState.mem[2] = nounVerb.second
        ic.run()

        Assertions.assertEquals(19690720L, ic.machineState.mem[0])
        Assertions.assertEquals(8976, (100 * nounVerb.first) + nounVerb.second)
    }

    @Test
    fun day5Part1() {
        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/05.txt")))
        ic.feedInput(1L)
        ic.run()

        Assertions.assertEquals(2845163, ic.machineState.output.last())
    }

    @Test
    fun day5Part2() {
        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/05.txt")))
        ic.feedInput(5L)
        ic.run()

        Assertions.assertEquals(9436229, ic.machineState.output[0])
    }

    @Test
    fun day7Part1() {
        val program = Files.readString(Paths.get("src/main/resources/year2019/07.txt"))
        val phases = (0..4).toSet()
        val phasePermutations = getPhasePermutations(phases)

        val answer = phasePermutations.stream().mapToLong {
            val ic1 = IC(program)
            val ic2 = IC(program)
            val ic3 = IC(program)
            val ic4 = IC(program)
            val ic5 = IC(program)

            ic1.feedInput(it[0].toLong())
            ic1.feedInput(0L)
            ic1.run()

            ic2.feedInput(it[1].toLong())
            ic2.feedInput(ic1.getAndClearOutput().first())
            ic2.run()

            ic3.feedInput(it[2].toLong())
            ic3.feedInput(ic2.getAndClearOutput().first())
            ic3.run()

            ic4.feedInput(it[3].toLong())
            ic4.feedInput(ic3.getAndClearOutput().first())
            ic4.run()

            ic5.feedInput(it[4].toLong())
            ic5.feedInput(ic4.getAndClearOutput().first())
            ic5.run()

            ic5.getAndClearOutput().first()
        }.max().orElse(-1)

        Assertions.assertEquals(206580, answer)
    }

    @Test
    fun day7Part2() {
        val program = Files.readString(Paths.get("src/main/resources/year2019/07.txt"))
        val phases = (5..9).toSet()
        val phasePermutations = getPhasePermutations(phases)

        val answer = phasePermutations.stream().mapToLong {
            val ic1 = IC(program, false, "IC1")
            val ic2 = IC(program, false, "IC2")
            val ic3 = IC(program, false, "IC3")
            val ic4 = IC(program, false, "IC4")
            val ic5 = IC(program, false, "IC5")
            ic1.feedInput(it[0].toLong())
            ic2.feedInput(it[1].toLong())
            ic3.feedInput(it[2].toLong())
            ic4.feedInput(it[3].toLong())
            ic5.feedInput(it[4].toLong())

            var halted = false
            while (!halted) {
                val ic5Out = if (ic5.getOutput().isEmpty()) 0L else ic5.getOutput().last()

                ic1.feedInput(ic5Out)
                ic1.run()
                val ic1Out = ic1.getOutput().last()

                ic2.feedInput(ic1Out)
                ic2.run()
                val ic2Out = ic2.getOutput().last()

                ic3.feedInput(ic2Out)
                ic3.run()
                val ic3Out = ic3.getOutput().last()

                ic4.feedInput(ic3Out)
                ic4.run()
                val ic4Out = ic4.getOutput().last()

                ic5.feedInput(ic4Out)
                ic5.run()

                halted = ic1.machineState.halted ||
                        ic2.machineState.halted ||
                        ic3.machineState.halted ||
                        ic4.machineState.halted ||
                        ic5.machineState.halted
            }

            ic5.getOutput().last()
        }.max().orElse(-1)

        Assertions.assertEquals(2299406, answer)
    }

    @Test
    fun day9Part1() {
        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/09.txt")))
        ic.feedInput(1L)
        ic.run()

        Assertions.assertEquals(2377080455, ic.machineState.output.first())
    }

    @Test
    fun day9Part2() {
        val ic = IC(Files.readString(Paths.get("src/main/resources/year2019/09.txt")))
        ic.feedInput(2L)
        ic.run()

        Assertions.assertEquals(74917, ic.machineState.output.first())
    }
}