package allahakberhw;
public class Date
{
		private int day,month,year;
		
		public Date(int day,int month,int year)
		{
			this.day=day;
			this.month=month;
			this.year=year;
		}
		
		public void setDay(int day){this.day=day;}
		public int getDay(){return day;}
		
		public void setMonth(int month){this.month=month;}
		public int getMonth(){return month;}

		public void setYear(int year){this.year=year;}
		public int getYear(){return year;}
		
		public String toString(){return day+"."+month+"."+year;}
		
		public int compareTo(Date other)
		{
			if(this.year>other.year)return 1;
			else if (this.year<other.year)return -1;
				else if (this.month>other.month)return 1;
					else if (this.month<other.month)return -1;
						else if (this.day>other.day)return 1;
							else if (this.day<other.day)return -1;
			return 0;
		}
	}

