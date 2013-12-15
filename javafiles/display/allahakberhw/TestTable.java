package allahakberhw;
public class TestTable
{
	private Test arr[]=new Test[10];
	private int numTest=0;
	
	public TestTable(int numTest, Test[] tests)
	{
		this.numTest=numTest;
		for (int i=0;i<numTest;i++)arr[i]=tests[i];
	}
	
	public int getNumTest(){return numTest;}
	public Test getTest(int index){return arr[index];}
	public void setTest(Test t ,int index){this.arr[index]=t;}
	public void addTest(Test t){this.numTest++; this.arr[numTest-1]=t;}
	
	public String toString ()
	{
		String s=numTest+" tests:";
		for (int i=0;i<numTest;i++)
			s+="\n"+arr[i];
		return s;
	}
	
	public int testsOnDate(Date d)
	{
		int c=0;
		for (int i=0;i<numTest;i++)
			if (arr[i].getTestDay().compareTo(d)==0)
				c++;
		return c;
	}
	
	public boolean testTableOK()
	{
		for (int i=0;i<numTest;i++)
			for (int j=i+1;i<numTest;j++)
				if (arr[i].getTestDay().compareTo(arr[j].getTestDay())==0)
					return false;
		return true;
	}
}
