package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Move;

public class King extends Figure {
	
	private List<Point> driectionsSimple = Arrays.asList(TOP_LEFT, TOP_RIGHT, DOWN_LEFT, DOWN_RIGHT, LEFT, TOP, RIGHT, DOWN);
	
	/**
	 * Castling
	 */
	private Point castlingKingside = new Point(1,0);
	private Point castlingQueenside = new Point(-1,0);
	
	protected King(boolean white) {
		super(white ? 4 : 3, 7, TYPE_KING, white, 0);
	}

	@Override
	public List<Move> getPossibleMoves(List<Figure> field) {
		List<Move> out = new ArrayList<>();
		for (Point direction : driectionsSimple) {
			out.addAll(getFreeMovesInDirection(field, direction, 1, true));
		}
		/**
		 * Castling Kingside
		 */
		if(!hasMoved()) {
			//Kingside
			
		}
		return out;
	}
}