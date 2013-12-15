
public class Game {

	public Tank[] tanks = new Tank[2];

	public Game() {
		tanks[0] = new Tank(new WorldPos(300,300));
		tanks[1] = new Tank(new WorldPos(500,500));
	}
	
	
	public void update() {
		for (Tank tank : tanks)
			tank.update();		
	}
	
}
