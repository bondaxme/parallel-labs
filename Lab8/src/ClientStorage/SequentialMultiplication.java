package ClientStorage;

public class SequentialMultiplication {
    public static Result multiply(Matrix A, Matrix B) {
        int rowsA = A.getRows();
        int colsA = A.getCols();
        int colsB = B.getCols();

        Result result = new Result(rowsA, colsB);

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += A.get(i, k) * B.get(k, j);
                }
                result.set(i, j, sum);
            }
        }

        return result;
    }
}