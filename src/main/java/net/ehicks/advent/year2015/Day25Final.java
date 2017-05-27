package net.ehicks.advent.year2015;

public class Day25Final
{
    public static void main(String[] args)
    {
        int goalRow = 2947;
        int goalCol = 3029;
        long answer = 0;

        Cell previous = new Cell(1, 20151125, 1, 1);
        for (int i = 0; i < 100_000_000; i++)
        {
            Cell cell = Cell.getNext(previous);

            if (cell.value < 0)
                System.out.println("shit");

            if (cell.row == goalRow && cell.col == goalCol)
            {
                answer = cell.value;
                break;
            }
            previous = cell;
        }


        System.out.println(answer);
    }

    private static class Cell
    {
        int index;
        long value;
        int row;
        int col;

        public Cell(int index, long value, int row, int col)
        {
            this.index = index;
            this.value = value;
            this.row = row;
            this.col = col;
        }

        static Cell getNext(Cell previous)
        {
            long value = (previous.value * 252533) % 33554393;

            int row;
            int col;
            if (previous.row == 1)
            {
                row = previous.col + 1;
                col = 1;
            }
            else
            {
                row = previous.row - 1;
                col = previous.col + 1;
            }
            return new Cell(previous.index + 1, value, row, col);
        }
    }
}
