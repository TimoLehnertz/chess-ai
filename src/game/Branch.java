package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import figures.Figure;

public class Branch {

	private Move move;
	private Move enemyMove;
	private List<Branch> subBranches = new ArrayList<>();
	private List<Figure> field;
	private int recursionLevel;
	private int maxRecursionLevel;
	private boolean white;
	private boolean kingHasMoved;
	
	/**
	 * @param move
	 * @param kiEnemy
	 * @param recursionLevel
	 * @param maxRecursionLevel
	 */
	protected Branch(Move move, boolean white, int recursionLevel, int maxRecursionLevel, List<Figure> field) {
		super();
		this.move = move;
		this.recursionLevel = recursionLevel;
		this.maxRecursionLevel = maxRecursionLevel;
		this.white = white;
		/**
		 * cloning field
		 */
		this.field = new ArrayList<>();
		this.kingHasMoved = Figure.getKingFrom(white, field).hasMoved();
		for (Figure figure : field) {
			this.field.add(figure.clone());
		}
		/**
		 * reconfiguring move
		 */
		Figure kill = null;
		if(field.indexOf(move.getKill()) != -1) {
			kill = this.field.get(field.indexOf(move.getKill()));
		}
		Move moveCpy = new Move(this.field.get(field.indexOf(move.getFigure())), move.getFrom(), move.getTo(), kill);//@ToDo better version
		moveCpy.move();
	}

	public final double getScore() {
		if(subBranches.size() == 0) {
			return getScoreFromField(field);
		}
		if(recursionLevel <= maxRecursionLevel) {
			Branch best = getBestSubBranch();
			if(best != null) {
				return best.getScore();
			} else {
				return -1000;
			}
		} else {
			return getScoreFromField(field);
		}
	}
	
	protected Branch getBestSubBranch() {
		if(subBranches.size() == 0) {
			return null;
		}
		double maxScore = -1000;
		Branch bestBranch = subBranches.get(0);
		for (Branch branch : subBranches) {
			double score = branch.getScore();
			if(score > maxScore) {
				maxScore = score;
				bestBranch = branch;
			}
		}
		return bestBranch;
	}

	protected void calculate() {
		subBranches = new ArrayList<>();
		if(recursionLevel <= maxRecursionLevel) {
			new Ki(!white, recursionLevel + 1).yourTurn(field, (Move move)->{if(move != null) {move.move();} enemyMove = move;});
			if(enemyMove == null) {
				return;
			}
			for (Figure figure : field) {
				if(figure.isWhite() == white) {
					for (Move move : figure.getPossibleMovesChecked(field)) {
						Branch branch = new Branch(move, white, recursionLevel + 1, maxRecursionLevel, field);
						subBranches.add(branch);
					}
				}
			}
			List<Branch> best = getBestBranches(subBranches, 3);
			for (Branch branch : best) {
				branch.calculate();
			}
			List<Branch> removals = new ArrayList<>();
			for (Branch branch : subBranches) {
				if(!best.contains(branch)) {
					removals.add(branch);
				}
			}
			for (Branch branch : removals) {
				subBranches.remove(branch);
			}
		}
	}
	
	public static List<Branch> getBestBranches(List<Branch> branches, int amount){
		List<Branch> out = new ArrayList<>();
		Map<Double, Branch> map = new HashMap<>();
		for (Branch branch : branches) {
			map.put(branch.getScore(), branch);
		}
		for (int i = 0; i < amount; i++) {
			double max = -100000;
			Branch best = null;
			for (Entry<Double, Branch> entry : map.entrySet()) {
				if(entry.getKey() >= max) {
					max = entry.getKey();
					best = entry.getValue();
				}
			}
			if(best != null) {
				out.add(best);
			}
			map.remove(max);
		}
		return out;
	}
	
	
	private static final double CENTER_DISTANCE_IMPORTANCE = 0.3;
	private static final double COVER_IMPORTANCE = 0.01;
	private static final double KING_NOT_MOVE_IMPORTANCE = 1;
	
	private double getScoreFromField(List<Figure> field) {
		double score = 0;
		
		Figure king = Figure.getKingFrom(white, field);
		
		for (Figure figure : field) {
			if(figure.isAlive()) {
				double mul;
				if(figure.isWhite() == white) {
					mul = 1;
				}else {
					mul = -1;
				}
				score += mul * figure.getScore();//mate
				/**
				 * cover
				 */
				score += mul * (figure.isCoveredBy(field) - figure.canBeHitBy(field)) * COVER_IMPORTANCE * figure.getScore();
			}
		}
		if(!kingHasMoved && !king.hasMoved()) {
			score += KING_NOT_MOVE_IMPORTANCE;
		}
		return score;
	}
	
	protected Move getMove() {
		return move;
	}

	protected void setMove(Move move) {
		this.move = move;
	}
	
	public static double distance(Point a, double x, double y) {
		double x1 = a.x - x;
		double y1 = a.y - y;
		return Math.sqrt(x1 * x1 + y1 * y1);
	}
	
	@Override
	public String toString() {
		return "Branch(r=" + recursionLevel + ", s=" + getScore() + ")my move: " + move + " | enemy move: " + enemyMove + " best sub:\n" + getBestSubBranch();
	}
}