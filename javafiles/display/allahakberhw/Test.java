package allahakberhw;
public class Test
{
	private String subject;
	private Date testDay;
	
	public Test(String subject, Date testDay)
	{this.subject=subject;this.testDay=testDay;}

	public void setSubject(String subject){this.subject=subject;}
	public void setTestDay(Date testDay){this.testDay=testDay;}
	
	public String getSubject(){return subject;}
	public Date getTestDay(){return testDay;}
	
	public String toString(){return "subject="+subject+" | test day="+testDay;}
	
	public boolean testsSameDate(Test test)
	{return this.testDay.compareTo(test.testDay)==0;}
}
