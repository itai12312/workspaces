package basic1;
/**
 * A segment1 represent a line (parallel to the x-axis) using two Points 
 * @author Itai Zeitak 302390836 
 * @version 21.4.12
 */
public class Segment1{
    private Point _poLeft;//represents left point
    private Point _poRight;//represents right point
    public static void main(String[]args){
    	System.out.println("********** Test Q2 Segment1 - Start **********");
		Segment1 seg0 = null; // (0.0,0.0)---(2.0,0.0)
		Segment1 seg2 = null;
		Segment1 seg3 = null; // seg2 = seg3 = (1.0,4.0)---(4.0,4.0)
		// constructor and toString
		seg0 = new Segment1(0.0, 0.0, 2.0, 0.0);
		if (!seg0.getPoLeft().equals(new Point(0.0, 0.0))) {
			System.out.println("\t ERROR - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());
		} else
			System.out.println("\t OK - expected seg0.getPoLeft()=(0.0,0.0) ; actual="
							+ seg0.getPoLeft());

		// second constructor Segment1 (Point left, Point right)
		Point pLeft = new Point(1.0, 4.0);
		Point pRight = new Point(4.0, 8.0);
		seg2 = new Segment1(pLeft, pRight);
		if (!seg2.getPoLeft().equals(pLeft) || !seg2.getPoRight().equals(new Point(4.0, 4.0))) {
			System.out.println("\t ERROR - second Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg2);
		} else
			System.out.println("\t OK - second Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg2);

		// copy constructor
		seg3 = new Segment1(seg2);
		if (!seg3.getPoLeft().equals(new Point(1.0, 4.0))|| !seg3.getPoRight().equals(new Point(4.0, 4.0))) {
			System.out.println("\t ERROR - 3rd Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg3);
		} else
			System.out.println("\t OK - 3rd Constructor - expected (1.0,4.0)---(4.0,4.0) ; actual="
							+ seg3);

		// equals

		if (!seg2.equals(seg3)) {
			System.out.println("\t ERROR - equals - seg2.equals(seg3)? - expected true ; actual="
							+ seg2.equals(seg3));
		} else
			System.out.println("\t OK - equals - seg2.equals(seg3)? - expected true ; actual="
							+ seg2.equals(seg3));

		// sizes
		if (seg0.getLength() != 2.0) {
			System.out.println("\t ERROR - seg0.getLength() - expected 2.0 ; actual="
							+ seg0.getLength());
		} else
			System.out.println("\t OK - seg0.getLength() - expected 2.0 ; actual="
							+ seg0.getLength());

		Segment1 s1 = new Segment1(0.0, 0.0, 2.0, 0.0);
		s1.changeSize(3.0);
		if (s1.getLength() != 5.0) {
			System.out.println("\t ERROR - s1.changeSize() - expected length 5.0 ; actual="
							+ s1.getLength());
		} else
			System.out.println("\t OK - s1.getLength() - expected length 5.0 ; actual="
							+ s1.getLength());

		s1 = new Segment1(0.0, 0.0, 2.0, 0.0);
		Segment1 s2 = new Segment1(0.0, 2.0, 4.0, 2.0); // bigger than s1
		if (s1.isBigger(s2)) {
			System.out.println("\t ERROR - s1.isBigger(s2) - expected false ; actual="
							+ s1.isBigger(s2));
		} else
			System.out.println("\t OK - s1.isBigger(s2) - expected false ; actual="
							+ s1.isBigger(s2));

		// under, above, right & left
		s1 = new Segment1(5.0, 0.0, 10.0, 0.0);
		Segment1 s3 = new Segment1(5.0, 3.0, 10.0, 3.0);
		Segment1 s4 = new Segment1(5.0, -3.0, 10.0, -3.0);
		// under
		if (!s1.isUnder(s3)) {
			System.out.println("\t ERROR - s1.isUnder(s3) - expected true ; actual="
							+ s1.isUnder(s3) + " s1=" + s1 + " s3=" + s3);
		} else
			System.out.println("\t OK - s1.isUnder(s3) - expected true ; actual="
							+ s1.isUnder(s3) + " s1=" + s1 + " s3=" + s3);
		// above
		if (!s1.isAbove(s4)) {
			System.out.println("\t ERROR - s1.isAbove(s4) - expected true ; actual="
							+ s1.isAbove(s4) + " s1=" + s1 + " s4=" + s4);
		} else
			System.out.println("\t OK - s1.isAbove(s4) - expected true ; actual="
							+ s1.isAbove(s4) + " s1=" + s1 + " s4=" + s4);

		Segment1 sMid = new Segment1(5.0, 0.0, 10.0, 0.0);
		Segment1 sLeft = new Segment1(0.0, 3.0, 4.0, 3.0);
		Segment1 sRight = new Segment1(11.0, -3.0, 15.0, -3.0);

		// left
		if (!sMid.isRight(sLeft)) {
			System.out.println("\t ERROR - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);
		} else
			System.out.println("\t OK - sMid.isRight(sLeft) - expected true ; actual="
							+ sMid.isRight(sLeft)+ " sMid="+ sMid+ " sLeft="+ sLeft);
		// right
		if (!sMid.isLeft(sRight)) {
			System.out.println("\t ERROR - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);
		} else
			System.out.println("\t OK - sMid.isLeft(sRight) - expected true ; actual="
							+ sMid.isLeft(sRight)+ " sMid="+ sMid+ " sRight=" + sRight);

		// overlap
		if (sMid.overlap(sMid) != 5.0) {
			System.out.println("\t ERROR - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);
		} else
			System.out.println("\t OK - sMid.overlap(sMid) - expected 5.0 ; actual="
							+ sMid.overlap(sMid)+ " sMid="+ sMid+ " sMid="+ sMid);

		if (sMid.trapezePerimeter(sMid) != 10.0) {
			System.out.println("\t ERROR - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);
		} else
			System.out.println("\t OK - sMid.trapezePerimeter(sMid) - expected 10.0 ; actual="
							+ sMid.trapezePerimeter(sMid)+ " sMid="+ sMid+ " sMid=" + sMid);

		System.out.println("********** Test Q2 Segment1 - Finish **********\n");
    }
    /**
     * Constructs a segment. Constructs a new segment using two Points
     * @param left the left point of the segment
     * @param right the right point of the segment
     */
    public Segment1(Point left,Point right){
       right.setY(left.getY());
       this._poLeft=left;
       this._poRight=right;
       
    }
    /**
     * Constructs a segment. Constructs a new segment using 4 specified x y coordinates: two coordinates for the left point and two coordinates for the right point
     *@param leftX X value of left point
     *@param leftY Y value of left point
     *@param rightX X value of right point
     *@param rightY Y value of right point
    */
    public Segment1(double leftX ,double leftY,double rightX ,double rightY){
    this._poLeft=new Point(leftX,leftY);
    this._poRight=new Point(rightX,leftY);
    }
    /**
     * Copy Constructor. Construct a segment using a reference segment
     * @param other the reference segment
     */
    public Segment1 (Segment1 other){
        this._poLeft=new Point(other.getPoLeft());
        this._poRight=new Point(other.getPoRight());
    }
    /**
     * Returns the left point of the segment
     * @return The left point of the segment
     */
    public Point getPoLeft(){
        return _poLeft;
    }
    /**
     * Returns the right point of the segment
     * @return The right point of the segment
     */
    public Point getPoRight(){
        return _poRight;
    }
    /**
     * Returns the segment length
     * @return The segment length
     */
    public double getLength(){
        return getPoRight().distance(_poLeft);
    }
    /**
     * Return a string representation of this segment. i.e. (3,4)---(3,6)
     *@override toString in class java.lang.Object
     *@return String representation of this segment 
     */
    public String toString(){
        return getPoLeft().toString()+"---"+getPoRight().toString();
    }
    /**
     * Check if the reference segment is equal to this segment
     *@param other the reference segment
     *@return True if the reference segment is equal to this segment
     */
    public boolean equals(Segment1 other){
        if(this.getPoLeft().equals(other.getPoLeft())&&this.getPoRight().equals(other.getPoRight()))
            return true;
        return false;
    }
    /**
     * Check if this segment is above a reference segment
     *@param other the reference segment
     *@return True if this segment is above the reference segment
     */
    public boolean isAbove(Segment1 other){
        return this.getPoLeft().isAbove(other.getPoLeft());
    }
    /**
     * Check if this segment is under a reference segment.
     *@param other the reference segment
     *@return True if this segment is under the reference segment
     */
    public boolean isUnder(Segment1 other){
        return other.isAbove(this);
    }
    /**
     * Check if this segment is left of a reference segment
     *@param other the reference segment
     *@return True if this segment is left to the reference segment
     */
    public boolean isLeft(Segment1 other){
        return this.getPoLeft().isLeft(other.getPoLeft());
    }
    /**
     * Check if this segment is right of a reference segment.
     *@param other the reference segment
     *@return True if this segment is right to the reference segment
     */
    public boolean isRight(Segment1 other){
        return other.isLeft(this);
    }
    /**
     * Move the segment horizontally by delta
     *@param delta the displacement size
     */
    public void moveHorizontal(double delta){
        this.getPoLeft().move(delta,0);
        this.getPoRight().move(delta,0);
    }
    /**
     * Move the segment vertically by delta
     *@param delta the displacement size
     */
    public void moveVertical(double delta){
        this.getPoLeft().move(0,delta);
        this.getPoRight().move(0,delta);
    }
    /**
     * Change the segment size by moving the right point by delta
     * Will be done only for valid delta:
     * only if the new right point remains the right point
     *@param delta the change size
     *
     */
    public void changeSize(double delta){
        if(getLength()<delta)
            this.getPoRight().move(delta,0);
    }
    /**
     * Check if a point is located on the segment 
     *@param p a point to be checked 
     *@return True if p is on this segment
     */
    public boolean pointOnSegment(Point p){
        if((!p.isLeft(getPoRight())&&!p.isRight(getPoRight()))||(!p.isLeft(getPoLeft())&&!p.isRight(getPoLeft()))||(p.isLeft(getPoRight())&&getPoLeft().isLeft(p))){
        	if(!p.isUnder(getPoLeft())&&!p.isAbove(getPoLeft()))
        		return true;
        }
        return false;
    }
    /**
     * Check if this segment is bigger then a reference segment 
     *@param other the reference segment 
     *@return True if this segment is bigger than the reference segment
     */
    public boolean isBigger(Segment1 other){
    	if(getLength()>other.getLength())
    		return true;
    	return false;
    }
    /**
     * Returns the overlap size of this segment and a reference segment 
     *@param other the reference segment 
     *@return The overlap size
     */
    public double overlap(Segment1 other){
    	if(other.getPoRight().isLeft(getPoLeft())||other.getPoLeft().isRight(getPoRight()))
    		return 0;
    	return Math.min(getPoRight().distance(other.getPoLeft()),other.getPoRight().distance(getPoLeft())); 
    }
    /**
     * Compute the trapeze perimeter, which constructed by this segment and a reference segment 
     *@param other the reference segment
     *@return The trapez perimeter
     */
    public double trapezePerimeter(Segment1 other){
    	if(other.getPoLeft().isAbove(getPoLeft())||other.getPoLeft().isUnder(getPoLeft()))
    		return other.getPoLeft().distance(getPoLeft())+other.getPoRight().distance(getPoRight())+getLength()+other.getLength();
    	return 0;
    }
}
