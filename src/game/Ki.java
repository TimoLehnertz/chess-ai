package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import figures.Figure;

public class Ki extends Player{

	/**
	 * Number of recursions when calculating best move
	 */
	public static int MAX_RECURSION_LEVEL = 2;
	
	private List<Branch> branches;
	
	private int recursion;
	
	public Ki(boolean white) {
		this(white, 0);
	}
	
	public Ki(boolean white, int recursion) {
		super(white);
		this.recursion = recursion;
	}
	
	/**
	 * start calculating the best possible move
	 */
	private Move perform(List<Figure> field) {
		for (Figure figure : field) {
			if(figure.isWhite() == isWhite()) {
				for (Move move : figure.getPossibleMovesChecked(field)) {
					Branch branch = new Branch(move, white, recursion, MAX_RECURSION_LEVEL);
					branch.calculate(field);
					branches.add(branch);
				}
			}
		}
		Branch bestBranch = null;
		double maxScore = -1000;
		for (Branch branch : branches) {
			if(branch.getScore() > maxScore) {
				maxScore = branch.getScore();
				bestBranch = branch;
			}
		}
		if(bestBranch != null) {
			return bestBranch.getMove();
		} else {
			System.out.println(field);
			log("I am unnable to perform :(");
			return null;
		}
	}
	
	@Override
	public void yourTurn(List<Figure> field, MakeMove callback) {
		branches = new ArrayList<>();
		Move bestMove = perform(field);
		if(bestMove != null) {
			callback.move(bestMove);
		} else {
			log("I lost :(");
		}
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