package mmn18;

public class BookNode
{
    private Book book;
    private BookNode left,right,parent;
    int color; //red - 1, black - 0

    /*
     * Constracts a node to the course object
     * @param _course   the course object
     */
    public BookNode(Book _book)
    {
        book=_book;
        left=null;
        right=null;
        parent=null;
    }

    /*
     * Returns the parent of the node
     * @return the parent of the node
     */
    public BookNode getParent()
    {
        return parent;
    }

    /*
     * Returns the left child of the node
     * @return the left child
     */
    public BookNode getLeft()
    {
        return left;
    }

    /*
     * Returns the right child of the node
     * @return the right child
     */
    public BookNode getRight()
    {
        return right;
    }

    /*
     * Return the book object
     * @return the book object
     */
    public Book getBook()
    {
        return book;
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
    public BookNode getGrandPa()
    {
    	if(parent!=null)
    		return parent.getParent();
    	return null;
    }

    /*
     * Replaces the parent with a new one
     * @param _parent the new parent
     */
    public void setParent(BookNode _parent)
    {
        parent=_parent;
    }

    /*
     * Replaces the right child with a new one
     * @param _right the new right child
     */
    public void setRight(BookNode _right)
    {
        right=_right;
    }

    /*
     * Replaces the left child with a new one
     * @param _left the new left child
     */
    public void setLeft(BookNode _left)
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
     * Sets the book object of the node
     * @param _book     the book object
     */
    public void setBook(Book _book)
    {
        book=_book;
    }
    
}
