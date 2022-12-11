import { readLines } from "./utils";

const PACKET_MARKER_LENGTH = 4;
const MESSAGE_MARKER_LENGTH = 14;

const getDistinctCharSequenceIndex = (line: string, sequenceLength: number) => {
  for (let i = 0; i < line.length - 1 - sequenceLength; i++) {
    const window = line.slice(i, i + sequenceLength);
    if (window.length === new Set(window).size) {
      console.log(`${window}, ends at i=${i + sequenceLength}`);
      return i + sequenceLength;
    }
  }
  return -1;
};

const run = async () => {
  const lines = await readLines("inputs/2022/06.txt");

  const startOfPacketIndex = getDistinctCharSequenceIndex(
    lines[0],
    PACKET_MARKER_LENGTH
  );
  const startOfMessageIndex = getDistinctCharSequenceIndex(
    lines[0],
    MESSAGE_MARKER_LENGTH
  );
  const part1 = startOfPacketIndex;
  const part2 = startOfMessageIndex;
  console.log(`part1: ${part1}`);
  console.log(`part2: ${part2}`);
};

run();
