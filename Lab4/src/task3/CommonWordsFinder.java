package task3;

import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class CommonWordsFinder {

    private static class CommonWordsTask extends RecursiveTask<Set<String>> {
        private final List<String> documents;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 1;

        public CommonWordsTask(List<String> documents, int start, int end) {
            this.documents = documents;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Set<String> compute() {
            if (end - start <= THRESHOLD) {
                return findCommonWords(documents.subList(start, end));
            } else {
                int mid = (start + end) / 2;
                CommonWordsTask task1 = new CommonWordsTask(documents, start, mid);
                CommonWordsTask task2 = new CommonWordsTask(documents, mid, end);
                invokeAll(task1, task2);
                Set<String> result1 = task1.join();
                Set<String> result2 = task2.join();
                return intersect(result1, result2);
            }
        }

        private Set<String> findCommonWords(List<String> docs) {
            Set<String> commonWords = null;
            for (String doc : docs) {
                Set<String> words = new HashSet<>(Arrays.asList(doc.split("\\W+")));
                if (commonWords == null) {
                    commonWords = words;
                } else {
                    commonWords.retainAll(words);
                }
            }
            return commonWords == null ? new HashSet<>() : commonWords;
        }

        private Set<String> intersect(Set<String> set1, Set<String> set2) {
            set1.retainAll(set2);
            return set1;
        }
    }

    public static void main(String[] args) {
        List<String> documents = readDocuments("C:\\Users\\maksb\\IdeaProjects\\parallel labs\\Lab4\\src\\books\\common_words");
        ForkJoinPool pool = new ForkJoinPool();
        CommonWordsTask task = new CommonWordsTask(documents, 0, documents.size());
        Set<String> commonWords = pool.invoke(task);
        System.out.println("Common words: " + commonWords);
    }

    private static List<String> readDocuments(String path) {
        List<String> documents = new ArrayList<>();
        try {
            Files.walk(Paths.get(path)).filter(Files::isRegularFile).forEach(filePath -> {
                try {
                    documents.add(new String(Files.readAllBytes(filePath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }
}

