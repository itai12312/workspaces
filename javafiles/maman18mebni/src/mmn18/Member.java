package mmn18;
public class Member {
    private String name;
    private int id;
    private BookTree books;
    int numofbooks;
  

    /*
     * Constracts a new member object
     * @param name member's name
     * @param id  member's id
      */
    public Member(String _name, int _id)
    {
        name=_name;
        id=_id;
        numofbooks=0;
        books=new BookTree();
    }

    /*
     * Adds a new Book into the member's Books-tree
     * @param bookName   the Book name
     */
    public void addBook(String bookName){
    	Book _Book=new Book(bookName);
        // create a node of the Book object
        BookNode Booknode=new BookNode(_Book);

        // if the tree is empty, sets the node as the root(0==false)
        if(books.isEmpty()==0)
            books.insert(Booknode);
        else
            books.setHead(Booknode);
        numofbooks++;
    }
    /*
     * removes book from the member's Books-tree
     * @param bookName   the Book name
     */
    public void removeBook(String bookName){
    	BookNode _Book=books.search(bookName);	
    	if(_Book!=null){
    		books.delete(_Book);
    	}
    	numofbooks--;
    }
    /*
     * Returns the member's name
     * @return  name
     */
    public String getName()
    {
        return name;
    }

    /*
     * Returns the member's last name
     * @return last name
     */
    public int getId()
    {
        return id;
    }

    /*
     * Returns the members sum of all his grades
     * @return grades
     */
    public BookTree getBooks()
    {
        return books;
    }

    /*
     * Returns the number of Books this member has taken
     * @return the number of Books
     */
    public int getNumOfBooks()
    {
        return numofbooks;
    }

    
}