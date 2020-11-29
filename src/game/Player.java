package game;

import java.awt.Graphics2D;
import java.awt.Point;

import figures.Figure;

public abstract class Player{

	public Figure[] figures;
	protected boolean white; 
	public abstract void yourTurn(Figure[] enemy, MakeMove callback);
	
	
	public Player(boolean white) {
		super();
		this.white = white;
		this.figures = Figure.getAllDefault(white);
	}


	protected Figure[] getFigures() {
		return figures;
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
}