package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rook extends Figure {

	private List<Point> driections = Arrays.asList(LEFT, TOP, RIGHT, DOWN);
	
	protected Rook(int i, boolean white) {
		super(i == 0 ? 0 : 7, 7, "Rook", white);
	}

	@Override
	public List<Point> getPossibleMoves(List<Figure> field) {
		List<Point> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreePointsInDirection(field, direction, 1));
		}
		return out;
	}
	
	@Override
	protected Rook clone(){
		Rook f = new Rook(0, isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}
