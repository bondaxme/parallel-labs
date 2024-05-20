public class ParallelFox {
    private final Matrix A;
    private final Matrix B;
    private final Result result;
    private final int numThreads;
    private final int blockSize;

    public ParallelFox(Matrix A, Matrix B, int numThreads) {
        this.A = A;
        this.B = B;
        this.result = new Result(A.getRows(), B.getCols());
        this.numThreads = numThreads;
        this.blockSize = A.getRows() / (int) Math.sqrt(numThreads);
    }

    private class WorkerThread extends Thread {
        private final int rowStart;
        private final int colStart;

        public WorkerThread(int rowStart, int colStart) {
            this.rowStart = rowStart;
            this.colStart = colStart;
        }

        @Override
        public void run() {
            for (int l = 0; l < numThreads; l++) {
                int k = (colStart + l) % numThreads;
                Matrix A_block = A.getSubMatrix(rowStart, k * blockSize, blockSize);
                Matrix B_block = B.getSubMatrix(k * blockSize, colStart, blockSize);

                for (int i = 0; i < blockSize; i++) {
                    for (int j = 0; j < blockSize; j++) {
                        int sum = 0;
                        for (int m = 0; m < blockSize; m++) {
                            sum += A_block.get(i, m) * B_block.get(m, j);
                        }
                        synchronized (result) {
                            result.set(rowStart + i, colStart + j, result.get(rowStart + i, colStart + j) + sum);
                        }
                    }
                }
            }

            // Обробка залишкових рядків і стовпців
            int remainder = A.getRows() % blockSize;
            if (remainder > 0) {
                for (int i = rowStart; i < rowStart + remainder; i++) {
                    for (int j = colStart; j < colStart + remainder; j++) {
                        int sum = 0;
                        for (int k = 0; k < A.getCols(); k++) {
                            sum += A.get(i, k) * B.get(k, j);
                        }
                        synchronized (result) {
                            result.set(i, j, result.get(i, j) + sum);
                        }
                    }
                }
            }
        }
    }

    public Result multiply() throws InterruptedException {
        int sqrtThreads = (int) Math.sqrt(numThreads);
        Thread[] threads = new Thread[numThreads];

        int threadIndex = 0;
        for (int i = 0; i < sqrtThreads; i++) {
            for (int j = 0; j < sqrtThreads; j++) {
                threads[threadIndex] = new WorkerThread(i * blockSize, j * blockSize);
                threads[threadIndex].start();
                threadIndex++;
            }
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return result;
    }
}