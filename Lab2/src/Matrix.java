import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private final int[][] data;

    public Matrix(int rows, int cols) {
        data = new int[rows][cols];
    }

    public int get(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, int value) {
        data[row][col] = value;
    }

    public int getRows() {
        return data.length;
    }

    public int getCols() {
        return data[0].length;
    }

    public void print() {
        for (int[] row : data) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public static Matrix generateRandomMatrix(int rows, int cols, int bound) {
        Matrix matrix = new Matrix(rows, cols);
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, random.nextInt(bound));
            }
        }

        return matrix;
    }

    public Matrix getSubMatrix(int rowStart, int colStart, int blockSize) {
        Matrix subMatrix = new Matrix(blockSize, blockSize);
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                subMatrix.set(i, j, this.get(rowStart + i, colStart + j));
            }
        }
        return subMatrix;
    }

    public void setSubMatrix(Matrix subMatrix, int rowStart, int colStart) {
        for (int i = 0; i < subMatrix.getRows(); i++) {
            for (int j = 0; j < subMatrix.getCols(); j++) {
                this.set(rowStart + i, colStart + j, subMatrix.get(i, j));
            }
        }
    }

}