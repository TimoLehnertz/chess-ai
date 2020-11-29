package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Figure {

	protected Pawn(int i, boolean white) {
		super(i, 6, "Pawn", white);
	}

	@Override
	public List<Point> getPossibleMoves(List<Figure> field) {
		List<Point> out = new ArrayList<>();
		Point local = getLocalPos();
		if(getLocalPos().y > 0) {
			int max = local.y == 6 ? 2 : 1;
			out.addAll(getFreePointsInDirection(field, TOP, max));
			Point test1 = new Point(local.x - 1, local.y - 1);
			Point test2 = new Point(local.x + 1, local.y - 1);
			if(enemyOnPos(field, test1)) {
				out.add(test1);
			}
			if(enemyOnPos(field, test2)) {
				out.add(test2);
			}
		} else {
			System.out.println("Pawn trough :)");
		}
		return out;
	}
	
	@Override
	protected Pawn clone(){
		Pawn f = new Pawn(0, isWhite());
		f.setLocalPos(getLocalPos());
		return f;
	}
}
