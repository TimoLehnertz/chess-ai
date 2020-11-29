package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queen extends Figure {

	private List<Point> driections = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, LEFT, TOP, RIGHT, DOWN);
	
	protected Queen(boolean white) {
		super(white ? 3 : 4, 7, "Queen", white);
	}

	@Override
	public List<Point> getPossibleMoves(List<Figure> field) {
		List<Point> out = new ArrayList<>();
		for (Point direction : driections) {
			out.addAll(getFreePointsInDirection(field, direction));
		}
		return out;
	}
	
	@Override
	protected Queen clone(){
		Queen f = new Queen(isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}
