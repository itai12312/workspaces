#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
//this program gets commands from the user, and runs them on the matrixs using the mat.c class
int main(int argc, char *argv[]){
	char *str="",*str1="";
	scanf("%s",str);
	while(str!="stop"){
		//printf("%s",str);
		if(str=="read_mat"||str=="add_mat"||str=="sub_mat"||str=="mult_mat"||str=="mult_scalar"||str=="trans_mat"||str=="print_mat"){
			scanf("%s",str1);
			if(numberofinstances(str,",",0,strlen(str1))==0&&(str=="read_mat"||str=="print_mat")){
				if(str="read_mat"){
					read_mat(str1);
				}else{
					print_mat(str1);
				}
			}
			else if(numberofinstances(str1,",",0,strlen(str1))==1&&str=="trans_mat"){
				trans_mat(substr(str1,0,find(str1,",",0,strlen(str1))),substr(str1,find(str1,",",0,strlen(str1)),strlen(str1)));                                                                               
			}else if(numberofinstances(str1,",",0,strlen(str1))==2){
				char* str2=substr(str1,0,find(str1,",",0,strlen(str1)));
				char* str3=substr(str1,find(str1,",",0,strlen(str1)+1,find(str1,",",find(str1,",",0,strlen(str1))+1,strlen(str1))));
				char* str4=substr(str1,find(str1,",",find(str1,",",0,strlen(str1))+1,strlen(str1)),strlen(str1));    
				if(str="add_mat"){
					add_mat(str2,str3,str4);
				}else if(str="sub_mat"){
					sub_mat(str2,str3,str4);
				}else if(str="mul_mat"){
					mul_mat(str2,str3,str4);
				}else if(str="mul_scalar"){
					mul_scalar(str2,str3,str4);
				} 
			} 
		}        
		scanf("%s",str);
	}
	system("PAUSE");	
	return 0;
}
