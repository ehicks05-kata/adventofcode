import { readLines } from "./utils";

const countSteps = (lines: string[]) => {
  return 0;
};

const run = async () => {
  const lines = (await readLines("inputs/2022/12.txt"))
    .filter((o) => o.length)
    .map((o) => o.trim());

  const part1 = countSteps(lines);
  const part2 = 0;
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
