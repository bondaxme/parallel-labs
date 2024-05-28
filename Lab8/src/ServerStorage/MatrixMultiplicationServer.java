package ServerStorage;

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

            int size = 1500;

            MatrixMultiplicationRequest request = (MatrixMultiplicationRequest) input.readObject();
            int numThreads = request.getNumThreads();

            Matrix A = new Matrix(size, size);
            Matrix B = new Matrix(size, size);
            A.fillRandom();
            B.fillRandom();

            Result result = StripedParallel.multiply(A, B, numThreads);

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

