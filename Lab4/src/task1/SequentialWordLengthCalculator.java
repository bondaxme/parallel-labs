package task1;

import java.util.List;

public class SequentialWordLengthCalculator {
    public static double calculateAverageWordLength(List<String> words) {
        long totalLength = words.stream().mapToInt(String::length).sum();
        return (double) totalLength / words.size();
    }
}
