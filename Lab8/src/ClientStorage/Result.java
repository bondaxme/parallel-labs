package ClientStorage;

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


    public boolean areEqual(Result other) {
        if (data.length != other.getData().length) {
            return false;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i].length != other.getData()[i].length) {
                return false;
            }
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != other.getData()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}

