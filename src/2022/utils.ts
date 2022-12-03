import * as fs from "node:fs/promises";

export const readLines = async (path: string) => {
  const buffer = await fs.readFile(path);
  const lines = buffer.toString("utf-8").split("\n");

  if (lines[lines.length - 1].length === 0)
    return lines.slice(0, lines.length - 1);

  return lines;
};
