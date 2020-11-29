package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import figures.Figure;

public class Human extends Player{

	private Figure active;
	/**
	 * local
	 */
	private List<Point> selection = new ArrayList<>();
	
	Figure[] enemy;
	
	private boolean myTurn = false;
	
	private MakeMove callback;
	
	public Human(boolean white) {
		super(white);
	}
	
	/**
	 * Global
	 */
	private Figure getFigureOnField(Point pos) {
		for (Figure figure : figures) {
			if(figure.getGlobalPos().x == pos.x && figure.getGlobalPos().y == pos.y) {
				return figure;
			}
		}
		return null;
	}
	
	/**
	 * global to local and vise versa
	 * @param global
	 * @return
	 */
	protected Point convert(Point global) {
		if(white) {
			return global;
		} else {
			return new Point(7 - global.x, 7 - global.y);
		}
	}

	@Override
	public void yourTurn(Figure[] enemy, MakeMove callback) {
		System.out.println(white ? "white" : "black");
		myTurn = true;
		this.enemy = enemy;
		this.callback = callback;
	}
	
	private void makeMove(Move move) {
		if(myTurn) {
			myTurn = false;
			callback.move(move);
		}
	}
	
	private List<Figure> getField(){
		List<Figure> out = new ArrayList<>();
		if(enemy != null) {
			for (Figure figure : enemy) {
				out.add(figure);
			}
		}
		for (Figure figure : figures) {
			out.add(figure);
		}
		return out;
	}

	@Override
	public void globalFieldClicked(Point global) {
		if(selection != null && active != null) {
			for (Point p : selection) {
				p = convert(p);
				if(p.x == global.x && p.y == global.y) {
					Move move = new Move(active, active.getGlobalPos(), p);
					makeMove(move);
					selection = null;
					return;
				}
			}
		}
		active = getFigureOnField(global);
		if(active != null) {
			selection = active.getPossibleMovesChecked(getField());
		}
	}
	
	@Override
	public void paint(Graphics2D g, int width) {
		if(myTurn) {
			if(active != null) {
				for (Point p : selection) {
					p = localToGlobal(p);
					g.setColor(new Color(100,200,100));
					g.fillOval((int) ((double)p.x / 8 * width) + width / 8 / 4, (int) ((double) p.y / 8 * width) + width / 8 / 4, width / 8 / 2, width / 8 / 2);
				}
			}
		}
	}
}