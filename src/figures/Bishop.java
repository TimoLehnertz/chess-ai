package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class Bishop extends Figure {
	
	private List<Point> driections = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT);
	
	protected Bishop(int index, boolean white) {
		super(index == 0 ? 2 : 5, 7, TYPE_BISHOP, white, index);
	}
	
	/**
	 * In local
	 */
	@Override
	public List<Move> getPossibleMoves(List<Figure> field) {
		List<Move> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreeMovesInDirection(field, direction));
		}
		return out;
	}
}
