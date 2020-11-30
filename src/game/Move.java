package game;

import java.awt.Point;

import figures.Figure;

public class Move {

	private Figure figure;
	private Figure kill;
	/**
	 * Global space
	 */
	private Point from, to;
	private boolean done = false;
	/**
	 * for castling
	 */
	private Move secondMove;
	
	/**
	 * @param figure
	 * @param from
	 * @param to
	 */
//	protected Move(Figure figure, Point from, Point to) {
//		this(figure, from, to, null);
//	}
	
	public Move(Figure figure, Point from, Point to, Figure kill) {
		this(figure, from, to, kill, null);
	}
	
	/**
	 * @param figure
	 * @param from
	 * @param to
	 * @param kill figure wich gets killed 
	 */
	public Move(Figure figure, Point from, Point to, Figure kill, Move secondMove) {
		super();
		this.figure = figure;
		this.from = from;
		this.to = to;
		this.kill = kill;
		this.secondMove = secondMove;
	}
	
	public void move() {
		if(!done) {
			if(secondMove != null) {
				secondMove.move();
			}
			figure.setGlobalPos(to);
			if(kill != null) {
				kill.kill();
			}
			done = true;
		}
	}
	
	public void undo() {
		if(done) {
			if(secondMove != null) {
				secondMove.move();
			}
			figure.setGlobalPos(to);
			if(kill != null) {
				kill.kill();
			}
			done = true;
		}
	}
	
	public Figure getFigure() {
		return figure;
	}
	protected void setFigure(Figure figure) {
		this.figure = figure;
	}
	public Point getFrom() {
		return from;
	}
	protected void setFrom(Point from) {
		this.from = from;
	}
	public Point getTo() {
		return to;
	}
	protected void setTo(Point to) {
		this.to = to;
	}
	
	public static String pointToString(Point p) {
		return "(" + p.x + ", " + p.y + ")";
	}
	
	public Figure getKill() {
		return kill;
	}

	@Override
	public String toString() {
		return "Move " + figure + " from: " + pointToString(from) + ", to: " + pointToString(to) + (kill != null ? " killing: " + kill : "");
	}
}