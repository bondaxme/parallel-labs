package task4;


import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ForkJoinPool;


public class DocumentSearch {

    public static void main(String[] args) {
        String directoryPath = "C:\\Users\\maksb\\IdeaProjects\\parallel labs\\Lab4\\src\\books";
        String[] keywords = {"parallel", "c++", "MPI", "algorithm", "java", "c#", "programming"};

        ForkJoinPool pool = new ForkJoinPool();
        DocumentSearchTask task = new DocumentSearchTask(Paths.get(directoryPath), keywords);
        List<Result> result = pool.invoke(task);

        for (Result res : result) {
            System.out.println(res.filePath + " - Keywords found: " + String.join(", ", res.keywordsFound));
        }
    }

}

