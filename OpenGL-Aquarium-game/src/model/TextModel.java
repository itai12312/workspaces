package model;

public class TextModel extends Model {
	public double centerX,centerY;
	private boolean isCentered;
	public TextModel(double x, double y) {
		super();
		centerX=x;
		centerY=y;
		isCentered=false;
	}
	public void center() {
		double posx,posy,posz;
		if (!isCentered) {
			isCentered=true;
			posx=posX;
			posy=posY;
			posz=posZ;
			setPos(-centerX,-centerY,0.0);
			fixTrasform();
			setPos(posx,posy,posz);
		}
	}
}
