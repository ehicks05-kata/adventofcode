import { readLines } from "./utils";

const parseLines = (lines: string[]) => {
  const rawCrates = lines.slice(0, lines.indexOf("\r") - 1);
  const rawInstructions = lines.slice(lines.indexOf("\r") + 1);

  const stacks = rawCrates[0].length / 4;
  const crates: string[][] = [];

  for (let i = 0; i < stacks; i++) {
    const index = i * 4 + 1;

    const stack = rawCrates.reduce((agg, curr) => {
      const value = curr[index];
      if (value !== " ") agg.push(value);
      return agg;
    }, [] as string[]);

    crates[i] = stack.reverse();
  }

  const instructions = rawInstructions.map((o) => {
    const words = o.split(" ").map(Number);
    return { count: words[1], from: words[3] - 1, to: words[5] - 1 };
  });

  return { crates, instructions };
};

const run = async () => {
  const lines = await readLines("inputs/2022/05.txt");

  const { crates, instructions } = parseLines(lines);
  console.log(crates);

  const part1Crates = [...crates];
  instructions.forEach(({ count, from, to }) => {
    while (count > 0) {
      const crate = part1Crates[from].pop();
      part1Crates[to].push(crate!);
      count--;
    }
  });

  const part2Crates = parseLines(lines).crates;
  instructions.forEach(({ count, from, to }) => {
    const slice = part2Crates[from].splice(-count);
    part2Crates[to].push(...slice);
  });

  const part1 = part1Crates.map((o) => o.pop()).join("");
  const part2 = part2Crates.map((o) => o.pop()).join("");
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
