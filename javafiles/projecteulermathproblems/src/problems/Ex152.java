package problems;

public class Ex152 {
	public static void main(String[]args){
		for (int i = 2; i < 50; i++) {
			for(int p=0;p<40;p++){
			int[]nums=new int[i];
			int j=0;//!isEmpty(nums)&&
				while(!isEmpty(nums)&&j<i){
					int number=(int)(Math.random()*79)+2;
					//System.out.println(number);
					if(!inArray(nums,number))
						nums[j]=number;
					j++;
				}
				for (int l=0;l<nums.length ; l++)
				{System.out.print(nums[l]+" ");}
				System.out.println(" ");
				if(sum(nums)==0.5)
					System.out.print(nums);
			}
		}
	}
	private static double sum(int[] nums) {
		double sum=0;
		for (int i : nums) {
			sum+=(1/i)*(1/i);
		}
		return sum;
	}
	private static boolean isEmpty(int[] nums) {
		for(int i=0;i<nums.length;i++){
			while(nums[i]==0){
				int number=(int)(Math.random()*79)+2;
				if(!inArray(nums,number))
					nums[i]=number;
			}
				
				
		}
			return true;
	}
	private static boolean inArray(int[]nums,int num){
		for (int i : nums) {
			if(i==num)
				return true;
		}
		return false;
	}
	
}
