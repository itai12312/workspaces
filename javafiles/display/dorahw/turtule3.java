package start;
import unit4.turtleLib.*;
public class turtule3 {
	public static void main(String[]args){
	Turtle turtle= new Turtle();
	turtle.tailDown();
	turtle.turnRight(90);
	turtle.moveForward(100);
turtle.turnLeft(90);
turtle.moveForward(100);
turtle.turnLeft(90);
turtle.moveForward(100);
turtle.turnLeft(90);
turtle.moveForward(100);
turtle.turnLeft(135);
turtle.moveForward(Math.pow(100*100+100*100, 0.5));
turtle.turnLeft(90);
turtle.moveForward(Math.pow(50*50+50*50, 0.5));
turtle.turnLeft(90);
turtle.moveForward(Math.pow(50*50+50*50, 0.5));
turtle.turnLeft (90);
turtle.moveForward(Math.pow(100*100+100*100, 0.5));

turtle.tailUp();
turtle.moveForward(50);
}}
