package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class Rook extends Figure {

	private List<Point> driections = Arrays.asList(LEFT, TOP, RIGHT, DOWN);
	
	protected Rook(int i, boolean white) {
		super(i == 0 ? 0 : 7, 7, TYPE_ROOK, white, i);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field) {
		List<Move> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreeMovesInDirection(field, direction, 8, true));
		}
		return out;
	}
}