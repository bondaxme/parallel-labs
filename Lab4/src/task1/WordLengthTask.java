package task1;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class WordLengthTask extends RecursiveTask<WordLengthResult> {
    private static final int THRESHOLD = 1000;
    private List<String> words;

    public WordLengthTask(List<String> words) {
        this.words = words;
    }

    @Override
    protected WordLengthResult compute() {
        if (words.size() <= THRESHOLD) {
            long totalLength = words.stream().mapToInt(String::length).sum();
            return new WordLengthResult(totalLength, words.size());
        } else {
            int mid = words.size() / 2;
            WordLengthTask task1 = new WordLengthTask(words.subList(0, mid));
            WordLengthTask task2 = new WordLengthTask(words.subList(mid, words.size()));
            invokeAll(task1, task2);
            WordLengthResult result1 = task1.join();
            WordLengthResult result2 = task2.join();
            return new WordLengthResult(result1.totalLength + result2.totalLength, result1.wordCount + result2.wordCount);
        }
    }
}
