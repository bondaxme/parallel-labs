public class ParallelFox {
    private final int numThreads;
    private final int blockSize;
    private final Matrix A;
    private final Matrix B;
    private final Result result;

    public ParallelFox(Matrix A, Matrix B, int numThreads) {
        this.numThreads = numThreads;
        this.blockSize = A.getRows() / numThreads;
        this.A = A;
        this.B = B;
        this.result = new Result(A.getRows(), B.getCols());
    }

    private class WorkerThread extends Thread {
        private final int rowStart, rowEnd, colStart, colEnd;

        public WorkerThread(int rowStart, int rowEnd, int colStart, int colEnd) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.colStart = colStart;
            this.colEnd = colEnd;
        }

        @Override
        public void run() {
            for (int i = rowStart; i < rowEnd; i++) {
                for (int j = colStart; j < colEnd; j++) {
                    for (int k = 0; k < A.getCols(); k++) {
                        result.set(i, j, result.get(i, j) + A.get(i, k) * B.get(k, j));
                    }
                }
            }
        }
    }

    public Result multiply() throws InterruptedException {
        Thread[][] threads = new Thread[numThreads][numThreads];

        for (int i = 0; i < numThreads; i++) {
            for (int j = 0; j < numThreads; j++) {
                int rowStart = i * blockSize;
                int rowEnd = rowStart + blockSize;
                int colStart = j * blockSize;
                int colEnd = colStart + blockSize;

                threads[i][j] = new WorkerThread(rowStart, rowEnd, colStart, colEnd);
                threads[i][j].start();
            }
        }

        for (int i = 0; i < numThreads; i++) {
            for (int j = 0; j < numThreads; j++) {
                threads[i][j].join();
            }
        }

        return result;
    }
}