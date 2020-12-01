package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import figures.Figure;

public abstract class Player{

	protected boolean white; 
	public abstract void yourTurn(List<Figure> field, MakeMove callback);
	
	
	public Player(boolean white) {
		super();
		this.white = white;
	}
	
	protected Point localToGlobal(Point local) {
		if(white) {
			return local;
		} else {
			return new Point(7 - local.x, 7 - local.y);
		}
	}
	
	public abstract void globalFieldClicked(Point p);
	
	public abstract void paint(Graphics2D g, int width);
	
	/**
	 * Simple logger
	 * @param msg
	 */
	protected void log(Object msg) {
		System.out.println("[" + (white ? "white" : "black") + "] " + msg);
	}

	protected boolean isWhite() {
		return white;
	}
}