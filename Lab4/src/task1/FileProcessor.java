package task1;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class FileProcessor {
    public static List<String> readFilesFromDirectory(String directoryPath) throws IOException {
        List<Path> filePaths = getFilePaths(directoryPath);

        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        FileReadTask task = new FileReadTask(filePaths);
        return forkJoinPool.invoke(task);
    }

    public static List<String> readFilesFromDirectorySequential(String directoryPath) throws IOException {
        List<Path> filePaths = getFilePaths(directoryPath);

        List<String> words = new ArrayList<>();
        for (Path filePath : filePaths) {
            words.addAll(readAndProcessText(filePath));
        }
        return words;
    }

    public static List<Path> getFilePaths(String dirPath) throws IOException {
        return Files.walk(Paths.get(dirPath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .collect(Collectors.toList());
    }

    private static class FileReadTask extends RecursiveTask<List<String>> {
        private static final int THRESHOLD = 1;
        private List<Path> filePaths;

        public FileReadTask(List<Path> filePaths) {
            this.filePaths = filePaths;
        }

        @Override
        protected List<String> compute() {
            if (filePaths.size() <= THRESHOLD) {
                List<String> words = new ArrayList<>();
                for (Path filePath : filePaths) {
                    try {
                        words.addAll(readAndProcessText(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return words;
            } else {
                int mid = filePaths.size() / 2;
                FileReadTask task1 = new FileReadTask(filePaths.subList(0, mid));
                FileReadTask task2 = new FileReadTask(filePaths.subList(mid, filePaths.size()));
                invokeAll(task1, task2);
                List<String> result = new ArrayList<>(task1.join());
                result.addAll(task2.join());
                return result;
            }
        }
    }

    private static List<String> readAndProcessText(Path filePath) throws IOException {
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        content = content.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        return Arrays.asList(content.split("\\s+"));
    }
}
