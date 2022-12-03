import { sumBy } from "lodash";
import { readLines } from "./utils";

const MOVE_INPUTS = {
  A: 0,
  X: 0,
  B: 1,
  Y: 1,
  C: 2,
  Z: 2,
} as const;
type MoveInput = keyof typeof MOVE_INPUTS;

const MOVES = {
  0: 0,
  1: 1,
  2: 2,
} as const;
type Move = keyof typeof MOVES;

const POINTS = {
  0: 1,
  1: 2,
  2: 3,
  DRAW: 3,
  WIN: 6,
} as const;

const resolveRound = (p1Move: Move, p2Move: Move) => {
  const [p1MovePoints, p2MovePoints] = [POINTS[p1Move], POINTS[p2Move]];

  if (p1Move === p2Move) return p1MovePoints + POINTS.DRAW;

  const winner =
    (p1Move === 1 && p2Move === 0) ||
    (p1Move === 2 && p2Move === 1) ||
    (p1Move === 0 && p2Move === 2)
      ? 0
      : 1;

  const p1WinPoints = winner === 0 ? POINTS.WIN : 0;
  return p1MovePoints + p1WinPoints;
};

const processLinePart1 = (line: string) => {
  const [player1, player2] = line.split(" ");
  return resolveRound(
    MOVE_INPUTS[player2 as MoveInput],
    MOVE_INPUTS[player1 as MoveInput]
  );
};

// PART 2

const OUTCOMES = {
  lose: "lose",
  draw: "draw",
  win: "win",
} as const;
const INPUT_TO_OUTCOMES = {
  X: OUTCOMES.lose,
  Y: OUTCOMES.draw,
  Z: OUTCOMES.win,
} as const;

const getRespondingMove = (move: Move, outcome: Outcome) => {
  if (outcome === "draw") return move;
  if (outcome === "win") {
    if (move === 0) return 1;
    if (move === 1) return 2;
    if (move === 2) return 0;
  }
  if (outcome === "lose") {
    if (move === 0) return 2;
    if (move === 1) return 0;
    if (move === 2) return 1;
  }
  throw new Error(`couldn't get responding move`);
};

type OutcomeInput = keyof typeof INPUT_TO_OUTCOMES;
type Outcome = keyof typeof OUTCOMES;

const resolveRoundPart2 = (opponentMove: Move, outcome: Outcome) => {
  const ourMove = getRespondingMove(opponentMove, outcome);
  const movePoints = POINTS[ourMove];
  const outcomePoints = outcome === "win" ? 6 : outcome === "draw" ? 3 : 0;
  return movePoints + outcomePoints;
};

const processLinePart2 = (line: string) => {
  const [opponent, outcome] = line.split(" ");
  return resolveRoundPart2(
    MOVE_INPUTS[opponent as MoveInput],
    INPUT_TO_OUTCOMES[outcome as OutcomeInput]
  );
};

const run = async () => {
  const lines = await readLines("inputs/2022/02.txt");
  const part1 = sumBy(lines, processLinePart1);
  console.log(`part1: ${part1}`);
  const part2 = sumBy(lines, processLinePart2);
  console.log(`part2: ${part2}`);
};

run();
