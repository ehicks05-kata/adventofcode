import { readLines } from "./utils";

const countVisibleTrees = (lines: string[]) => {
  return 0;
};

const run = async () => {
  const lines = await readLines("inputs/2022/08.txt");

  const part1 = countVisibleTrees(lines);
  const part2 = 0;
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
