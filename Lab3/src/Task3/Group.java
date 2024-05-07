package Task3;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<String> students;

    public Group(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }

    public void addStudent(String student) {
        students.add(student);
    }

    public List<String> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }
}

