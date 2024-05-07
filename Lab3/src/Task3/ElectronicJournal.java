package Task3;

import java.util.ArrayList;
import java.util.List;

public class ElectronicJournal {
    public static void main(String[] args) {
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        Group group3 = new Group("Group 3");

        group1.addStudent("Student 1");
        group1.addStudent("Student 2");
        group1.addStudent("Student 3");
        group1.addStudent("Student 4");

        group2.addStudent("Student 5");
        group2.addStudent("Student 6");
        group2.addStudent("Student 7");
        group2.addStudent("Student 8");

        group3.addStudent("Student 9");
        group3.addStudent("Student 10");
        group3.addStudent("Student 11");
        group3.addStudent("Student 12");

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        GradeBook gradeBook = new GradeBook(groups, 2);

        TeachersThread lecturer = new TeachersThread(gradeBook);
        TeachersThread assistant1 = new TeachersThread(gradeBook);
        TeachersThread assistant2 = new TeachersThread(gradeBook);
        TeachersThread assistant3 = new TeachersThread(gradeBook);

        lecturer.start();
        assistant1.start();
        assistant2.start();
        assistant3.start();

        try {
            lecturer.join();
            assistant1.join();
            assistant2.join();
            assistant3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gradeBook.printGradesByWeeks();
    }
}
