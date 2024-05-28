package ServerStorage;

import java.io.Serializable;

public class MatrixMultiplicationRequest implements Serializable {
    private int numThreads;

    public MatrixMultiplicationRequest(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumThreads() {
        return numThreads;
    }
}
