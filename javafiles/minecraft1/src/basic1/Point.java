package basic1;
/**
 * Represents 2 dimensional integral points on a map.
 * 
 * @author Itai Zitak 302390836
 * @version 21.4.12
 */
public class Point{
    private double _x;//The x coordiante of the point
    private double _y;//The y coordiante of the point
    /**
     *  Constructs a point. Construct a new point with the specified x y coordinates. 
     *  @param x The x coordinate
     *  @param y The y coordinate
     */
    public Point(double x,double y){
     this._x=x;
     this._y=y;
    }
    
    /**
     * Copy constructor for Points. Construct a point with the same coordinates as another point.
     * @param other The point object from which to construct the new point
     */
    public Point(Point other){
        this._x=other._x;
        this._y=other._y;
    }
    /**
     * Returns the x coordinate of the point.
     * @return The x coordiante of the point
     */
    public double getX(){
        return _x;
    }
    /**
     * Returns the y coordinate of the point.
     * @return The y coordiante of the point
     */
    public double getY(){
        return _y;
    }
    /**
     * Changes the x coordinate of the point.
     * @param num The new x coordinate
     */
    public void setX(double num){
        _x=num;
    }
   /**
     * Changes the y coordinate of the point.
     * @param num The new y coordinate
     */
    public void setY(double num){
        _y=num;
    }
    /**
     * Check if the received point is equal to this point.
     * @param other The point to be compared with this point
     * @return True if the received point is equal to this point
     */
    public boolean equals(Point other){
        if(this._x==other._x&&this._y==other._y)
            return true;
           return false;
    }
    /**
     * Check if this point is above a received point.
     * @param other The point to check if this point is above
     * @return True if this point is above other point
     */
    public boolean isAbove(Point other){
        if(this._y>other._y)
            return true;
       return false;
    }
   /**
     * Check if this point is below a received point.
     * @param other The point to check if this point is below
     * @return True if this point is below other point
     */
    public boolean isUnder(Point other){
        return other.isAbove(this);
    }
   /**
     * Check if this point is left of received point.
     * @param other The point to check if this point is left of
     * @return True if this point is left of other point
     */
    public boolean isLeft(Point other){
       if(other._x>this._x)
            return true;
      return false;
    }
    /**
     * Check if this point is right of received point.
     * @param other The point to check if this point is right of
     * @return True if this point is right of other point
     */
    public boolean isRight(Point other){
        return other.isLeft(this);
    }
    /**
     * Check the distance between this point and a received point.
     * @param other The point to check distance from
     * @return double representing the distance
     */
    public double distance(Point p){
        return Math.sqrt(Math.pow((p._x-this._x),2)+Math.pow((p._y-this._y),2));
    }
    /**
     * Move the x coordinate by dX and the y coordinate by dY.
     * @param dX The amount to move x
     * @param dY The amount to move y
     * @return double representing the distance
     */
    public void move(double dx,double dy){
        this._x+=dx;
        this._y+=dy;
    }
     /**
     * Return a string representation of this point. i.e. (3,4)
     * @override toString in class java.lang.Object
     * @return String representation of this point
     */
    public String toString(){
        return "("+_x+","+_y+")";
    }
    
   
}
