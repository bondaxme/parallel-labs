import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of threads: ");
        int numThreads = scanner.nextInt();


        int rows = 1000;
        int cols = 1000;
        int bound = 10;

        if (Math.sqrt(numThreads) % 1 != 0) {
            System.out.println("The number of threads must be a perfect square.");
            return;
        }

        Matrix A = Matrix.generateRandomMatrix(rows, cols, bound);
        Matrix B = Matrix.generateRandomMatrix(rows, cols, bound);

//        System.out.println("Matrix A:");
//        A.print();
//        System.out.println("Matrix B:");
//        B.print();

        if (A.getRows() < numThreads) {
            System.out.println("Number of rows in matrix A must be greater than or equal to the number of threads.");
            return;
        }

//        long startTime = System.currentTimeMillis();
//        Result sequentialResult = SequentialMultiplication.multiply(A, B);
//        long endTime = System.currentTimeMillis();
//        System.out.println("Sequential multiplication took " + (endTime - startTime) + " milliseconds.");

        long startTime2 = System.currentTimeMillis();
        Result parallelResult = StripedParallel.multiply(A, B, numThreads);
        long endTime2 = System.currentTimeMillis();
        System.out.println("Parallel multiplication took " + (endTime2 - startTime2) + " milliseconds.");

        long startTime3 = System.currentTimeMillis();
        ParallelFox parallelFox = new ParallelFox(A, B, numThreads);
        Result parallelFoxResult = parallelFox.multiply();
        long endTime3 = System.currentTimeMillis();
        System.out.println("Parallel Fox multiplication took " + (endTime3 - startTime3) + " milliseconds.");

//
//        System.out.println("Sequential Result:");
//        sequentialResult.print();
//        System.out.println("Parallel Result:");
//        parallelResult.print();

//        if (sequentialResult.equals(parallelResult)) {
//            System.out.println("The results of striped are identical.");
//        } else {
//            System.out.println("The results differ.");
//        }
//
//        if (sequentialResult.equals(parallelFoxResult)) {
//            System.out.println("The results of Fox are identical.");
//        } else {
//            System.out.println("The results differ.");
//        }
    }
}