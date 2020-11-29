package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knight extends Figure {
	
	List<Point> directions = Arrays.asList(new Point(1,2),new Point(2,1),new Point(2,-1),new Point(2,-1),new Point(1,-2),new Point(-1,-2),new Point(-2,-1),new Point(-2,1));
	
	protected Knight(int i, boolean white) {
		super(i == 0 ? 1 : 6, 7, "Knight", white);
	}

	@Override
	public List<Point> getPossibleMoves(List<Figure> field) {
		List<Point> out = new ArrayList<>();
		for (Point direction : directions) {
			out.addAll(getFreePointsInDirection(field, direction, 1));
		}
		return out;
	}
	
	@Override
	protected Knight clone(){
		Knight f = new Knight(0, isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}
