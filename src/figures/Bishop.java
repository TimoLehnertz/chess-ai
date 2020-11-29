package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bishop extends Figure {
	
	private List<Point> driections = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT);
	
	protected Bishop(int i, boolean white) {
		super(i == 0 ? 2 : 5, 7, "Bishop", white);
	}
	
	/**
	 * In local
	 */
	@Override
	public List<Point> getPossibleMoves(List<Figure> field) {
		List<Point> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreePointsInDirection(field, direction));
		}
		return out;
	}

	@Override
	protected Bishop clone(){
		Bishop f = new Bishop(0, isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}
