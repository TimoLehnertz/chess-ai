package game;

import java.awt.Graphics2D;
import java.awt.Point;

import figures.Figure;

public class Ki extends Player{

	public Ki(boolean white) {
		super(white);
	}
	
	@Override
	public void yourTurn(Figure[] enemy, MakeMove callback) {
		
	}

	@Override
	public void globalFieldClicked(Point p) {
//		Do nothing
	}

	@Override
	public void paint(Graphics2D g, int width) {
		// TODO Auto-generated method stub
	}
}