import { sum, sortBy, sumBy } from "lodash";
import { readLines } from "./utils";

const run = async () => {
  const lines = await readLines("inputs/2022/01.txt");
  const elves = lines
    .join("\n")
    .split("\n\n")
    .map((o) => {
      const values = o.split("\n").map((oo) => Number(oo));
      return { values, sum: sum(values) };
    });

  const topThree = sortBy(elves, (o) => o.sum)
    .reverse()
    .slice(0, 3);
  console.log(topThree);

  const topThreeSum = sumBy(topThree, (o) => o.sum);
  console.log(topThreeSum);
};

run();
