package mmn18;
import java.util.ArrayList;
import java.util.Scanner;
public class maman18{
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
		String[]queries={
		"+ Yoni 89111112","Yoni 89111112 MN6543 +","+ Ronen 321332212","Ronen 321332212 VB2356 +",
		"? 89111112","? !","+ Ami 36563232","+ Rotem 8125802","+ Asaf 697241132","+ Oren 32472212",
		"Oren 32472212 AB1132 +","Oren 32472212 AB1132 -","+ Tomer 123456","Tomer 123456 BC4561 +",
		"? 123456","? BC4561","? !","Yoni 89111112 MN6543 -","? MN6543","- Yoni 89111112","? !",
		"+ Alon 9876","Alon 9876 FG6574 +","? FG6574","? !","Alon 9876 FG6574 -","Ronen 321332212 VB2356 -","? !",
		"+ Avi 4556","Avi 4556 MN8734 +","+ Ron 2822013","Ron 2822013 VB7649 +","? 2822013","? VB7649","? 4556",
		"? MN8734","? !","+ Ami 36563232","Avi 4556 MN8734 -","Ron 2822013 VB7649 +","? !","+ Ben 11102000",
		"Ben 11102000 BC4581 +","? 11102000","? BC4581","? !","Ben 11102000 BC4581 -","- Ben 11102000","? !"
		,"Yoni 89111112 MN6543 -","? MN6543","- Yoni 89111112","? !","exit"};//array of 54 commmands
		int i=0;
		
		BookTree books=new BookTree();
		MemberTree members=new MemberTree();
		String text=queries[i];
		while(!text.equals("exit")){
			String text1=text.substring(0,text.indexOf(" ")).trim();
			text=text.substring(text.indexOf(" ")+1);
			if(text1.charAt(0)>='A' && text1.charAt(0)<='Z'){
				String text2=text.substring(0,text.indexOf(" ")).trim();
				text=text.substring(text.indexOf(" ")+1);
				String text3=text.substring(0,text.indexOf(" ")).trim();
				text=text.substring(text.indexOf(" ")+1).trim();
				if(members.search(Integer.parseInt(text2))!=null){
					MemberNode member=members.search(Integer.parseInt(text2));
					if(text.equals("+")){//add a book
						if(books.search(text3)!=null){//delete if its borrowed by somebody else already
							members.search(books.search(text3).getBook().getmemberId()).getmember().removeBook(text3);
							books.delete(books.search(text3));
						}
						if(member.getmember().getNumOfBooks()<10){//if has space
							member.getmember().addBook(text3);//adds book
							books.insert(new BookNode(new Book(text3,Integer.parseInt(text2))));
							System.out.println("member name "+text1+" member id"+text2+" borrowed book" +text3);
						}
					}else if(text.endsWith("-")){//returns a book
						if(books.search(text3)!=null&&books.search(text3).getBook().getmemberId()==Integer.parseInt(text2)){// if book is found
							member.getmember().removeBook(text3);
							books.delete(books.search(text3));
							System.out.println("member name "+text1+" member id"+text2+" returned book" +text3);
						}
					}
				}
			}else if(text1.equals("+")){//add member
				String text2=text.substring(0,text.indexOf(" "));
				text=text.substring(text.indexOf(" ")+1);
				String text3=text.trim();//.substring(0,text.indexOf(" "));
				if(members.search(Integer.parseInt(text3))==null){//if member is not already added
					members.insert(new MemberNode(new Member(text2, Integer.parseInt(text3))));//id,name
					System.out.println("member name "+text2+" member id"+text3+" created");
				}
			}else if(text1.equals("-")){//remove member
				String text2=text.substring(0,text.indexOf(" "));
				text=text.substring(text.indexOf(" ")+1);
				String text3=text.trim();//.substring(0,text.indexOf(" "));
				MemberNode member=members.search(Integer.parseInt(text3));
				if(member!=null){//removes all book that member holds
					BookNode y=member.getmember().getBooks().getHead();
					while(y!=null)
						y=y.getLeft();
					while(y!=null){
						books.delete(books.search(y.getBook().getBookName()));
						y=member.getmember().getBooks().successor(y);//scans the list of books that member owns
					}
					members.delete(member);
					System.out.println("member name "+text2+" member id"+text3+" deleted");
				}
			}else if(text1.equals("?")){
				String text2=text.trim();//.substring(0,text.indexOf(" "));
				if(text2.charAt(0)>='A' && text2.charAt(0)<='Z'){//which member holds book X
					if(books.search(text2)!=null){
						System.out.println("memberid that holds book"+text2+ " is:"+books.search(text2).getBook().getmemberId());
					}
				}else if(text2.charAt(0)>='1' && text2.charAt(0)<='9'){//which books holds member X 
					if(members.search(Integer.parseInt(text2))!=null){
						System.out.println("books  for:"+text2+" numberofbooks:"+members.search(Integer.parseInt(text2)).getmember().getNumOfBooks());
						members.search(Integer.parseInt(text2)).getmember().getBooks().findBooks();
						System.out.println("end list of books");
					}
				}else if(text2.equals("!")==true){//print members with greatest number of books
					members.findMaxPersons();
				}
			}
			i++;
			text=queries[i];
		}
	}
}
