package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class Knight extends Figure {
	
	List<Point> directions = Arrays.asList(new Point(1,2),new Point(2,1),new Point(2,-1),new Point(2,-1),new Point(1,-2),new Point(-1,-2),new Point(-2,-1),new Point(-2,1));
	
	protected Knight(int i, boolean white) {
		super(i == 0 ? 1 : 6, 7, TYPE_KNIGHT, white, i, 3);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field, boolean all) {
		List<Move> out = new ArrayList<>();
		for (Point direction : directions) {
			out.addAll(getFreeMovesInDirection(field, direction, 1, true));
		}
		return out;
	}
}
