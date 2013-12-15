package model;

import main.Renderer;

public class DeathScreen {
	public static void show() {
		RenderGroup text = new RenderGroup(Renderer.font.texture);
		RenderGroup bkg = new RenderGroup();
		TextModel deathText = Renderer.font.getStringModel("GAME OVER");
		deathText.center();
		deathText.scale(2.0);
		deathText.setPos(0.0, 0.0, 1.0);
		TextModel restartButton = Renderer.font.getStringModel("Press enter to restart");
		restartButton.center();
		restartButton.move(0.0,-0.4,1.0);

		text.add(deathText);
		text.add(restartButton);
		text.inWorld = false;
		
		bkg.add(new Rectangle(2.0, 2.0,-1.0,-1.0,1.001));
		bkg.inWorld = false;
		Renderer.addGroup(bkg);
		Renderer.addGroup(text);
	}
	
}
