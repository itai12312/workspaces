package mmn18;

public class BookTree
{
    private BookNode head;

    /*
     * Constracts the BookTree object
     * @param _head the root of the tree
     */
    public BookTree(BookNode _head)
    {
        head=_head;
    }

    /*
     * Constracts the BookTree object
     * the root of the tree is set as null
     */
    public BookTree()
    {
        head=null;
    }

    /*
     * Sets the head of the tree
     * @param _head the head
     */
    public void setHead(BookNode _head)
    {
        head=_head;
    }
    /*
     * returns the head of the tree
     */
    public BookNode getHead()
    {
        return head;
    }
    /*
     * Checks if the tree is empty
     * @return 1 if it's empty, otherwise 0
     */
    public int isEmpty()
    {
        if(head==null)
            return 1;
        return 0;
    }

    /*
     * Left rotating the node in the tree, as mentioned in the book
     * @param x the node we want to rotate
     */
    private void leftRotate(BookNode x)
    {
        BookNode y=x.getRight();
        x.setRight(y.getRight());
        if(y.getLeft()!=null)
            y.getLeft().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent()==null)
            head=y;
        else if(x==x.getParent().getLeft())
            x.getParent().setLeft(y);
        else
            x.getParent().setRight(y);
        y.setLeft(x);
        x.setParent(y);

    }

    /*
     * Right rotating the node in the tree, as mentioned in the book
     * @param x the node we want to rotate
     */
    private void rightRotate(BookNode x)
    {
        BookNode y;
        if(x.getLeft()!=null)
            y=x.getLeft();
        else
            y=null;
        if(y.getLeft()!=null)
            x.setLeft(y.getLeft());
        else
            x.setLeft(null);
        if(y.getRight()!=null)
            y.getRight().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent()==null)
            head=y;
        else if(x==x.getParent().getRight())
            x.getParent().setRight(y);
        else
            x.getParent().setLeft(y);
        y.setRight(x);
        x.setParent(y);
    }

    /*
     * Inserts a new node into the tree
     * @param z the node
     */
    public void insert(BookNode z)
    {
        BookNode y=null,x=head;
        while (x!=null)
        {
            y=x;
            if(z.getBook().getBookName().compareTo(x.getBook().getBookName())<0)
                x=x.getLeft();
            else
                x=x.getRight();
        }
        z.setParent(y);
        if (y==null)
            head=z;
        else
        {
            if((z.getBook().getBookName().compareTo(y.getBook().getBookName()))<0)
                y.setLeft(z);
            else
                y.setRight(z);
        }

        z.setLeft(null);
        z.setRight(null);
        z.setColor(1);
        if(z.getGrandPa()!=null)
            insertFixUp(z);
    }

    /*
     * After inserting a node, the tree might be illegal.
     * Therefore, this function will fix every problem,
     * in order, to get a legal one.
     * @param z the node that was inserted
     */
    private void insertFixUp(BookNode z)
    {
        //red=1, black=0
        BookNode y=null;

        while( (z!=head) && (z.getParent().getColor()==1) )
        {
            if (z.getParent()==z.getGrandPa().getRight())
            {
                y=z.getGrandPa().getRight();
                if(y.getColor()==1)
                {
                    z.getParent().setColor(0);
                    y.setColor(0);
                    z.getGrandPa().setColor(1);
                    z=z.getGrandPa();
                }
                else
                {
                    if(z==z.getParent().getRight())
                    {
                        z=z.getParent();
                        leftRotate(z);
                    }
                    else
                    {
                        z.getParent().setColor(0);
                        z.getGrandPa().setColor(1);
                        rightRotate(z.getGrandPa());
                    }
                }
                
            }
            else
            {
                y=z.getGrandPa().getLeft();
                if(y.getColor()==1)
                {
                    z.getParent().setColor(0);
                    y.setColor(0);
                    z.getGrandPa().setColor(1);
                    z=z.getGrandPa();
                }
                else
                {
                    if(z==z.getParent().getLeft())
                    {
                        z=z.getParent();
                        rightRotate(z);
                    }
                    else
                    {
                        z.getParent().setColor(0);
                        z.getGrandPa().setColor(1);
                        leftRotate(z.getGrandPa());
                    }
                }
                
            }
        }
        head.setColor(0);
    }

    /*
     * Deletes a node in the tree.
     * As mentioned in the book
     * @param z the node we want to delete
     * @return    pointer to the new node, where the deleted node was.
     */
    public BookNode delete(BookNode z)
    {
        BookNode y=null,x=null;

        if( (z.getLeft()==null) || (z.getRight()==null) )
            y=z;
        else
            y=successor(z);

        if(y.getLeft()!=null)
        {
            x=y.getLeft();
            
        }
        else
        {
            x=y.getRight();
        }
        if(x!=null)
        	x.setParent(y.getParent());
        if(y.getParent()==null)
            head=x;
        else if(y==y.getParent().getLeft()&&x!=null)
            x.getParent().setLeft(x);
        else
            y.getParent().setRight(x);

        if(y!=z)
            copyData(y,z);
        if(y.getColor()==0)
            deleteFixUp(x);
        return y;
    }

    /*
     * After deleting a node, the tree might be illegal.
     * Therefore, this function will fix every problem,
     * in order, to get a legal one.
     * @param z the node that is going to be deleted
     */
    private void deleteFixUp(BookNode x)
    {
        BookNode w;
        while( x!=null&&(x!=head) && (x.getColor()==0))
        {
            if (x==x.getParent().getLeft())
            {
                w=x.getParent().getRight();
                if(w.getColor()==1)
                {
                    w.setColor(0);
                    x.getParent().setColor(1);
                    leftRotate(x.getParent());
                    w=x.getParent().getRight();
                }
                if( (w.getLeft().getColor()==0) && (w.getRight().getColor()==0) )
                {
                    w.setColor(1);
                    x=x.getParent();
                }
                else if(w.getRight().getColor()==0)
                {
                    w.getLeft().setColor(0);
                    w.setColor(1);
                    rightRotate(w);
                    w=x.getParent().getRight();
                }
                else
                {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(0);
                    w.getRight().setColor(0);
                    leftRotate(x.getParent());
                    x=head;
                }
            }
            else
            {
                w=x.getParent().getLeft();
                if(w.getColor()==1)
                {
                    w.setColor(0);
                    x.getParent().setColor(1);
                    rightRotate(x.getParent());
                    w=x.getParent().getLeft();
                }
                if( (w.getRight().getColor()==0) && (w.getLeft().getColor()==0) )
                {
                    w.setColor(1);
                    x=x.getParent();
                }
                else if(w.getLeft().getColor()==0)
                {
                    w.getRight().setColor(0);
                    w.setColor(1);
                    leftRotate(w);
                    w=x.getParent().getLeft();
                }
                else
                {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(0);
                    w.getLeft().setColor(0);
                    rightRotate(x.getParent());
                    x=head;
                }
            }
        }
        if(x!=null)
        	x.setColor(0);
    }

    /*
     * Returns the node that contains the key that is the successor of
     * a given node
     * @param x the given node
     * @return    pointer to the successor
     */
    public BookNode successor(BookNode x)
    {
        BookNode y=null;

        if(x.getRight()!=null)
            return minimum(x.getRight());

        y=x.getParent();
        while( (y!=null) && (x==y.getRight()) )
        {
            x=y;
            y=y.getParent();
        }

        return y;
    }

    /*
     * Returns the node with the minimal key in a sub tree
     * @param x where the node, where the sub tree begins
     * @return  pointer to the node with the minimal key
     */
    private BookNode minimum(BookNode x)
    {
        while(x.getLeft()!=null)
            x=x.getLeft();
        return x;
    }


    /*
     * Copies the relavent data of a node to the other node
     * @param z the node that its data is going to be replaced by the other one
     * @param y the node that its data is going to replace the data of the other one
     */
    private void copyData(BookNode y, BookNode z)
    {
        z.setBook(y.getBook());
    }

    /*
     * Searches for a node by its id
     * @param id the id of the node
     * @return null if not found, otherwise a pointer to the node
     */
    public BookNode search(String id)
    {
        BookNode y=head;
        while(y!=null)
        {
            if(y.getBook().getBookName().compareTo(id)<0)
                y=y.getRight();
            else if(y.getBook().getBookName().compareTo(id)>0)
                y=y.getLeft();
            else
                return y;
        }
        return y;
    }

    /*
     * prints all the book that the current user holds
     */
	public void findBooks() {
		BookNode y=head;
		recursiveprint(y);		
	}
	
	/*
	 * prints all the info in a given tree
	 * @param y the tree head
	 */
	public void recursiveprint(BookNode y){
		if(y!=null){
		System.out.print(y.getBook().getBookName()+" ");
		if(y.getLeft()!=null)
			recursiveprint(y.getLeft());
		if(y.getRight()!=null)
			recursiveprint(y.getRight());
	}
	}

}
