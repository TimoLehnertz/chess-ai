package main;

import game.Game;
import game.Human;
import game.Ki;
import game.Player;
import game.Window;

public class Main {

	public static void main(String[] args) {
		Player p1 = new Human(true);
//		Player p2 = new Human(false);
		Player p2 = new Ki(false);
		Game game = new Game(p1, p2);
		
		Window w = new Window(600,620);
		w.displayGame(game);
	}
}