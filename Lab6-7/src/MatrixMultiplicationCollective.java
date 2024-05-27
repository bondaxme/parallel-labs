import mpi.MPI;

import java.util.Random;

public class MatrixMultiplicationCollective {
    static final int SIZE = 1000;
    static final int MASTER = 0;

    public static void main(String[] args) {
        int[][] a = new int[SIZE][SIZE];
        int[][] b = new int[SIZE][SIZE];
        int[][] c = new int[SIZE][SIZE];
        long start = 0;

        MPI.Init(args);
        int numtasks = MPI.COMM_WORLD.Size();
        int taskid = MPI.COMM_WORLD.Rank();

        if (numtasks < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.Finalize();
            System.exit(1);
        }

        int slice = SIZE / numtasks;
        if (SIZE % numtasks != 0) {
            System.out.println("Matrix size must be divisible by the number of tasks. Quitting...");
            MPI.Finalize();
            System.exit(1);
        }

        if (taskid == MASTER) {
            System.out.println("mpi_mm has started with " + numtasks + " tasks.");

            Random random = new Random();

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    a[i][j] = 10;
//                    a[i][j] = random.nextInt(10);
                }
            }
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    b[i][j] = 10;
//                    b[i][j] = random.nextInt(10);

                }
            }
            start = System.currentTimeMillis();
        }

        int[][] aBuffer = new int[slice][SIZE];

        System.out.println("Task " + taskid + " is receiving " + slice + " rows");

        MPI.COMM_WORLD.Scatter(a, taskid * slice, slice, MPI.OBJECT, aBuffer, 0, slice, MPI.OBJECT, MASTER);
        MPI.COMM_WORLD.Bcast(b, 0, SIZE, MPI.OBJECT, MASTER);

        int[][] cBuffer = new int[slice][SIZE];

        for (int i = 0; i < slice; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    cBuffer[i][j] += aBuffer[i][k] * b[k][j];
                }
            }
        }

//        MPI.COMM_WORLD.Gather(cBuffer, 0, slice, MPI.OBJECT, c, taskid * slice, slice, MPI.OBJECT, MASTER);
        MPI.COMM_WORLD.Allgather(cBuffer, 0, slice, MPI.OBJECT, c, taskid * slice, slice, MPI.OBJECT);

        if (taskid == MASTER) {
            long end = System.currentTimeMillis();
//            System.out.println("Result matrix C:");
//            Utils.printMatrix(c);
            System.out.println("\nTime: " + (end - start) + "ms");

            int[][] cSequential = new int[SIZE][SIZE];
            SequentialMultiplication.multiply(a, b, cSequential);
//            System.out.println("\nSequential result matrix C:");
//            Utils.printMatrix(cSequential);
            Utils.compareMatrices(c, cSequential);
        }

        MPI.Finalize();
    }
}
