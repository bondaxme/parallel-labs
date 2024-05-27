public class Utils {
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.println();
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
        }
    }

    public static void compareMatrices(int[][] a, int[][] b) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] != b[i][j]) {
                    System.out.println("\nMatrices are not equal");
                    return;
                }
            }
        }
        System.out.println("\nMatrices are equal");
    }
}
