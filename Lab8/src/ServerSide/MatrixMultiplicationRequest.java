package ServerSide;

import java.io.Serializable;

public class MatrixMultiplicationRequest implements Serializable {
    private Matrix matrixA;
    private Matrix matrixB;
    private int numThreads;

    public MatrixMultiplicationRequest(Matrix matrixA, Matrix matrixB, int numThreads) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.numThreads = numThreads;
    }

    public Matrix getMatrixA() {
        return matrixA;
    }

    public Matrix getMatrixB() {
        return matrixB;
    }

    public int getNumThreads() {
        return numThreads;
    }
}

