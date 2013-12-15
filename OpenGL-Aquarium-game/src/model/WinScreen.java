package model;

import main.Renderer;

public class WinScreen {
	public static void show() {
		RenderGroup text = new RenderGroup(Renderer.font.texture);
		RenderGroup bkg = new RenderGroup();
		TextModel winText = Renderer.font.getStringModel("WINNER");
		winText.center();
		winText.scale(2.0);
		winText.setPos(0.0, 0.0, 1.0);
		TextModel restartButton = Renderer.font.getStringModel("Press enter to restart");
		restartButton.center();
		restartButton.move(0.0,-0.4,1.0);

		text.add(winText);
		text.add(restartButton);
		text.inWorld = false;
		
		Model bkgRect = new Rectangle(2.0, 2.0,-1.0,-1.0,1.001);
		bkgRect.color(0.0, 1.0, 0.0);
		bkg.add(bkgRect);
		bkg.inWorld = false;
		Renderer.addGroup(bkg);
		Renderer.addGroup(text);
	}
	
}
