package task4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class DocumentSearchTask extends RecursiveTask<List<Result>> {
    private final Path directory;
    private final String[] keywords;

    DocumentSearchTask(Path directory, String[] keywords) {
        this.directory = directory;
        this.keywords = keywords;
    }

    @Override
    public List<Result> compute() {
        List<Result> matchingFiles = new ArrayList<>();
        List<DocumentSearchTask> subTasks = new ArrayList<>();

        try {
            Files.list(directory).forEach(path -> {
                if (Files.isDirectory(path)) {
                    DocumentSearchTask task = new DocumentSearchTask(path, keywords);
                    task.fork();
                    subTasks.add(task);
                } else {
                    List<String> foundKeywords = searchFile(path, keywords);
                    if (!foundKeywords.isEmpty()) {
                        matchingFiles.add(new Result(path, foundKeywords));
                    }
                }
            });

            for (DocumentSearchTask task : subTasks) {
                matchingFiles.addAll(task.join());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matchingFiles;
    }

    private List<String> searchFile(Path file, String[] keywords) {
        List<String> foundKeywords = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(file));
            for (String keyword : keywords) {
                if (content.contains(keyword)) {
                    foundKeywords.add(keyword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundKeywords;
    }
}
