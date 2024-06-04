package task2;

import java.util.concurrent.RecursiveTask;

public class MatrixMultiplierTask extends RecursiveTask<Result> {
    private static final int THRESHOLD = 4;
    private final Matrix A;
    private final Matrix B;
    private final Result result;
    private final int startRow;
    private final int endRow;

    public MatrixMultiplierTask(Matrix A, Matrix B, Result result, int startRow, int endRow) {
        this.A = A;
        this.B = B;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected Result compute() {
        int rowsA = A.getRows();
        int colsA = A.getCols();
        int colsB = B.getCols();

        if (endRow - startRow <= THRESHOLD) {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < colsB; j++) {
                    int sum = 0;
                    for (int k = 0; k < colsA; k++) {
                        sum += A.get(i, k) * B.get(k, j);
                    }
                    result.set(i, j, sum);
                }
            }
        } else {
            int midRow = (startRow + endRow) / 2;
            invokeAll(new MatrixMultiplierTask(A, B, result, startRow, midRow), new MatrixMultiplierTask(A, B, result, midRow, endRow));
        }

        return result;
    }
}