package Programming;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
public class programming1{
	static Scanner in=new Scanner(System.in);
	static ArrayList<String>which=new ArrayList<String>();
	static ArrayList<String>againstwhat=new ArrayList<String>();
	public static void main(String[] args) {
	long time=System.currentTimeMillis();
	kelet("C:\\Users\\AZ\\Downloads\\wordlist\\wordlist.txt",againstwhat);
	kelet("C:\\Users\\AZ\\Downloads\\words.txt",which);
	System.out.println((System.currentTimeMillis()-time));
	//System.out.println(which);
	//System.out.println(againstwhat);
	for(int i=0;i<which.size();i++){
		for(int j=0;j<againstwhat.size();i++){
			System.out.print(scramble(which.get(i),againstwhat.get(j)));
		}
	}
	
	}
	
	private static void kelet(String path,ArrayList<String>words){
		
		//"C:\Users\AZ\Downloads\wordlist.zip\wordlist";
		BufferedReader in=null;
		System.out.println(path);
		try {
			in = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    String str = "";
	    while (str != null) {
	        try {
				str = in.readLine();
				if(str!=null&&!str.equals("")){				
					words.add(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		//while(in,)
		
	}

	public static String scramble(String word,String wordfromdictionary){
		String str=wordfromdictionary;
		if(word.length()==wordfromdictionary.length()){
			while(!word.equals("")){
				//char a=word.charAt(0);
				if(!wordfromdictionary.replaceFirst(word.charAt(0)+"","").equals(wordfromdictionary)){
					return "";
				}
				word.replaceFirst(word.charAt(0)+"","");
			}
			if(wordfromdictionary.equals("")){
				return str+",";
			}else{
				return "";
			}
			
		}
		return "";
	}
}