package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class Queen extends Figure {

	private List<Point> driections = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, LEFT, TOP, RIGHT, DOWN);
	
	protected Queen(boolean white) {
		super(white ? 3 : 4, 7, TYPE_QUEEN, white, 0, 9);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field, boolean all) {
		List<Move> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreeMovesInDirection(field, direction));
		}
		return out;
	}
}
