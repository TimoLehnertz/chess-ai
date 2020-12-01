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
	private List<Move> possibleMoves = new ArrayList<>();
	
	List<Figure> field;
	
	private boolean myTurn = false;
	
	private MakeMove callback;
	
	public Human(boolean white) {
		super(white);
	}
	
	/**
	 * Global
	 */
	private Figure getFigureOnField(Point gloabl) {
		for (Figure figure : field) {
			if(figure.getGlobalPos().x == gloabl.x && figure.getGlobalPos().y == gloabl.y && figure.isAlive() && figure.isWhite() == isWhite()) {
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
		if(isWhite()) {
			return global;
		} else {
			return new Point(7 - global.x, 7 - global.y);
		}
	}

	@Override
	public void yourTurn(List<Figure> field, MakeMove callback) {
		log("Its my turn");
		myTurn = true;
		this.field = field;
		this.callback = callback;
	}
	
	private void makeMove(Move move) {
		log("making move: " + move);
		if(myTurn) {
			myTurn = false;
			callback.move(move);
		}
	}

	@Override
	public void globalFieldClicked(Point globalField) {
		if(myTurn) {
			if(possibleMoves != null && active != null) {
				for (Move move : possibleMoves) {
					Point gloabl = move.getTo();
					if(gloabl.x == globalField.x && gloabl.y == globalField.y) {
						makeMove(move);
						possibleMoves = null;
						active = null;
						return;
					}
				}
			}
			active = getFigureOnField(globalField);
			if(active != null) {
				log(active + " selected");
				possibleMoves = active.getPossibleMovesChecked(field);
			}
		}
	}
	
	@Override
	public void paint(Graphics2D g, int width) {
		if(myTurn) {
			if(active != null) {
				for (Move move : possibleMoves) {
					Point global = move.getTo();
					g.setColor(new Color(100,200,100));
					g.fillOval((int) ((double)global.x / 8 * width) + width / 8 / 4, (int) ((double) global.y / 8 * width) + width / 8 / 4, width / 8 / 2, width / 8 / 2);
				}
			}
		}
	}
}