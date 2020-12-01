package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class King extends Figure {
	
	private List<Point> driectionsSimple = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, LEFT, TOP, RIGHT, DOWN);
	
	protected King(boolean white) {
		super(white ? 4 : 3, 7, TYPE_KING, white, 0, 100);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field, boolean all) {
		List<Move> out = new ArrayList<>();
		for (Point direction : driectionsSimple) {
			out.addAll(getFreeMovesInDirection(field, direction, 1, true));
		}
		/**
		 * Castling Kingside
		 */
		if(!hasMoved() && all) {
			if(!canEnemyHitKing(field)) {
				//Kingside
				Figure rook1 = getFigureTypeAtPos(field, convert(new Point(0, 7)), isWhite(), TYPE_ROOK);
				Figure rook2 = getFigureTypeAtPos(field, convert(new Point(7, 7)), isWhite(), TYPE_ROOK);
				if(rook1 != null) {
					if(!rook1.hasMoved()) {
						if(!isFigureAtPoint(field, new Point(1,7)) && !isFigureAtPoint(field, new Point(2,7)) && !isFigureAtPoint(field, new Point(3,7))) {
							if(!canFieldBeHit(field, new Point(3, 7))) {
								out.add(new Move(this, getGlobalPos(), new Point(2, 7), null, new Move(rook1, rook1.getGlobalPos(), new Point(3, 7), null)));
							}
						}
					}
				}
				if(rook2 != null) {
					if(!rook2.hasMoved()) {
						if(!isFigureAtPoint(field, new Point(6,7)) && !isFigureAtPoint(field, new Point(5,7))) {
							if(!canFieldBeHit(field, new Point(5, 7))) {
								out.add(new Move(this, getGlobalPos(), new Point(6, 7), null, new Move(rook2, rook2.getGlobalPos(), new Point(5, 7), null)));
							}
						}
					}
				}
			}
		}
		return out;
	}
}