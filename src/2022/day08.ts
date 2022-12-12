import { range } from "lodash";
import { readLines } from "./utils";

const isEdge = (
  rows: string[],
  rowIndex: number,
  cols: string[],
  colIndex: number
) => {
  return (
    rowIndex === 0 ||
    colIndex === 0 ||
    rowIndex === rows.length - 1 ||
    colIndex === cols.length - 1
  );
};

const countTreesVisibleFromOutside = (rows: string[]) => {
  let visibleCount = 0;
  rows.forEach((row, rowIndex) => {
    const cells = row.split("");
    cells.forEach((cell, colIndex) => {
      if (isEdge(rows, rowIndex, cells, colIndex)) {
        visibleCount++;
        return;
      }

      const currentTree = Number(cell);

      const visibleFromTop = range(0, rowIndex)
        .map((checkRowIndex) => rows[checkRowIndex].split("")[colIndex])
        .map(Number)
        .every((o) => o < currentTree);
      const visibleFromBottom = range(rows.length - 1, rowIndex, -1)
        .map((checkRowIndex) => rows[checkRowIndex].split("")[colIndex])
        .map(Number)
        .every((o) => o < currentTree);
      const visibleFromLeft = range(0, colIndex)
        .map((checkColIndex) => rows[rowIndex].split("")[checkColIndex])
        .map(Number)
        .every((o) => o < currentTree);
      const visibleFromRight = range(cells.length - 1, colIndex, -1)
        .map((checkColIndex) => rows[rowIndex].split("")[checkColIndex])
        .map(Number)
        .every((o) => o < currentTree);

      if (
        visibleFromTop ||
        visibleFromBottom ||
        visibleFromLeft ||
        visibleFromRight
      ) {
        visibleCount++;
        return;
      }
    });
  });

  return visibleCount;
};

const getHighestScenicScore = (rows: string[]) => {
  let highestScenicScore = 0;

  rows.forEach((row, rowIndex) => {
    const cells = row.split("");
    cells.forEach((cell, colIndex) => {
      if (isEdge(rows, rowIndex, cells, colIndex)) {
        return;
      }

      const currentTree = Number(cell);

      let visibleUp = 0;
      for (let i = 1; rowIndex - i >= 0; i++) {
        visibleUp++;
        const otherTree = Number(rows[rowIndex - i].split("")[colIndex]);
        if (otherTree >= currentTree) {
          break;
        }
      }

      let visibleDown = 0;
      for (let i = 1; rowIndex + i < rows.length; i++) {
        visibleDown++;
        const otherTree = Number(rows[rowIndex + i].split("")[colIndex]);
        if (otherTree >= currentTree) {
          break;
        }
      }

      let visibleLeft = 0;
      for (let i = 1; colIndex - i >= 0; i++) {
        visibleLeft++;
        const otherTree = Number(rows[rowIndex].split("")[colIndex - i]);
        if (otherTree >= currentTree) {
          break;
        }
      }

      let visibleRight = 0;
      for (let i = 1; colIndex + i < cells.length; i++) {
        visibleRight++;
        const otherTree = Number(rows[rowIndex].split("")[colIndex + i]);
        if (otherTree >= currentTree) {
          break;
        }
      }

      const scenicScore = visibleUp * visibleDown * visibleLeft * visibleRight;
      if (scenicScore > highestScenicScore) {
        highestScenicScore = scenicScore;
        return;
      }
    });
  });

  return highestScenicScore;
};

const run = async () => {
  const lines = (await readLines("inputs/2022/08.txt"))
    .filter((o) => o.length)
    .map((o) => o.trim());

  const part1 = countTreesVisibleFromOutside(lines);
  const part2 = getHighestScenicScore(lines);
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
