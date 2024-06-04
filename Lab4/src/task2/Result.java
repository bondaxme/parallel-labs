package task2;

public class Result {
    private final int[][] data;

    public Result(int rows, int cols) {
        data = new int[rows][cols];
    }

    public synchronized void set(int row, int col, int value) {
        data[row][col] = value;
    }

    public int get(int row, int col) {
        return data[row][col];
    }

    public int getRows() {
        return data.length;
    }

    public int getCols() {
        return data[0].length;
    }

    public void print() {
        for (int[] row : data) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public boolean equals(Result other) {
        if (this.data.length != other.data.length) return false;
        if (this.data[0].length != other.data[0].length) return false;

        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[i].length; j++) {
                if (this.data[i][j] != other.data[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}