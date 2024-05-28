package ClientSide;

import java.io.Serializable;

public class MatrixMultiplicationRequest implements Serializable {
    private int rowsA;
    private int colsA;
    private int colsB;
    private int numThreads;

    public MatrixMultiplicationRequest(int rowsA, int colsA, int colsB, int numThreads) {
        this.rowsA = rowsA;
        this.colsA = colsA;
        this.colsB = colsB;
        this.numThreads = numThreads;
    }

    public int getRowsA() {
        return rowsA;
    }

    public int getColsA() {
        return colsA;
    }

    public int getColsB() {
        return colsB;
    }

    public int getNumThreads() {
        return numThreads;
    }
}
