import { chunk, sum, sumBy } from "lodash";
import { readLines } from "./utils";

interface Sack {
  compartment1: string[];
  compartment2: string[];
  duplicate: string;
  duplicatePriority: number;
}

const toString = (sack: Sack) => ({
  ...sack,
  compartment1: sack.compartment1.join(""),
  compartment2: sack.compartment2.join(""),
});

const toPriority = (input: string) => {
  const a = "a".charCodeAt(0);
  const A = "A".charCodeAt(0);
  const UPPER_EXTRA_OFFSET = 26; // upper case meant to start at 27

  const isUpper = input.toUpperCase() === input;
  const charCode = input.charCodeAt(0);
  const offset = isUpper ? A - UPPER_EXTRA_OFFSET : a;
  // the '+ 1' makes this 1-based instead of 0-based
  return charCode - offset + 1;
};

const lineToSack = (line: string) => {
  const [compartment1, compartment2] = chunk(line.split(""), line.length / 2);
  const duplicate = compartment1.reduce((agg, curr) => {
    if (agg) return agg;
    return compartment2.indexOf(curr) !== -1 ? curr : "";
  }, "");
  const duplicatePriority = toPriority(duplicate);

  return { compartment1, compartment2, duplicate, duplicatePriority };
};

const getAllItems = (sack: Sack) => [
  ...sack.compartment1,
  ...sack.compartment2,
];

const processGroup = (sacks: Sack[]) => {
  const duplicate = getAllItems(sacks[0]).reduce((agg, curr) => {
    if (agg) return agg;
    const inSack2 = getAllItems(sacks[1]).includes(curr);
    const inSack3 = getAllItems(sacks[2]).includes(curr);
    return inSack2 && inSack3 ? curr : "";
  }, "");

  return toPriority(duplicate);
};

const run = async () => {
  const lines = await readLines("inputs/2022/03.txt");

  const sacks = lines.map(lineToSack);
  const part1 = sumBy(sacks, (o) => o.duplicatePriority);
  console.log(sacks.map(toString));
  console.log(`part1: ${part1}`);

  const groups = chunk(sacks, 3);
  const part2 = sum(groups.map(processGroup));
  console.log(`part2: ${part2}`);
};

run();
