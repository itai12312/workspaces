package moredemos;
public class Trans {
	public double[]trans;
	public String type="";
	public void updateTrans(int location,int trans1){
		if(location<trans.length){
			trans[location]=trans1;
		}
	}
	public Trans(double[]trans, String type) {	
			this.trans = trans;
		if(type.equals("rotate")||type.equals("translate"))
		this.type = type;
	}
	
}