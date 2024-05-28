
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
                System.out.println("Thread " + threadIndex + " is working on rows " + i + " to " + (i + blockSize) + " and columns " + j + " to " + (j + blockSize));
                threadIndex++;
            }
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                Result blockC = SequentialMultiplication.multiply(blockA, blockB);

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

    private synchronized void addBlockToResult(Result block, int row, int col) {
        for (int i = 0; i < block.getRows(); i++) {
            for (int j = 0; j < block.getCols(); j++) {
                result.set(row + i, col + j, result.get(row + i, col + j) + block.get(i, j));
            }
        }
    }
}
