package Task3;

public class TeachersThread extends Thread {
    private GradeBook gradeBook;

    public TeachersThread(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
    }

    @Override
    public void run() {
        for (int week = 0; week < gradeBook.getGrades()[0][0].length; week++) {
            System.out.println("Week " + (week + 1) + ": Thread " + currentThread().getName() + " is setting grades");
            for (int groupIndex = 0; groupIndex < gradeBook.getGroups().size(); groupIndex++) {
                for (int i = 0; i < gradeBook.getGroups().get(groupIndex).getStudents().size(); i++) {
                    String student = gradeBook.getGroups().get(groupIndex).getStudents().get(i);
                    synchronized (gradeBook) {
                        if (!gradeBook.hasStudentReceivedGradeThisWeek(student)) {
                            int grade = (int) (Math.random() * 101);
                            gradeBook.setGrades(groupIndex, i, week, grade);
                            System.out.println("Thread " + currentThread().getName() + " set grade " + grade + " for Student " + student + " in Week " + (week + 1));
                            gradeBook.addStudentWithGradeThisWeek(student);
                            break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gradeBook.removeStudentsWithGradeThisWeek();
        }
    }
}

