import java.util.Random;

public class SequentialMultiplication {

    static final int NRA = 1000; /* number of rows in matrix A */
    static final int NCA = 1000; /* number of columns in matrix A */
    static final int NCB = 1000; /* number of columns in matrix B */

    public static void main(String[] args) {
        int[][] a = new int[NRA][NCA];
        int[][] b = new int[NCA][NCB];
        int[][] c = new int[NRA][NCB];

        for (int i = 0; i < NRA; i++) {
            for (int j = 0; j < NCA; j++) {
                a[i][j] = 10;
            }
        }
        for (int i = 0; i < NCA; i++) {
            for (int j = 0; j < NCB; j++) {
                b[i][j] = 10;
            }
        }

        long start = System.currentTimeMillis();
        multiply(a, b, c);
        long end = System.currentTimeMillis();

        System.out.println("Matrix A:");
        Utils.printMatrix(a);
        System.out.println("\nMatrix B:");
        Utils.printMatrix(b);
        System.out.println("\nResult matrix C:");
        Utils.printMatrix(c);

        System.out.println("\nSequential multiplication took " + (end - start) + " ms");
    }

    public static void multiply(int[][] a, int[][] b, int[][] c) {
        for (int i = 0; i < NRA; i++) {
            for (int j = 0; j < NCB; j++) {
                for (int k = 0; k < NCA; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
    }
}
