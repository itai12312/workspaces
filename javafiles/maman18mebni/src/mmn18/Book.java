package mmn18;
public class Book {
	private String name;
    int memberId;
    /*
     * Constracts the Book object
     * @param bookName    the name of the book
     * @param idNum         the id number of the member
     */
    public Book(String bookName, int idNum){
        name=bookName;
        memberId=idNum;
    }
    /*
     * Constracts the Book object
     * @param bookName    the name of the book
     */
    public Book(String bookName){
        name=bookName;
        memberId=0;
    }
    /*
     * Returns the name of the Book
     * @return the name of the Book
     */
    public String getBookName(){
        return name;
    }
    /*
     * Return the id number of the member
     * @return the id number of the member
     */
    public int getmemberId(){
        return memberId;
    }
   
    /*
     * sets the name of the Book
     * @param nameBook the name of the Book
     */
    public void setBookName(String nameBook){
        name=nameBook;
    }
    /*
     * sets the id number of the member
     * @param id the id number of the member
     */
    public void setmemberId(int id){
        memberId=id;
    }
}
