import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelFox {
    private final Matrix matrixA;
    private final Matrix matrixB;
    private final Result result;
    private final int blockSize;

    public ParallelFox(Matrix matrixA, Matrix matrixB, int numThreads) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = new Result(matrixA.getRows(), matrixB.getCols());
        this.blockSize = (int) Math.ceil((double) matrixA.getRows() / Math.sqrt(numThreads));
    }

    public Result multiply() throws InterruptedException {
        int numBlocks = (int) Math.ceil((double) matrixA.getRows() / blockSize);
        Thread[] threads = new Thread[numBlocks * numBlocks];
        int threadIndex = 0;

        for (int i = 0; i < matrixA.getRows(); i += blockSize) {
            for (int j = 0; j < matrixB.getCols(); j += blockSize) {
                threads[threadIndex] = new Thread(new FoxTask(i, j));
                threads[threadIndex].start();
                threadIndex++;
                System.out.println("Thread " + threadIndex + " is working on rows " + i + " to " + (i + blockSize) + " and columns " + j + " to " + (j + blockSize));
            }
        }

        for (Thread thread : threads) {
            if (thread != null) {
                thread.join();
            }
        }

        return result;
    }

    private class FoxTask implements Runnable {
        private final int row;
        private final int col;

        public FoxTask(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            for (int k = 0; k < matrixA.getRows(); k += blockSize) {
                Matrix blockA = getBlock(matrixA, row, k, blockSize);
                Matrix blockB = getBlock(matrixB, k, col, blockSize);
                Matrix blockC = multiplyBlocks(blockA, blockB);

                addBlockToResult(blockC, row, col);
            }
        }
    }

    private Matrix getBlock(Matrix matrix, int row, int col, int size) {
        int rows = Math.min(size, matrix.getRows() - row);
        int cols = Math.min(size, matrix.getCols() - col);
        Matrix block = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                block.set(i, j, matrix.get(row + i, col + j));
            }
        }

        return block;
    }

    private Matrix multiplyBlocks(Matrix blockA, Matrix blockB) {
        int rows = blockA.getRows();
        int cols = blockB.getCols();
        Matrix resultBlock = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                for (int k = 0; k < blockA.getCols(); k++) {
                    sum += blockA.get(i, k) * blockB.get(k, j);
                }
                resultBlock.set(i, j, sum);
            }
        }

        return resultBlock;
    }

    private synchronized void addBlockToResult(Matrix block, int row, int col) {
        for (int i = 0; i < block.getRows(); i++) {
            for (int j = 0; j < block.getCols(); j++) {
                result.set(row + i, col + j, result.get(row + i, col + j) + block.get(i, j));
            }
        }
    }
}
