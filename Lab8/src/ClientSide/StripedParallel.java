package ClientSide;

public class StripedParallel {
    public static Result multiply(Matrix A, Matrix B, int numThreads) throws InterruptedException {
        int rowsA = A.getRows();
        int colsA = A.getCols();
        int colsB = B.getCols();

        Result result = new Result(rowsA, colsB);
        Thread[] threads = new Thread[numThreads];

        int rowsPerThread = rowsA / numThreads;

        for (int t = 0; t < numThreads; t++) {
            final int startRow = t * rowsPerThread;
            final int endRow = (t == numThreads - 1) ? rowsA : startRow + rowsPerThread;

            threads[t] = new Thread(() -> {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < colsB; j++) {
                        int sum = 0;
                        for (int k = 0; k < colsA; k++) {
                            sum += A.get(i, k) * B.get(k, j);
                        }
                        result.set(i, j, sum);
                    }
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return result;
    }
}
