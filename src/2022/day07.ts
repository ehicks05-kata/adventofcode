import { min, partition, sum, sumBy } from "lodash";
import { readLines } from "./utils";

interface Dir {
  children: (Dir | File)[];
  name: string;
  size?: number;
}

interface File {
  name: string;
  size: number;
}

/**
 * Stamp each dir with a size, where size is the sum of direct child files,
 * plus the size of each child dir.
 */
const calculateDirSizes = (dir: Dir): number => {
  const [dirs, files] = partition(dir.children, (o) => "children" in o) as [
    Dir[],
    File[]
  ];
  const localSize = sumBy(files, (o) => o.size);
  const childrenSizes = dirs.map((o) => calculateDirSizes(o));
  const totalSize = localSize + sum(childrenSizes);
  dir.size = totalSize;
  return totalSize;
};

/**
 * Sum the size of all dirs smaller than sizeLimit. Files may be counted
 * multiple times.
 */
const sumDirSizes = (dir: Dir, sizeLimit: number): number => {
  const dirs = dir.children.filter((o) => "children" in o) as Dir[];
  const childrenSize = sum(dirs.map((o) => sumDirSizes(o, sizeLimit)));
  return dir.size! <= sizeLimit ? childrenSize + dir.size! : childrenSize;
};

/**
 * Find smallest dir with size >= sizeLimit
 */
const findSmallestDir = (
  dir: Dir,
  sizeLimit: number,
  bestMatch = Number.MAX_SAFE_INTEGER
): number => {
  const dirs = dir.children.filter((o) => "children" in o) as Dir[];
  const childSizes = dirs.map((o) => findSmallestDir(o, sizeLimit, bestMatch));
  const smallestValidSize = min(
    [...childSizes, dir.size!].filter((o) => o >= sizeLimit)
  );
  if (!smallestValidSize || smallestValidSize >= bestMatch) return bestMatch;
  console.log(smallestValidSize);
  return smallestValidSize;
};

const parseLsOutput = (items: string[]) => {
  return items.map((o) => {
    if (o.startsWith("dir")) {
      const [, name] = o.split(" ");
      const dir: Dir = { children: [], name };
      return dir;
    } else {
      const [size, name] = o.split(" ");
      const file: File = { name, size: Number(size) };
      return file;
    }
  });
};

const processCommand = (
  root: Dir,
  cwd: string[],
  command: string,
  args?: string,
  output?: string[]
) => {
  if (command === "cd") {
    switch (args) {
      case "/": {
        while (cwd.length > 1) cwd.pop();
        break;
      }
      case "..": {
        cwd.pop();
        break;
      }
      default: {
        if (args) cwd.push(args);
      }
    }
  }
  if (command === "ls") {
    let node = root;
    const wd = [...cwd];
    wd.shift();
    while (wd.length) {
      const currentDir = wd.shift();
      const child = node.children.find((o) => o.name === currentDir);
      if (!child || "size" in child) throw new Error("invalid path");
      node = child;
    }

    const childrenForNode = parseLsOutput(output || []);
    node.children = childrenForNode;
  }
};

const parse = (lines: string[]) => {
  const root: Dir = { children: [], name: "/" };
  const cwd = ["/"];

  while (lines.length) {
    const line = lines.shift()!;
    const [_prompt, command, args] = line.split(" ");

    const output = [];
    while (lines.length && !lines[0].startsWith("$")) {
      output.push(lines[0]);
      lines.shift();
    }
    processCommand(root, cwd, command, args, output);
  }

  return root;
};

const run = async () => {
  const lines = await readLines("inputs/2022/07.txt");

  const root = parse(lines);
  const totalSize = calculateDirSizes(root);

  const part1 = sumDirSizes(root, 100_000);

  const spaceToFreeUp = 70_000_000 - totalSize;
  console.log({ totalSize, spaceToFreeUp });
  const part2 = findSmallestDir(root, spaceToFreeUp);
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
