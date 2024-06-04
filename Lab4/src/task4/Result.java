package task4;

import java.nio.file.Path;
import java.util.List;

class Result {
    Path filePath;
    List<String> keywordsFound;

    Result(Path filePath, List<String> keywordsFound) {
        this.filePath = filePath;
        this.keywordsFound = keywordsFound;
    }
}
