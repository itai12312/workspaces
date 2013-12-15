package mmn18;

public class StudentNode
{
    private Member student;
    private StudentNode left,right,parent;
    private int color;

    public StudentNode(Member stdnt)
    {
        student=stdnt;
        left=null;
        right=null;
        parent=null;
        color=0;
    }

    /*
     * Returns the parent of the node
     * @return parent
     */
    public StudentNode getParent()
    {
        return parent;
    }

    /*
     * Returns the left child of the node
     * @return left child
     */
    public StudentNode getLeft()
    {
        return left;
    }

    /*
     * Returns the right child of the node
     * @return right child
     */
    public StudentNode getRight()
    {
        return right;
    }

    /*
     * Returns the student object of the node
     * @return student object
     */
    public Member getStudent()
    {
        return student;
    }

    /*
     * Returns the color of the node
     * @return 1 if the color is red, otherwise 0 if it's black
     */
    public int getColor()
    {
        return color;
    }

    /*
     * Returns the grand parent of the node
     * @return grand parent
     */
    public StudentNode getGrandPa()
    {
        return parent.getParent();
    }

    /*
     * Replaces the parent of the node with a new one
     * @param _parent the new parent
     */
    public void setParent(StudentNode _parent)
    {
        parent=_parent;
    }

    /*
     * Replaces the right child of the node with a new one
     * @param _right the new right child
     */
    public void setRight(StudentNode _right)
    {
        right=_right;
    }

    /*
     * Replaces the left child of the node with a new one
     * @param _left the new left child
     */
    public void setLeft(StudentNode _left)
    {
        left=_left;
    }
    
    /*
     * Replaces the color of the node with a new one
     * @param _color the new color
     */
    public void setColor(int _color)
    {
        color=_color;
    }
    
    /*
     * Replaces the student object of the node with a new one
     * @param _student the new student object
     */
    public void setStudent(Member _student)
    {
        student=_student;
    }


}
