import java.util.Scanner;
import unit4.collectionsLib.Node;
public class t220 {
Scanner in=new Scanner(System.in);
	public static void main(String[] args) {

		//222 targil 8
		//2233 targil 11
		
		
		
		
	}
	public void  t1a(){Node<Integer>a=new Node<Integer>(7);
	Node<Integer>b=new Node<Integer>(21);
	Node<Integer>c=new Node<Integer>(4);
	
	a.setNext(b);
	b.setNext(c);
	c.setNext(null);}
	public void t1b(){int n=(int) (Math.random()*99+2);
	Node<Integer> a=new Node<Integer>(1);
	Node<Integer>b=new Node<Integer>(2);
	for(int i=3;i<=n;i++){
		a.setNext(b);
		a=b;
		b=new Node<Integer>(i);
	}}
	public void t1c(){
		Node<Integer> a=new Node<Integer>(1);
		Node<Integer>b=new Node<Integer>(2);
		for(int i=3;i<=;i++){
			a.setNext(b);
			a=b;
			b=new Node<Integer>(i);
		}
		
	}
	public String getWord(int i,String str){
		if(i>str.length())
			return "";
		String word="";
		while(str.charAt(i)!=' '){
			word+=str.charAt(i);
		}
		return str;
	}
	public void t2(){}
	public void t3(){}
	public void t4(){}
	public void a222t8(){}
	public void a223t11(){}

}
