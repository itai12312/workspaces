package mmn18;

import java.util.ArrayList;

public class MemberTree
{
	private MemberNode head;
	ArrayList<String> membs;
	int maximum;
	/*
	 * Constracts a members tree
	 * @param hd    the root of the tree
	 */
	public MemberTree(MemberNode hd)
	{
		head=hd;
	}

	/*
	 * Constracts an empty members tree
	 */
	public MemberTree()
	{
		head=null;
	}

	/*
	 * Sets the head of the tree
	 * @param _head the head
	 */
	public void setHead(MemberNode hd)
	{
		head=hd;
	}
	/*
	 * returns the head of the tree
	 */
	public MemberNode getHead()
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
	private void leftRotate(MemberNode x)
	{
		MemberNode y=x.getRight();
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
	private void rightRotate(MemberNode x)
	{
		MemberNode y;
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
	public void insert(MemberNode z)
	{
		MemberNode y=null,x=head;
		while (x!=null)
		{
			y=x;
			if(z.getmember().getId()<x.getmember().getId())
				x=x.getLeft();
			else
				x=x.getRight();
		}
		z.setParent(y);
		if (y==null)
			head=z;
		else
		{
			if(z.getmember().getId()<y.getmember().getId())
				y.setLeft(z);
			else
				y.setRight(z);
		}

		z.setLeft(null);
		z.setRight(null);
		z.setColor(1);
		if(z.getGrandPa()!=null)
		{
			//System.out.println(z.getGrandPa().getName()+" ; "+z.getParent().getName()+" ; "+z.getName());
			//System.out.println(z.getParent().getColor());
			insertFixUp(z);
		}
	}

	/*
	 * After inserting a node, the tree might be illegal.
	 * Therefore, this function will fix every problem,
	 * in order, to get a legal one.
	 * @param z the node that was inserted
	 */
	private void insertFixUp(MemberNode z)
	{
		//red=1, black=0
		MemberNode y=null;

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
	public MemberNode delete(MemberNode z)
	{
		MemberNode y=null,x=null;

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
	private void deleteFixUp(MemberNode x)
	{
		MemberNode w;
		while( (x!=head) && (x.getColor()==0))
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
		x.setColor(0);
	}

	/*
	 * Returns the node that contains the key that is the successor of
	 * a given node
	 * @param x the given node
	 * @return    pointer to the successor
	 */
	public MemberNode successor(MemberNode x)
	{
		MemberNode y=null;

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
	private MemberNode minimum(MemberNode x)
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
	private void copyData(MemberNode y, MemberNode z)
	{
		z.setmember(y.getmember());
	}

	/*
	 * Searches for a node by its id
	 * @param id the id of the node
	 * @return null if not found, otherwise a pointer to the node
	 */
	public MemberNode search(int id)
	{
		MemberNode y=head;
		while(y!=null)
		{
			if(y.getmember().getId()<id)
				y=y.getRight();
			else if(y.getmember().getId()>id)
				y=y.getLeft();
			else
				return y;
		}
		return y;
	}
	/*
	 * prints all the members that hold the greatest number of books
	 * by scanning all the member's number of books
	 */
	public void findMaxPersons() {
		membs=new ArrayList<String>();
		maximum=-1;
		recursivescan(head);
		System.out.println("members that hold most books are:");
		for(int i=0;i<membs.size();i++)
			System.out.print(membs.get(i)+" ");
		System.out.println();
	}

	/*
	 * prints all the info in a given tree
	 * @param y the tree head
	 */
	public void recursivescan(MemberNode y){
		if(y!=null){
			if(y.getmember().getNumOfBooks()>maximum){
				maximum=y.getmember().getNumOfBooks();
				membs=new ArrayList<String>();
				membs.add(y.getmember().getName());
			}else if(y.getmember().getNumOfBooks()==maximum){
				membs.add(y.getmember().getName());
			}
			recursivescan(y.getLeft());
			recursivescan(y.getRight());
		}
	}
}