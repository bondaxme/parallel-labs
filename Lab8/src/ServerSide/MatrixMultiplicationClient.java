package ServerSide;

import java.io.*;
import java.net.Socket;

public class MatrixMultiplicationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Генерація матриць на стороні клієнта
            Matrix A = new Matrix(100, 100);
            Matrix B = new Matrix(100, 100);
            A.fillRandom();
            B.fillRandom();
            int numThreads = 2;

            // Відправка запиту на сервер
            MatrixMultiplicationRequest request = new MatrixMultiplicationRequest(A, B, numThreads);

            long startTime = System.currentTimeMillis();
            output.writeObject(request);

            // Отримання результату від сервера
            Result result = (Result) input.readObject();
            long endTime = System.currentTimeMillis();

            // Вивід результату
            printMatrix(result.getData());

            // Вивід часу виконання
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
