package voxngine.io;

public class Controlls {
	
	Keyboard keyboad;
	Mouse mouse;
	GamePad gamepad;
	
	public Controlls() {
		this.keyboad = new Keyboard();
		this.mouse = new Mouse();
		this.gamepad = new GamePad();
	}

	public Keyboard getKeyboad() {
		return keyboad;
	}

	public Mouse getMouse() {
		return mouse;
	}

	public GamePad getGamepad() {
		return gamepad;
	}
	
	public void endEvents() {
		this.keyboad.endEvents();
		this.mouse.endEvents();
		//this.gamepad.endEvents();
		
	}
	
	public void destroy() {
		this.keyboad.destroy();
		this.mouse.destroy();
		//this.gamepad.destroy();
	}

	

	

}
