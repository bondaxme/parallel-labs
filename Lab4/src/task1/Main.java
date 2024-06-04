package task1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) throws IOException {

        String directoryPath = "C:\\Users\\maksb\\IdeaProjects\\parallel labs\\Lab4\\src\\books\\TEST2";

        // warm up
        System.out.println("Warmup started...");
        List<String> wordsWarmUp = FileProcessor.readFilesFromDirectory(directoryPath);
        WordLengthTask warmUpTask = new WordLengthTask(wordsWarmUp);
        ForkJoinPool warmUpPool = new ForkJoinPool(6);
        warmUpPool.invoke(warmUpTask);
        List<String> wordsSeq = FileProcessor.readFilesFromDirectorySequential(directoryPath);
        SequentialWordLengthCalculator.calculateAverageWordLength(wordsSeq);
        System.out.println("Warmup finished.");



        // parallel file read
        long startParallelRead = System.currentTimeMillis();
        List<String> wordsParallel = FileProcessor.readFilesFromDirectory(directoryPath);
        long endParallelRead = System.currentTimeMillis();

        System.out.println("Amount of words in files: " + wordsParallel.size());
        System.out.println("Amount of files: " + FileProcessor.getFilePaths(directoryPath).size());

        // parallel calculate
        long startParallelCalc = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        WordLengthTask parallelTask = new WordLengthTask(wordsParallel);
        WordLengthResult parallelResult = forkJoinPool.invoke(parallelTask);
        double parallelAverage = (double) parallelResult.totalLength / parallelResult.wordCount;
        long endParallelCalc = System.currentTimeMillis();

        // seq file read
        long startSequentialRead = System.currentTimeMillis();
        List<String> wordsSequential = FileProcessor.readFilesFromDirectorySequential(directoryPath);
        long endSequentialRead = System.currentTimeMillis();

        // seq calculate
        long startSequentialCalc = System.currentTimeMillis();
        double sequentialAverage = SequentialWordLengthCalculator.calculateAverageWordLength(wordsSequential);
        long endSequentialCalc = System.currentTimeMillis();

        System.out.println("Parallel Average Word Length: " + parallelAverage);
        System.out.println("Sequential Average Word Length: " + sequentialAverage);
        System.out.println("Parallel Read Time: " + (endParallelRead - startParallelRead) + " ms");
        System.out.println("Parallel Calculation Time: " + (endParallelCalc - startParallelCalc) + " ms");
        System.out.println("Sequential Read Time: " + (endSequentialRead - startSequentialRead) + " ms");
        System.out.println("Sequential Calculation Time: " + (endSequentialCalc - startSequentialCalc) + " ms");
    }
}
