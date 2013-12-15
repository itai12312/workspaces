
public enum Direction {
	RIGHT(1,0), STOP(0,0), LEFT(-1,0), UP(0,-1), DOWN(0,1);
	
	private double dx;
	private double dy;
	
	private Direction(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move(WorldPos position) {
		position.x+=dx;
		position.y+=dy;		
	}
}
