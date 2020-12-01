package game;

import java.util.ArrayList;
import java.util.List;

import figures.Figure;

public class Branch {

	private Move move;
	private List<Branch> subBranches;
	private List<Figure> field;
	private int recursionLevel;
	private int maxRecursionLevel;
	private boolean white;
	private boolean enemyMat = false;
	
	/**
	 * @param move
	 * @param kiEnemy
	 * @param recursionLevel
	 * @param maxRecursionLevel
	 */
	protected Branch(Move move, boolean white, int recursionLevel, int maxRecursionLevel) {
		super();
		this.move = move;
		this.recursionLevel = recursionLevel;
		this.maxRecursionLevel = maxRecursionLevel;
		this.white = white;
	}

	public final double getScore() {
		if(recursionLevel <= maxRecursionLevel) {
			double maxScore = -1000;
			for (Branch branch : subBranches) {
				maxScore = Math.max(maxScore, branch.getScore());
			}
			if(enemyMat) {
				return maxScore + 10;
			}
			return maxScore;
		} else {
			if(enemyMat) {
				return getScoreFromField(field) + 10;
			}
			return getScoreFromField(field);
		}
	}

	protected void calculate(List<Figure> field) {
		subBranches = new ArrayList<>();
		/**
		 * copying field
		 */
		List<Figure> fieldCpy = new ArrayList<>();
		for (Figure figure : field) {
			fieldCpy.add(figure.clone());
		}
		/**
		 * reconfiguring move
		 */
		Figure kill = null;
		if(field.indexOf(move.getKill()) != -1) {
			kill = fieldCpy.get(field.indexOf(move.getKill()));
		}
		Move moveCpy = new Move(fieldCpy.get(field.indexOf(move.getFigure())), move.getFrom(), move.getTo(), kill);//@ToDo better version
		
		this.field = fieldCpy;
		if(recursionLevel <= maxRecursionLevel) {
			moveCpy.move();
			new Ki(!white, recursionLevel + 1).yourTurn(fieldCpy, (Move move)->{if(move != null) {move.move();} else{enemyMat = true;}});
			if(enemyMat) {
				return;
			}
			for (Figure figure : fieldCpy) {
				if(figure.isWhite() == white) {
					for (Move move : figure.getPossibleMovesChecked(fieldCpy)) {
						Branch branch = new Branch(move, white, recursionLevel + 1, maxRecursionLevel);
						branch.calculate(fieldCpy);
						subBranches.add(branch);
					}
				}
			}
		}
	}
	
	/**
	 * @param field
	 * @return
	 */
	private double getScoreFromField(List<Figure> field) {
		double score = 0;
		for (Figure figure : field) {
			if(figure.isAlive()) {
				if(figure.isWhite() == white) {
					score += figure.getScore();//mate
				} else {
					score -= figure.getScore();//enemy
				}
			}
		}
		return score;
	}
	
	protected Move getMove() {
		return move;
	}

	protected void setMove(Move move) {
		this.move = move;
	}
}