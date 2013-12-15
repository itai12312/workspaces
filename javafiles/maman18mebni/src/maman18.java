package maman18mebni;
import java.util.ArrayList;
import java.util.Scanner;
public class maman18{
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
	    MaxHeap numofbooks=new MaxHeap();
	    RedBlackTree books=new RedBlackTree();
	    RedBlackTree members=new RedBlackTree();
	    String text=in.nextLine();
	    while(!text.equals("exit")){
	    	String text1=text.substring(0,text.indexOf(" "));
	    	text=text.substring(text.indexOf(" ")+1);
	    	if(text1.charAt(0)>='a' && text1.charAt(0)<='Z'){
	    		String text2=text.substring(0,text.indexOf(" "));
		    	text=text.substring(text.indexOf(" ")+1);
	    		String text3=text.substring(0,text.indexOf(" "));
		    	text=text.substring(text.indexOf(" ")+1);
		    	if(text.endsWith("+")){
		    		if(books.isFound(text3)==false&&members.getNumBooks(text2)<10)
		    			int booksnum=members.increasebyonenumofbooks(text2);
		    			numofbooks.updateBookCount(text2,booksnum);//which id,what to update to
		    			books.add(text3,text2);//bookname,person
	    		}else if(text.endsWith("-")){
	    			int books=members.decreasebyonenumofbooks(text2);
	    			numofbooks.updateBookCount(text2,books);//which id,what to update to
	    			books.remove(text3);//bookname,person
	    		}
	    	}else if(text1.equals("+")){
	    		String text2=text.substring(0,text.indexOf(" "));
		    	text=text.substring(text.indexOf(" ")+1);
	    		String text3=text.substring(0,text.indexOf(" "));
	    		
	    		members.add(text3,text2);//id,name
	    		numofbooks.add(text3,0);//id,numbooks
	    	}else if(text1.equals("-")){
	    		String text2=text.substring(0,text.indexOf(" "));
		    	text=text.substring(text.indexOf(" ")+1);
	    		String text3=text.substring(0,text.indexOf(" "));
	    		for(book in members.getbooks(text3))
	    			books.remove(book); 
	    		members.remove(text3);//by id
	    		numofbooks.remove(text3);
	    	}else if(text1.equals("?")){
	    		String text2=text.substring(0,text.indexOf(" "));
	    		if(text2.charAt(0)>='a' && text2.charAt(0)<='Z'){
	    			books.findPerson(text2);
	    		}else if(text2.charAt(0)>='1' && text2.charAt(0)<='9'){
	    			members.findBooks(text2);
	    		}else if(text2.equals("!")){
	    			numofbooks.findMaxPersons();
	    		}
	    	} 	
	    	text=in.nextLine();
	    }
	}
}
