package mmn18;

public class Course {
    private String name,id;
    int grade;
    int studentsNum;
    MemberTree students;

    /*
     * Constracts the Course object
     * @param courseName    the name of the course
     * @param idNum         the id number of the course
     */
    public Course(String courseName, String idNum)
    {
        name=courseName;
        id=idNum;
        students=new MemberTree();
        grade=0;
        studentsNum=0;
    }

    /*
     * Adds a new student to the course's students-tree,
     * where it stores students, who has taken the course.
     * @param _student  the student object
     * @param  _grade   the student's grade in the course
     */
    public void addStudent(Member _student,int _grade)
    {
        // create a node to the given student object
        StudentNode studNode=new StudentNode(_student);

        // if the students-tree is empty, the node will be a root
        if(students.isEmpty()==0)
            students.insert(studNode);
        else
            students.setHead(studNode);

        // increase number of the field studentsNum
        // and add the specific student's grade in the course
        // with the sum of the other grades of the students, who have
        // taken it.
        studentsNum++;
        grade+=_grade;
    }

    /*
     * Sets a new grade in the course
     */
    public void setNewGradeInCourse(String sID,int _grade)
    {
        StudentNode studNode=students.search(sID);
        studNode.getStudent().setGrades(_grade);
    }

    /*
     * Returns the name of the course
     * @return the name of the course
     */
    public String getCourseName()
    {
        return name;
    }

    /*
     * Return the id number of the course
     * @return the id number of the course
     */
    public String getCourseId()
    {
        return id;
    }

    /*
     * Returns the sum of the grades of the course
     * @return the sum of the grades
     */
    public int getGrades()
    {
        return grade;
    }

    /*
     * Returns the number of students, who have taken this course
     * @return the number of students
     */
    public int getNumOfStudents()
    {
        return studentsNum;
    }

    /*
     * Sets a new sum of grades in the course
     */
    public void setGrade(int g)
    {
        grade=g;
    }
}
