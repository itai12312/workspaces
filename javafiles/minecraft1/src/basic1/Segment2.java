package basic1;
/**
 * A segment2 represent a line (parallel to the x-axis) using a center point and length
 * @author Itai Zitak 302390836
 * @version 21.4.12
 */
public class Segment2{
    private Point _poCenter;//represents center point
    private double _length;//represents right point

    /**
     * Constructs a segment. Constructs a new segment using two Points
     * @param left the left point of the segment
     * @param right the right point of the segment
     */
    public Segment2(Point left,Point right){
       this._poCenter=new Point((left.getX()+right.getX())/2,left.getY());
       this._length=right.getX()-left.getX();
       
    }
    /**
     * Constructs a segment. Constructs a new segment using 4 specified x y coordinates: two coordinates for the left point and two coordinates for the right point
     *@param leftX X value of left point
     *@param leftY Y value of left point
     *@param rightX X value of right point
     *@param rightY Y value of right point
    */
    public Segment2(double leftX ,double leftY,double rightX ,double rightY){
    this._poCenter=new Point((leftX+rightX)/2,leftY);
    this._length=rightX-leftX;
    }
    /**
     * Copy Constructor. Construct a segment using a reference segment
     * @param other the reference segment
     */
    public Segment2(Segment2 other){
        this._poCenter=new Point(other.getPoLeft().getX()+other.getLength()/2,other.getPoLeft().getY());
        this._length=other.getLength();
    }
    /**
     * Constructs a segment. Constructs a new segment using a center point and the segment length 
     *@param poCenter the Center Point
     *@param length the segment length
    */
    public Segment2(Point poCenter,double length){
        this._poCenter=new Point(poCenter);
        this._length=length;
    }
    
    //this function returns the center point
    private Point getPoCenter(){
    	return _poCenter;
    }
    //this function changes the length by delta
    private void changeLength(double delta){
    	_length+=delta;
    }
    /**
     * Returns the left point of the segment
     * @return The left point of the segment
     */
    public Point getPoLeft(){
        return new Point(getPoCenter().getX()-getLength()/2,getPoCenter().getY());
    }
    /**
     * Returns the right point of the segment
     * @return The right point of the segment
     */
    public Point getPoRight(){
        return new Point(getPoCenter().getX()+getLength()/2,getPoCenter().getY());
    }
    /**
     * Returns the segment length
     * @return The segment length
     */
    public double getLength(){
        return _length;
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
    public boolean equals(Segment2 other){
        if(this.getPoLeft().equals(other.getPoLeft())&&this.getPoRight().equals(other.getPoRight()))
            return true;
        return false;
    }
    /**
     * Check if this segment is above a reference segment
     *@param other the reference segment
     *@return True if this segment is above the reference segment
     */
    public boolean isAbove(Segment2 other){
        return this.getPoLeft().isAbove(other.getPoLeft());
    }
    /**
     * Check if this segment is under a reference segment.
     *@param other the reference segment
     *@return True if this segment is under the reference segment
     */
    public boolean isUnder(Segment2 other){
        return other.isAbove(this);
    }
    /**
     * Check if this segment is left of a reference segment
     *@param other the reference segment
     *@return True if this segment is left to the reference segment
     */
    public boolean isLeft(Segment2 other){
        return this.getPoLeft().isLeft(other.getPoLeft());
    }
    /**
     * Check if this segment is right of a reference segment.
     *@param other the reference segment
     *@return True if this segment is right to the reference segment
     */
    public boolean isRight(Segment2 other){
        return other.isLeft(this);
    }
    /**
     * Move the segment horizontally by delta
     *@param delta the displacement size
     */
    public void moveHorizontal(double delta){
       getPoCenter().move(delta,0);
       
    }
    /**
     * Move the segment vertically by delta
     *@param delta the displacement size
     */
    public void moveVertical(double delta){
    	getPoCenter().move(0,delta);
    }
    /**
     * Change the segment size by moving the right point by delta
     * Will be done only for valid delta:
     * only if the new right point remains the right point
     *@param delta the change size
     *
     */
    public void changeSize(double delta){
        if(getLength()<delta){
            this.getPoCenter().move(delta/2,0);
            changeLength(delta);
        }
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
    public boolean isBigger(Segment2 other){
    	if(getLength()>other.getLength())
    		return true;
    	return false;
    }
    /**
     * Returns the overlap size of this segment and a reference segment 
     *@param other the reference segment 
     *@return The overlap size
     */
    public double overlap(Segment2 other){
    	if(other.getPoRight().isLeft(getPoLeft())||other.getPoLeft().isRight(getPoRight()))
    		return 0;
    	return Math.min(getPoRight().distance(other.getPoLeft()),other.getPoRight().distance(getPoLeft())); 
    }
    /**
     * Compute the trapeze perimeter, which constructed by this segment and a reference segment 
     *@param other the reference segment
     *@return The trapez perimeter
     */
    public double trapezePerimeter(Segment2 other){
    	if(other.getPoLeft().isAbove(getPoLeft())||other.getPoLeft().isUnder(getPoLeft()))
    		return other.getPoLeft().distance(getPoLeft())+other.getPoRight().distance(getPoRight())+getLength()+other.getLength();
    	return 0;
    }
}
