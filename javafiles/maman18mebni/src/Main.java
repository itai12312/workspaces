package mmn18;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        boolean loop=true;
        int avrg=0;
        String [] query=new String[4];
        Scanner input=new Scanner(System.in);

        // main students-tree and courses-tree
        MemberTree sTree=new MemberTree();
        BookTree cTree=new BookTree();

        // Creating Students & Courses
        createStudent("Yoni","Choen","089111112",sTree);
        createStudent("Ronen","Vaknin","3210332212",sTree);
        createStudent("Ami","Shapira","0360563232",sTree);
        createStudent("Rotem","Reches","0812005802",sTree);
        createStudent("Asaf","Katz","6907241132",sTree);
        createStudent("Oren","Shalev","3200472212",sTree);

        createCourse("Linear Algebra I","20109",cTree);
        createCourse("Calculus I","20106",cTree);
        createCourse("Linear Algebra II","20229",cTree);
        createCourse("Calculus II","20212",cTree);
        createCourse("Discreth Mathematics","20283",cTree);
        createCourse("Graph Theory","20295",cTree);

        registerStudentCourse("089111112","20109",sTree,cTree,97);
        registerStudentCourse("089111112","20106",sTree,cTree,92);
        registerStudentCourse("089111112","20229",sTree,cTree,95);

        registerStudentCourse("3210332212","20109",sTree,cTree,91);
        registerStudentCourse("3210332212","20283",sTree,cTree,100);

        registerStudentCourse("0360563232","20106",sTree,cTree,61);

        registerStudentCourse("0812005802","20295",sTree,cTree,54);

        registerStudentCourse("6907241132","20109",sTree,cTree,87);

        registerStudentCourse("3200472212","20109",sTree,cTree,90);
        registerStudentCourse("3200472212","20229",sTree,cTree,96);
        registerStudentCourse("3200472212","20106",sTree,cTree,92);
        registerStudentCourse("3200472212","20212",sTree,cTree,100);
        registerStudentCourse("3200472212","20283",sTree,cTree,100);
        registerStudentCourse("3200472212","20295",sTree,cTree,100);
        // End

        // Query-Section
        while(loop)
        {
            System.out.print("Query: ");
            query[0]=input.nextLine();
            query=query[0].split(" ");
            if(query[0].toLowerCase().compareTo("exit")==0)
                loop=false;
            else if(query[0].compareTo("?")==0)
            {
                if(query[1].compareTo("SC")==0)
                    studentPassed(sTree,query[3],query[2]);
                if(query[1].compareTo("SA")==0)
                    studentAvrg(query[2],sTree);
                if(query[1].compareTo("CA")==0)
                    courseAvrg(query[2],cTree);
            }
            else
            {
                if(query[0].compareTo("INS")==0)
                    createStudent(query[1],query[2],query[3],sTree);
                else if(query[0].compareTo("INC")==0)
                    createCourse(query[1],query[2],cTree);
                else if(query[0].compareTo("REG")==0)
                    registerStudentCourse(query[1],query[2],sTree,cTree,Byte.parseByte(query[3]));
            }
        }
        // End   
    }


    // creating new students
    // this is done by the following steps
    // creating a student using Student class
    // later creating a node, which contains the student
    // inserting this node to the Student Tree
    private static void createStudent(String fName,String lName,String ID,MemberTree sTree)
    {

        // create the student and its node
        Member stud=new Member(fName,lName,ID);
        StudentNode studnode=new StudentNode(stud);

        // if the tree is empty, set the node as root
        if(sTree.isEmpty()==0)
            sTree.insert(studnode);
        else
            sTree.setHead(studnode);
    }

    // creating new courses
    // this is done by the following steps
    // creating a course using Course class
    // later creating a node, which contains the course
    // inserting this node to the Course Tree
    private static void createCourse(String name,String ID,BookTree cTree)
    {
        // create the course and its node
        Book course=new Book(name,ID);
        CourseNode coursenode=new CourseNode(course);

        // if the tree is empty, set the node as root
        if(cTree.isEmpty()==0)
            cTree.insert(coursenode);
        else
            cTree.setHead(coursenode);
    }

    // registering a students to a course
    // every student has a tree contains all its
    // courses, which have been taken by him.
    // and every course has a tree contains all its
    // students, who have taken this course.
    // in order to register a student to a course, we have to
    // first add the course to the student's courses-tree
    // and second add the student to the course's students-tree
    // in addition, we add the grade to each tree.
    private static void registerStudentCourse(String sID,String cID,
            MemberTree sTree,BookTree cTree,int grade)
    {
        // search student by id in the main-students-tree
        StudentNode studPointer=sTree.search(sID);
        // search course by id in the main-courses-tree
        CourseNode coursePointer=cTree.search(cID);

        // avoiding aliasing :
        // we will create new student's node and course's node
        // and they will be inserted into course's students-tree
        // and student's courses-tree, respectively
        Member stud=new Member(studPointer.getStudent().getFirstName(),
                studPointer.getStudent().getLastName(),
                studPointer.getStudent().getStudentId());
        Book course=new Book(coursePointer.getCourse().getCourseName()
                ,coursePointer.getCourse().getCourseId());

        // inserting to the new objects' grades field the grade
        // which is important in order to get the average faster
        // and that's why we write null and not entering an object
        // because the objects' trees aren't going to be used.
        stud.addCourse(null, grade);
        course.addStudent(null, grade);

        //creating new objects' nodes.
        StudentNode studNode=new StudentNode(stud);
        CourseNode courseNode=new CourseNode(course);

        // updating student's courses-tree and course's students-tree
        studPointer.getStudent().addCourse(courseNode.getCourse(),grade);
        coursePointer.getCourse().addStudent(studNode.getStudent(), grade);

        // the output
        System.out.print("The Student ");
        System.out.print(stud.getFirstName()+" "+stud.getLastName()+", ID ");
        System.out.print(stud.getStudentId()+", has studied ");
        System.out.println(course.getCourseName()+" and his grade is "+grade);
    }

    // Query SC - prints the student's name, the course's name and the
    //            student's grade in this specific course.
    //            if the student has failed in this course, instead of his grade,
    //            "failed" will be printed.
    private static void studentPassed(MemberTree sT,String cId,String sId)
    {
        Member stud;
        Book course;

        // find the student by id in the main-students-tree
        StudentNode s=sT.search(sId);

        // if student wasn't found, print an error message
        if( (s!=null))
        {
            // search the course by id in the student's courses-tree
            // if not found, prints an error message
            if(s.getStudent().getCoursesTree().search(cId)==null)
                System.out.println("The student hasn't taken this course");

            //otherwise, if the course was found, and his grade is not smaller
            // than 60 then print it out.
            else if(s.getStudent().getCoursesTree().search(cId).getCourse().getGrades()>=60)
            {
                stud=s.getStudent();
                course=s.getStudent().getCoursesTree().search(cId).getCourse();
                System.out.print(" "+stud.getFirstName()+" "+stud.getLastName());
                System.out.println(" "+course.getCourseName()+" "+course.getGrades());
            }
            //otherwise, if the course was found, but his grade is smaller
            // than 60, then print that he has failed.
            else
            {
                stud=s.getStudent();
                course=s.getStudent().getCoursesTree().search(cId).getCourse();
                System.out.print(" "+stud.getFirstName()+" "+stud.getLastName());
                System.out.println(" "+course.getCourseName()+" FAILED");
            }
        }
        else
            System.out.println("The student wasn't found");
    }



    // Query SA - prints the student's grades average.
    private static void studentAvrg(String ID,MemberTree sTree)
    {
        int grades,numOfCourses,avrg;
        Member stud;

        // search the student by id in the main-students-tree
        StudentNode studNode=sTree.search(ID);
        stud=studNode.getStudent();

        // if not found, prints an error message
        if(stud==null)
            System.out.println("Student wasn't found");
        // otherwise, prints the average
        else
        {
            // get the sum of the grades
            grades=studNode.getStudent().getGrades();
            // get the number of courses
            numOfCourses=studNode.getStudent().getNumOfCourses();

            avrg=grades/numOfCourses;
            
            // prints the message
            System.out.print(stud.getFirstName()+" "+stud.getLastName());
            System.out.println(" has grades average of  "+avrg);
        }
    }

    // Query CA - prints the students' grades average in this course.
    private static void courseAvrg(String ID,BookTree cTree)
    {
        int grades,numOfStudents,avrg;
        Book course;

        // search the student by id in the main-students-tree
        CourseNode courseNode=cTree.search(ID);
        course=courseNode.getCourse();

        // if not found, prints an error message
        if(course==null)
            System.out.println("Course wasn't found");
        else
        {
            // get the sum of the grades
            grades=courseNode.getCourse().getGrades();
            // get the num of the students, who have taken this course
            numOfStudents=courseNode.getCourse().getNumOfStudents();

            avrg=grades/numOfStudents;
            
            // prints the message
            System.out.print(" "+course.getCourseName()+" "+course.getCourseId());
            System.out.println(" has grades average of  "+avrg);
        }
    }

    public void courseUpdate(String sID,String cID, MemberTree sTree,
            BookTree cTree, int _grade)
    {
        // search student by id in the main-students-tree
        StudentNode studPointer=sTree.search(sID);
        // search course by id in the main-courses-tree
        CourseNode coursePointer=cTree.search(cID);

        //updating the grade in both nodes
        studPointer.getStudent().setNewGradeInCourse(cID, _grade);
        coursePointer.getCourse().setNewGradeInCourse(sID, _grade);
    }

}
