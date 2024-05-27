import mpi.MPI;

import java.util.Random;

public class MatrixMultiplicationBlocking {
    static final int NRA = 1000; /* number of rows in matrix A */
    static final int NCA = 1000; /* number of columns in matrix A */
    static final int NCB = 1000; /* number of columns in matrix B */
    static final int MASTER = 0; /* taskid of first task */
    static final int FROM_MASTER = 1; /* setting a message type */
    static final int FROM_WORKER = 2; /* setting a message type */

    public static void main(String[] args) {
        int[][] a = new int[NRA][NCA];
        int[][] b = new int[NCA][NCB];
        int[][] c = new int[NRA][NCB];

        MPI.Init(args);
        int numtasks = MPI.COMM_WORLD.Size();
        int taskid = MPI.COMM_WORLD.Rank();
        int[] rows = {0};
        int[] offset = {0};

        if (numtasks < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.Finalize();
            System.exit(1);
        }

        int numworkers = numtasks - 1;

        if (taskid == MASTER) {
            System.out.println("mpi_mm has started with " + numtasks + " tasks.");

            Random random = new Random();

            // Initialize matrices A and B
            for (int i = 0; i < NRA; i++) {
                for (int j = 0; j < NCA; j++) {
                    a[i][j] = 10;
//                    a[i][j] = random.nextInt(10);
                }
            }
            for (int i = 0; i < NCA; i++) {
                for (int j = 0; j < NCB; j++) {
                    b[i][j] = 10;
//                    b[i][j] = random.nextInt(10);
                }
            }

            // Send matrix data to worker tasks
            int averow = NRA / numworkers;
            int extra = NRA % numworkers;

            long start = System.currentTimeMillis();

            for (int dest = 1; dest <= numworkers; dest++) {
                rows[0] = (dest <= extra) ? averow + 1 : averow;
                System.out.println("Sending " + rows[0] + " rows to task " + dest + " offset = " + offset[0]);

                MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Send(a, offset[0], rows[0], MPI.OBJECT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Send(b, 0, NCA, MPI.OBJECT, dest, FROM_MASTER);

                offset[0] += rows[0];
            }

            // Receive results from worker tasks
            for (int source = 1; source <= numworkers; source++) {
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, source, FROM_WORKER);
                MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, source, FROM_WORKER);
                MPI.COMM_WORLD.Recv(c, offset[0], rows[0], MPI.OBJECT, source, FROM_WORKER);

                System.out.println("Received results from task " + source);
            }

            long end = System.currentTimeMillis();

            // Print A and B
//            System.out.println("Matrix A:");
//            Utils.printMatrix(a);
//            System.out.println("\nMatrix B:");
//            Utils.printMatrix(b);
//            System.out.println("\nResult Matrix:");
//            Utils.printMatrix(c);
            System.out.println("\nDone.");
            System.out.println("Time: " + (end - start) + "ms");


            int cSeq[][] = new int[NRA][NCB];
            SequentialMultiplication.multiply(a, b, cSeq); // Sequential multiplication
            Utils.compareMatrices(c, cSeq); // Compare matrices
//            Utils.printMatrix(cSeq); // Print sequential result
        } else { // worker task
            MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER);
            MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, MASTER, FROM_MASTER);
            MPI.COMM_WORLD.Recv(a, 0, rows[0], MPI.OBJECT, MASTER, FROM_MASTER);
            MPI.COMM_WORLD.Recv(b, 0, NCA, MPI.OBJECT, MASTER, FROM_MASTER);

            for (int k = 0; k < NCB; k++) {
                for (int i = 0; i < rows[0]; i++) {
                    for (int j = 0; j < NCA; j++) {
                        c[i][k] += a[i][j] * b[j][k];
                    }
                }
            }

            MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER);
            MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, MASTER, FROM_WORKER);
            MPI.COMM_WORLD.Send(c, 0, rows[0], MPI.OBJECT, MASTER, FROM_WORKER);
        }

        MPI.Finalize();
    }
}
