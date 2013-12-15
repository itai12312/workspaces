package allahakberhw;
public class use
{
	public static void main(String[] args)
	{
		TestTable tt=new TestTable(1, new Test[]{new Test("Math", new Date(1,1,2011))});
		tt.addTest(new Test("English", new Date(5,3,2011)));
		tt.addTest(new Test("OOP", new Date(8,4,2011)));
	}

}
