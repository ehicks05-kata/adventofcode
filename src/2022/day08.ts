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
    rowIndex === rows.length - 1 ||
    colIndex === 0 ||
    colIndex === cols.length - 1
  );
};

const countVisibleTrees = (rows: string[]) => {
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

      if (rowIndex === 1 && colIndex === 9) {
        console.log({
          rowIndex,
          colIndex,
          currentTree,
          visibleFromTop,
          visibleFromBottom,
          visibleFromLeft,
          visibleFromRight,
        });
      }

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

const run = async () => {
  const lines = (await readLines("inputs/2022/08.txt")).filter((o) => o.length);

  const part1 = countVisibleTrees(lines);
  const part2 = 0;
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
