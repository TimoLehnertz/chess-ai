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
	public List<Move> getPossibleMoves(List<Figure> field, boolean all) {
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
		/**
		 * en passant 
		 */
		if(local.y == 3) {
			Point test1 = new Point(local.x - 1, local.y);
			Point test2 = new Point(local.x + 1, local.y);
			Figure hit1 = getFigureTypeAtPos(field, convert(test1), !isWhite(), TYPE_PAWN);
			Figure hit2 = getFigureTypeAtPos(field, convert(test2), !isWhite(), TYPE_PAWN);
			if(hit1 != null) {
				System.out.println("hit 1: " + hit1.isMovedLastMove());
				if(hit1.isMovedLastMove()) {
					if(hit1.getLastLocalPos().y == 6) {
						out.add(new Move(this, getGlobalPos(), convert(add(test1, new Point(0, -1))), hit1));
					}
				}
			}
			if(hit2 != null) {
				System.out.println("hit 2: " + hit2.isMovedLastMove());
				if(hit2.isMovedLastMove()) {
					if(hit2.getLastLocalPos().y == 6) {
						out.add(new Move(this, getGlobalPos(), convert(add(test2, new Point(0, -1))), hit2));
					}
				}
			}
		}
		return out;
	}
}
