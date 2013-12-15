package allahakberhw;

import java.util.Scanner;

public class assigment1 {
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
	assigment1 firsttown=new assigment1(0,0,"first");
	assigment1 secondtown=new assigment1(10,4,"second");
	assigment1 thirdtown=new assigment1(3,8,"third");
	firsttown.setName(in.next());
	System.out.println(thirdtown.toString());
		
	}
	public int getPopulation() {
		return population;
	}
	public void setPopulation(int population) {
		this.population = population;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private int population;
	private int size;
	protected String name;
	
	public assigment1(int population,int size,String name){
		this.name=name;
		this.population=population;
		this.size=size;	
	}
	public int occupany(){
		return (this.population>this.size)?100:this.population/this.size;
	}
	
	public String toString(){
		String message="name: "+this.name+" population:"+this.population+" size:"+this.size+" occupancy: "+occupany();
		return message;
		
	}
	public void reset(){
		this.name="";
		this.size=0;
		this.population=0;
	}
}
