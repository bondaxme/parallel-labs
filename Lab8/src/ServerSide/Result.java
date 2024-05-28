package ServerSide;

import java.io.Serializable;

public class Result implements Serializable {
    private int[][] data;

    public Result(int rows, int cols) {
        data = new int[rows][cols];
    }

    public void set(int row, int col, int value) {
        data[row][col] = value;
    }

    public int[][] getData() {
        return data;
    }
}

