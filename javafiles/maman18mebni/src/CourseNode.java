package mmn18;

public class CourseNode
{
    private Book course;
    private CourseNode left,right,parent;
    private int grade;
    int color; //red - 1, black - 0

    /*
     * Constracts a node to the course object
     * @param _course   the course object
     */
    public CourseNode(Book _course)
    {
        course=_course;
        left=null;
        right=null;
        parent=null;
        grade=0;
    }

    /*
     * Returns the parent of the node
     * @return the parent of the node
     */
    public CourseNode getParent()
    {
        return parent;
    }

    /*
     * Returns the left child of the node
     * @return the left child
     */
    public CourseNode getLeft()
    {
        return left;
    }

    /*
     * Returns the right child of the node
     * @return the right child
     */
    public CourseNode getRight()
    {
        return right;
    }

    /*
     * Return the course object
     * @return the course object
     */
    public Book getCourse()
    {
        return course;
    }

    /*
     * Return the color of the node
     * red is 1 and black is 0
     * @return the color
     */
    public int getColor()
    {
        return color;
    }

    /*
     * Returns the grand parent of the node
     * @return grand parent
     */
    public CourseNode getGrandPa()
    {
        return parent.getParent();
    }

    /*
     * Replaces the parent with a new one
     * @param _parent the new parent
     */
    public void setParent(CourseNode _parent)
    {
        parent=_parent;
    }

    /*
     * Replaces the right child with a new one
     * @param _right the new right child
     */
    public void setRight(CourseNode _right)
    {
        right=_right;
    }

    /*
     * Replaces the left child with a new one
     * @param _left the new left child
     */
    public void setLeft(CourseNode _left)
    {
        left=_left;
    }

    /*
     * Sets the node color
     * @param _color the color
     */
    public void setColor(int _color)
    {
        color=_color;
    }

    /*
     * Sets the course object of the node
     * @param _course   the course object
     */
    public void setCourse(Book _course)
    {
        course=_course;
    }
    
}
