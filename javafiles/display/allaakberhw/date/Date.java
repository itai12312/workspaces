package p2;
public class Date
{
	private int year; private int month; private int day;

	public Date(int day, int month, int year)
	{
		this.day=day;
		this.month=month;
		this.year=year;
	}
	
	public int getYear(){return year;}
	public int getMonth(){return month;}
	public int getDay(){return day;}
	
	public void setYear(int year){this.year=year;}
	public void setMonth(int month){this.month=month;}
	public void setDay(int day){this.day=day;}
	
	public int compareTo(Date other)
	{
		if (this.year>other.year) return 1;
		if (this.year<other.year) return -1;
		if (this.month>other.month) return 1;
		if (this.month<other.month) return -1;
		if (this.day>other.day) return 1;
		if (this.day<other.day) return -1;
		return 0;
	}
	
	public String toString()
	{
		return "<"+day+">.<"+month+">.<"+year +">";
	}
}
