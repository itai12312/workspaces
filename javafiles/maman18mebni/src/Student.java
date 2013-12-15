package mmn18;

public class Student {
    private String fName,lName;
    private String id;
    private BookTree Courses;
    private int grade;
    private int coursesNum;

    /*
     * Constracts a new student object
     * @param firstName student's first name
     * @param lastName  student's last name
     * @param idNum     student's id number
     */
    public Student(String firstName, String lastName,String idNum)
    {
        fName=firstName;
        lName=lastName;
        id=idNum;
        grade=0;
        coursesNum=0;
        Courses=new BookTree();
    }

    /*
     * Adds a new course into the student's courses-tree
     * @param _course   the course object
     * @param _grade    the grade in this course
     */
    public void addCourse(Book _course,int _grade)
    {
        // create a node of the course object
        CourseNode courseNode=new CourseNode(_course);

        // if the tree is empty, sets the node as the root
        if(Courses.isEmpty()==0)
            Courses.insert(courseNode);
        else
            Courses.setHead(courseNode);
        coursesNum++;
        grade+=_grade;
    }

    /*
     * Sets a new grade in the course
     * @param cID   the course id
     * @param _grade    the student's grade in this course
     */
    public void setNewGradeInCourse(String cID,int _grade)
    {
        CourseNode courseNode=Courses.search(cID);
        courseNode.getCourse().setGrade(_grade);
    }

    /*
     * Returns the student's first name
     * @return first name
     */
    public String getFirstName()
    {
        return fName;
    }

    /*
     * Returns the student's last name
     * @return last name
     */
    public String getLastName()
    {
        return lName;
    }

    /*
     * Returns the student's id
     * @return id
     */
    public String getStudentId()
    {
        return id;
    }

    /*
     * Returns the students sum of all his grades
     * @return grades
     */
    public int getGrades()
    {
        return grade;
    }

    /*
     * Returns the number of courses this student has taken
     * @return the number of courses
     */
    public int getNumOfCourses()
    {
        return coursesNum;
    }

    /*
     * Returns the student's courses-tree
     * @return the courses-tree
     */
    public BookTree getCoursesTree()
    {
        return Courses;
    }

    /*
     * Sets a new grades
     * @param _grade    the new grade
     */
    public void setGrades(int _grade)
    {
        grade=_grade;
    }
}
