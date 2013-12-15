
public class Tank {

	private Direction motionDir;
	public WorldPos position;
	public double sightAngle = 0;
	private double sightRotation = 0;
	
	public Tank(WorldPos pos) {
		position = pos;
		motionDir = Direction.STOP;
	}

	public void setMotion(Direction d) {
		motionDir = d;		
	}

	public void update() {
		motionDir.move(position);
		sightAngle+=sightRotation ;
		if (sightAngle > 2*Math.PI)
			sightAngle-=2*Math.PI;
		if (sightAngle < 0)
			sightAngle+=2*Math.PI;
	}

	public void setSightRotation(double angle) {
		sightRotation = angle;
	}

}
