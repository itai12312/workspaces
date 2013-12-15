package allahakberhw;
public class testTest
{
	public static void main(String[] args)
	{
		Test t1=new Test("Math", new Date(1,10,2011));
		Test t2=new Test("PhysX", new Date(4,9,2011));
		Test t3=new Test("OOP", new Date(22,11,2011));
		
		System.out.println(t1+"\n"+t3);
		t2.setTestDay(t3.getTestDay());
		System.out.println(t2.testsSameDate(t3));
	}

}
