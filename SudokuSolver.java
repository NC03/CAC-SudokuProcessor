public class SudokuSolver {
    private int[][] boxGrid = new int[9][9];

    public int[][] getBoard() {
        return boxGrid;
    }

    @Override
    public String toString() {
        String out = "";
        for (int d = 0; d < boxGrid.length; d++) {
            for (int e = 0; e < boxGrid[d].length; e++) {
                out += (boxGrid[d][e] + " ");
            }
            out += "\n";
        }
        return out;
    }

    public SudokuSolver(int[][] bg) {
        boxGrid = bg;
        solveSudoku();
    }

    public boolean checkRow(int col, int num) {
        for (int i = 0; i < boxGrid.length; i++) {
            if (boxGrid[i][col] == num) {
                return false;
            }
        }
        return true;
    }

    public boolean checkColumn(int row, int num) {
        for (int i = 0; i < boxGrid.length; i++) {
            if (boxGrid[row][i] == num) {
                return false;
            }
        }
        return true;
    }

    public boolean checkBox(int row, int col, int num) {
        int startRow = row - (row % 3);
        int startCol = col - (col % 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boxGrid[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean doesWork(int row, int col, int num) {
        if (checkRow(col, num) && checkColumn(row, num) && checkBox(row, col, num)) {
            return true;
        }
        return false;
    }

    public boolean solveSudoku() {
        boolean isDone = false;
        for (int i = 0; i < boxGrid.length; i++) {
            for (int j = 0; j < boxGrid[i].length; j++) {
                if (boxGrid[i][j] == 0) {
                    for (int n = 1; n <= 9; n++) {
                        if (doesWork(i, j, n)) {
                            boxGrid[i][j] = n;
                            if (solveSudoku()) {
                                return true;
                            } else {
                                boxGrid[i][j] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        isDone = true;
        for (int i = 0; i < boxGrid.length; i++) {
            for (int j = 0; j < boxGrid[i].length; j++) {
                if (boxGrid[i][j] == 0) {
                    isDone = false;
                }
            }
        }
        if (isDone == true) {
            return true;
        }
        return false;
    }
}
