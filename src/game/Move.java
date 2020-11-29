package game;

import java.awt.Point;

import figures.Figure;

public class Move {

	private Figure figure;
	/**
	 * Global space
	 */
	private Point from, to;
	private boolean done = false;
	
	/**
	 * @param figure
	 * @param from
	 * @param to
	 */
	protected Move(Figure figure, Point from, Point to) {
		super();
		this.figure = figure;
		this.from = from;
		this.to = to;
	}
	
	public void move() {
		if(!done) {
			figure.setGlobalPos(to);
			done = true;
		}
	}
	
	protected Figure getFigure() {
		return figure;
	}
	protected void setFigure(Figure figure) {
		this.figure = figure;
	}
	protected Point getFrom() {
		return from;
	}
	protected void setFrom(Point from) {
		this.from = from;
	}
	protected Point getTo() {
		return to;
	}
	protected void setTo(Point to) {
		this.to = to;
	}	
}