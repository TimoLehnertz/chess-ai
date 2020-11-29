package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King extends Figure {
	
	private List<Point> driections = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, LEFT, TOP, RIGHT, DOWN);
	
	protected King(boolean white) {
		super(white ? 4 : 3, 7, "King", white, true);
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
	protected King clone(){
		King f = new King(isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}