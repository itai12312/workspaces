package modx;

import java.util.Scanner;

public class javascript1 {
static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {

	String	al="`1234567890-=~!@#$%^&*()_+qwer"
				+"tyuiop[]QWERTYUIOP{}|asdfghjkl;A"
				+"SDFGHJKL:zxcvbnm,./ZXCVBNM<>?";
				String ab1="";
				int bctr=0;
			
				 String tst=in.nextLine() +"*"+in.nextLine()+"*";
				 String ls="9999621534361185176113439965168437841040414209";
				 String a="8";
				 ls="99621534361185176113439965168437841040414209";
				 String nls="";
				 int flg=0;
				 while (ls.length()>12){
				  int ab=Interger.parseInt(ls.substring(0,2))-89;
				  ab1=(ab1==""?""+ab:ab1);
				  String oab1=ab1;
				  ls=ls.substring(2,ls.length);
				  for (var i=0;i<ab;i++){
				   nr=eval(ls.substring(0,2))-a;
				   ls=ls.substring(2,ls.length);
				   nls+=al.charAt(nr);
				   }
				  nls+="*";
				  if (nls.indexOf(tst)>-1){
				   ls="";
				   flg=1;
				   }
				  }
				 if (flg==1){
				  tstOk();
				  }
				 else{
				  bctr++;
				  if (bctr>3){
				   location.href="wrongpage.php";
				   }
				  else{
				   alert("Sorr</abad Username or Password."
				   +" Failed Attempt #"+bctr+".");
				   }
				  }
				 

	
	}
	
}
