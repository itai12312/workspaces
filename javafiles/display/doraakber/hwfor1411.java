package doraakber;

import unit4.collectionsLib.Stack;

public class hwfor1411<T> {
	public void amud246targil1(){
		//א
		//s1='a','b','c','e'
		//s2=ריק
		//ב
		//s1,s1=1,2,3,4,5,6
	}
	public static void amud247targil3(){
		unit4.collectionsLib.Stack<Integer> st1=new Stack<Integer>();
		st1.push(8);
		st1.push(2);
		st1.push(1);
		st1.push(7);
		st1.push(3);
		unit4.collectionsLib.Stack<Integer> st2=new Stack<Integer>();
		int x,y;
		while(!st1.isEmpty()){
			x=st1.pop();
			if(!st1.isEmpty()){
				y=st1.pop();
				st2.push(y);
			}
			else
				st2.push(x);
			st2.push(x);
			
		}
		System.out.println("st2= "+st2);
	}
	public static void main(String[]args){
		amud247targil3();
	}
	
	public T amud251targil14a(int k,Stack<T> a){
		Stack<T>b=new Stack<T>();
		int count=0;
		while(!a.isEmpty()){
			b.push(a.pop());
			count++;
		}
		int number=1;
		while(number<count){
			b.pop();
		}
		return b.pop();
	}
	public void amud251targil14b(int k,Stack<T> a){
		Stack<T>b=new Stack<T>();
		int count=0;
		while(!a.isEmpty()&&count<length(a)-k){
			b.push(a.pop());
		}
		while(!b.isEmpty()){
			a.push(b.pop());
		}
		
	}
	public int length(Stack<T>a){
		int count=0;
		while(!a.isEmpty()){
			a.pop();
			count++;
		}
		return count;
	}
}
