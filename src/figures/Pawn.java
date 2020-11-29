package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import game.Move;

public class Pawn extends Figure {

	protected Pawn(int i, boolean white) {
		super(i, 6, TYPE_PAWN, white, i);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field) {
		List<Move> out = new ArrayList<>();
		Point local = getLocalPos();
		if(local.y > 0) {
			int max = local.y == 6 ? 2 : 1;
			out.addAll(getFreeMovesInDirection(field, TOP, max, false));
			Point test1 = new Point(local.x - 1, local.y - 1);
			Point test2 = new Point(local.x + 1, local.y - 1);
			
			
			Figure hit1 = getFigureAtPos(field, convert(test1), !isWhite());
			Figure hit2 = getFigureAtPos(field, convert(test2), !isWhite());
			
			if(hit1 != null) {
				out.add(new Move(this, getGlobalPos(), convert(test1), hit1));
			}
			if(hit2 != null) {
				out.add(new Move(this, getGlobalPos(), convert(test2), hit2));
			}
		}
		return out;
	}
}
