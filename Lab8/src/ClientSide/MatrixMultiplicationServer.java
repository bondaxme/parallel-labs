package ClientSide;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MatrixMultiplicationServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // Read the request from client
            MatrixMultiplicationRequest request = (MatrixMultiplicationRequest) input.readObject();
            int rowsA = request.getRowsA();
            int colsA = request.getColsA();
            int colsB = request.getColsB();
            int numThreads = request.getNumThreads();

            // Generate matrices on the server
            Matrix A = new Matrix(rowsA, colsA);
            Matrix B = new Matrix(colsA, colsB);
            A.fillRandom();
            B.fillRandom();

            // Perform matrix multiplication
            Result result = StripedParallel.multiply(A, B, numThreads);

            // Send the result back to the client
            output.writeObject(result);

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

