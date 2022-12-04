import { sumBy } from "lodash";
import { readLines } from "./utils";

interface Range {
  from: number;
  to: number;
}

const toString = (range: Range) => {
  return `${range.from}-${range.to}`;
};

const toRange = (input: string) => {
  const fromTo = input.split("-").map(Number);
  return { from: fromTo[0], to: fromTo[1] };
};

const toRangePairs = (line: string) => {
  return line.split(",").map(toRange);
};

const isTotalOverlap = (ranges: Range[]) => {
  const [r1, r2] = ranges;
  const firstContainsSecond = r1.from <= r2.from && r1.to >= r2.to;
  const secondContainsFirst = r2.from <= r1.from && r2.to >= r1.to;
  const isContainment = firstContainsSecond || secondContainsFirst;
  if (isContainment) {
    console.log(`containment: ${ranges.map(toString)}`);
  }
  return isContainment;
};

const isPartialOverlap = (ranges: Range[]) => {
  const [r1, r2] = ranges;
  const isOverlap = r1.from <= r2.to && r2.from <= r1.to;
  if (isOverlap) {
    console.log(`partial: ${ranges.map(toString)}`);
  }
  return isOverlap;
};

const run = async () => {
  const lines = await readLines("inputs/2022/04.txt");
  const rangePairs = lines.map(toRangePairs);
  console.log(rangePairs);
  const part1 = sumBy(rangePairs, (ranges) => (isTotalOverlap(ranges) ? 1 : 0));
  const part2 = sumBy(rangePairs, (ranges) => (isPartialOverlap(ranges) ? 1 : 0));
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
