package Task3;

import java.util.ArrayList;
import java.util.List;

public class GradeBook {
    private List<Group> groups;
    private int[][][] grades; // [groupIndex][studentIndex][week]
    private List<String> studentsWithGradeThisWeek;

    public GradeBook(List<Group> groups, int weeks) {
        this.groups = groups;
        this.grades = new int[groups.size()][][];
        for (int i = 0; i < groups.size(); i++) {
            this.grades[i] = new int[groups.get(i).getStudents().size()][weeks];
        }
        this.studentsWithGradeThisWeek = new ArrayList<>();
    }

    public synchronized void setGrades(int groupIndex, int studentIndex, int week, int grade) {
        grades[groupIndex][studentIndex][week] = grade;
    }

    public synchronized void printGradesByWeeks() {
        for (int week = 0; week < grades[0][0].length; week++) {
            System.out.println("Week " + (week + 1) + ":");
            for (int groupIndex = 0; groupIndex < groups.size(); groupIndex++) {
                Group group = groups.get(groupIndex);
                System.out.println("Group: " + group.getName());
                for (int studentIndex = 0; studentIndex < group.getStudents().size(); studentIndex++) {
                    String student = group.getStudents().get(studentIndex);
                    int grade = grades[groupIndex][studentIndex][week];
                    System.out.println("Student: " + student + ", Grade: " + grade);
                }
            }
            System.out.println();
        }
    }

    public int[][][] getGrades() {
        return grades;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public synchronized boolean hasStudentReceivedGradeThisWeek(String student) {
        return studentsWithGradeThisWeek.contains(student);
    }

    public synchronized void addStudentWithGradeThisWeek(String student) {
        studentsWithGradeThisWeek.add(student);
    }

    public synchronized void removeStudentsWithGradeThisWeek() {
        studentsWithGradeThisWeek.clear();
    }
}