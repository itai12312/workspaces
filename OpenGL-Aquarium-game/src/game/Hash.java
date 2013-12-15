package game;

public  class Hash {
	public static String MD5(String md5) { 
		try {       
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");  
			byte[] array = md.digest(md5.getBytes());  
			StringBuffer sb = new StringBuffer();  
			for (int i = 0; i < array.length; ++i) {   
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));      
				}
			//int d=hashToInt(sb.toString());
			//System.out.println(d);
			//System.out.println(percentile(d));w
			return sb.toString(); 
				} catch (java.security.NoSuchAlgorithmException e) {   
					e.printStackTrace();
				}   
		return null; 
	}
	
	public static int hashToInt(String md5){
		int sum=0;
		//System.out.println(md5+"hashToInt");
		//System.out.println("char "+"value "+"pos  "+"radix pox      "+"equals    "+" sum  ");
		for(int i=31;i>-1;i--){
			//System.out.println(md5.charAt(i)+"      "+charToInt(md5.charAt(i))+"    "+i+"    "+(int)(Math.pow(16,32-i))+"   "+(int)(charToInt(md5.charAt(i))*Math.pow(16,32-i))+"   "+sum);
			//System.out.print("char: "+charToInt(md5.charAt(i))+" ");
			sum+=(int)(charToInt(md5.charAt(i))*(int)(Math.pow(16,32-i)));
		}
		//System.out.println("hash of:"+md5+" is :"+sum);
		return sum;
	}
	public static int charToInt(char a1){
		//System.out.println("charOtint:"+a1);
		switch(a1){
			case '0':return 0;
			case '1':return 1;
			case '2':return 2;
			case '3':return 3;
			case '4':return 4;
			case '5':return 5;
			case '6':return 6;
			case '7':return 7;
			case '8':return 8;
			case '9':return 9;
			case 'a':return 10;
			case 'b':return 11;
			case 'c':return 12;
			case 'd':return 13;
			case 'e':return 14;
			case 'f':return 15;
			
		}
		return 0;
		
	}

	public static int percentile(int c){
		while(c>1000000000){
			c=c/10;
		}
		while(c<-1000000000){
			c=c/10;
		}
		return c;	
	}
	
	public static int hash1(String d){
		return hashToInt(d);
	}
	
	public static int hash2(String d){
		return percentile(hash1(d));
	}
	public static int hash3(String s){
		return hash2(MD5(s));
	}
}
