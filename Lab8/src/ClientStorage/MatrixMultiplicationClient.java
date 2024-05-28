package ClientStorage;

import java.io.*;
import java.net.Socket;

public class MatrixMultiplicationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            int numThreads = 6;

            long startTime = System.currentTimeMillis();

            int size = 500;

            Matrix A = new Matrix(size, size);
            Matrix B = new Matrix(size, size);
            A.fillRandom();
            B.fillRandom();

            MatrixMultiplicationRequest request = new MatrixMultiplicationRequest(A, B, numThreads);

            output.writeObject(request);

            Result result = (Result) input.readObject();
            long endTime = System.currentTimeMillis();

//            printMatrix(result.getData());

            Result result_seq = SequentialMultiplication.multiply(A, B);

            System.out.println("Client Storage");

            System.out.println("Matrix size: " + result.getData().length + "x" + result.getData()[0].length);

            System.out.println("Are results equal: " + result_seq.areEqual(result));



            System.out.println("Time taken: " + (endTime - startTime) + " ms");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
