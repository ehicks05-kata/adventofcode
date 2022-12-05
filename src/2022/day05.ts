import { chunk } from "lodash";
import { readLines } from "./utils";

interface Ix {
  count: number;
  from: number;
  to: number;
}

const parseLines = (lines: string[]) => {
  const rawCrates = lines.slice(0, lines.indexOf(""));
  const rawInstructions = lines.slice(lines.indexOf("") + 1);

  console.log(rawCrates);
  const stacks = 0;
  const crates = rawCrates.reduce((agg, curr) => {
    const chunks = chunk(curr, 4);
  }, {});

  const instructions = rawInstructions.map(o => {
    const words = o.split(" ");
    return {count: words[1], from: words[3], to: words[5]};
  });

  return {instructions};
};

const run = async () => {
  const lines = await readLines("inputs/2022/05.txt");

  const {instructions} = parseLines(lines);
  const part1 = 0;
  const part2 = 0;
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
