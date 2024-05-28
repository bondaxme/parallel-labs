package ClientSide;

import java.io.*;
import java.net.Socket;

public class MatrixMultiplicationClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Параметри матриць і кількість потоків
            int rowsA = 100;
            int colsA = 100;
            int colsB = 100;
            int numThreads = 2;

            // Відправка запиту на сервер для генерації матриць та їх множення
            MatrixMultiplicationRequest request = new MatrixMultiplicationRequest(rowsA, colsA, colsB, numThreads);

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
