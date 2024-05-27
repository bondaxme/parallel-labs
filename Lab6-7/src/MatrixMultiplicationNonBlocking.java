import mpi.MPI;
import mpi.Request;
import mpi.Status;

import java.util.Random;

public class MatrixMultiplicationNonBlocking {
    static final int NRA = 2000; /* number of rows in matrix A */
    static final int NCA = 2000; /* number of columns in matrix A */
    static final int NCB = 2000; /* number of columns in matrix B */
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
//                    a[i][j] = random.nextInt() * 10;
                }
            }
            for (int i = 0; i < NCA; i++) {
                for (int j = 0; j < NCB; j++) {
                    b[i][j] = 10;
//                    b[i][j] = random.nextInt() * 10;
                }
            }

            // Send matrix data to worker tasks
            int averow = NRA / numworkers;
            int extra = NRA % numworkers;

            long start = System.currentTimeMillis();

            for (int dest = 1; dest <= numworkers; dest++) {
                rows[0] = (dest <= extra) ? averow + 1 : averow;
                System.out.println("Sending " + rows[0] + " rows to task " + dest + " offset = " + offset[0]);

                MPI.COMM_WORLD.Isend(b, 0, NCA, MPI.OBJECT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Isend(offset, 0, 1, MPI.INT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Isend(rows, 0, 1, MPI.INT, dest, FROM_MASTER);
                MPI.COMM_WORLD.Isend(a, offset[0], rows[0], MPI.OBJECT, dest, FROM_MASTER);

                offset[0] += rows[0];
            }

            // Receive results from worker tasks
            for (int source = 1; source <= numworkers; source++) {
                MPI.COMM_WORLD.Irecv(offset, 0, 1, MPI.INT, source, FROM_WORKER).Wait();
                MPI.COMM_WORLD.Irecv(rows, 0, 1, MPI.INT, source, FROM_WORKER).Wait();
                MPI.COMM_WORLD.Irecv(c, offset[0], rows[0], MPI.OBJECT, source, FROM_WORKER).Wait();
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

            // Sequential multiplication
            int[][] cSeq = new int[NRA][NCB];
            SequentialMultiplication.multiply(a, b, cSeq);
            Utils.compareMatrices(c, cSeq);
        } else { // worker task
            Request getB = MPI.COMM_WORLD.Irecv(b, 0, NCA, MPI.OBJECT, MASTER, FROM_MASTER);

            MPI.COMM_WORLD.Irecv(offset, 0, 1, MPI.INT, MASTER, FROM_MASTER).Wait();
            MPI.COMM_WORLD.Irecv(rows, 0, 1, MPI.INT, MASTER, FROM_MASTER).Wait();

            MPI.COMM_WORLD.Isend(offset, 0, 1, MPI.INT, MASTER, FROM_WORKER);
            MPI.COMM_WORLD.Isend(rows, 0, 1, MPI.INT, MASTER, FROM_WORKER);

            MPI.COMM_WORLD.Irecv(a, 0, rows[0], MPI.OBJECT, MASTER, FROM_MASTER).Wait();

            getB.Wait();
            for (int k = 0; k < NCB; k++) {
                for (int i = 0; i < rows[0]; i++) {
                    for (int j = 0; j < NCA; j++) {
                        c[i][k] += a[i][j] * b[j][k];
                    }
                }
            }

            MPI.COMM_WORLD.Isend(c, 0, rows[0], MPI.OBJECT, MASTER, FROM_WORKER);
        }

        MPI.Finalize();
    }
}
