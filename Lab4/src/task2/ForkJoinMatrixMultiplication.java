package task2;

import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinMatrixMultiplication {

    public static Result multiply(Matrix A, Matrix B, int numThreads) {
        ForkJoinPool pool = new ForkJoinPool(numThreads);
        Result result = new Result(A.getRows(), B.getCols());
        MatrixMultiplierTask task = new MatrixMultiplierTask(A, B, result, 0, A.getRows());
        pool.invoke(task);
        return result;
    }

    public static void main(String[] args) throws InterruptedException {

        int numThreads = 6;
        int rows = 1000;
        int cols = 1000;
        int bound = 10;

        Matrix A = Matrix.generateRandomMatrix(rows, cols, bound);
        Matrix B = Matrix.generateRandomMatrix(rows, cols, bound);

        if (A.getRows() < numThreads) {
            System.out.println("Number of rows in matrix A must be greater than or equal to the number of threads.");
            return;
        }

        // warmup
        System.out.println("Warmup started...");

        StripedParallel.multiply(A, B, numThreads);
        ForkJoinMatrixMultiplication.multiply(A, B, numThreads);

        System.out.println("Warmup finished.");


        long startTime2 = System.currentTimeMillis();
        Result parallelResult2 = StripedParallel.multiply(A, B, numThreads);
        long endTime2 = System.currentTimeMillis();

        System.out.println("Striped Parallel multiplication took " + (endTime2 - startTime2) + " milliseconds.");

        long startTime = System.currentTimeMillis();
        Result parallelResult = ForkJoinMatrixMultiplication.multiply(A, B, numThreads);
        long endTime = System.currentTimeMillis();

        System.out.println("ForkJoin Parallel multiplication took " + (endTime - startTime) + " milliseconds.");


        System.out.println("Results are equal: " + parallelResult.equals(parallelResult2));
    }
}
