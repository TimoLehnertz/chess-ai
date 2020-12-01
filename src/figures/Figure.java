package figures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import game.Move;

public abstract class Figure {

	/**
	 * directions
	 */
	protected static final Point TOP_LEFT = new Point(-1, -1);
	protected static final Point TOP_RIGHT = new Point(1, -1);
	protected static final Point DOWN_LEFT = new Point(-1, 1);
	protected static final Point DOWN_RIGHT = new Point(1, 1);
	protected static final Point TOP = new Point(0, -1);
	protected static final Point LEFT = new Point(-1, 0);
	protected static final Point RIGHT = new Point(1, 0);
	protected static final Point DOWN = new Point(0, 1);
	
	public static final int TYPE_PAWN = 0;
	public static final int TYPE_ROOK = 1;
	public static final int TYPE_QUEEN = 2;
	public static final int TYPE_KING = 3;
	public static final int TYPE_BISHOP = 4;
	public static final int TYPE_KNIGHT = 5;
	
	public static final BufferedImage[] IMG_PWAN = {loadImage("/pawn_w.png"),loadImage("/pawn_b.png")};
	public static final BufferedImage[] IMG_BISHOP = {loadImage("/bishop_w.png"),loadImage("/bishop_b.png")};
	public static final BufferedImage[] IMG_QUEEN = {loadImage("/queen_w.png"),loadImage("/queen_b.png")};
	public static final BufferedImage[] IMG_KING = {loadImage("/king_w.png"),loadImage("/king_b.png")};
	public static final BufferedImage[] IMG_ROOK = {loadImage("/rook_w.png"),loadImage("/rook_b.png")};
	public static final BufferedImage[] IMG_KNIGHT = {loadImage("/knight_w.png"),loadImage("/knight_b.png")};
	
	private double score;
	private Point pos;
	/**
	 * Last Position or null if not moved yet
	 */
	private Point lastPos;
	private boolean movedLastMove = false;
	private int type;
	private boolean isAlive = true;
	private boolean white;
	
	private int index;
	
	private static final Figure[][] defaultFigures = {getDistinctDefaultFigures(true), getDistinctDefaultFigures(false)};
	
	protected Figure(int x, int y, int type, boolean white, int index, double score) {
		super();
		this.pos = new Point(x, y);
		this.type = type;
		this.white = white;
		this.index = index;
		this.score = score;
	}
	
	 static BufferedImage loadImage(String path) {
		 try {
			return ImageIO.read(Figure.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	 }
	 
	private BufferedImage getImage() {
		int i = isWhite() ? 0 : 1;
		switch (getType()) {
		case TYPE_KING: return IMG_KING[i];
		case TYPE_PAWN: return IMG_PWAN[i];
		case TYPE_BISHOP: return IMG_BISHOP[i];
		case TYPE_KNIGHT: return IMG_KNIGHT[i];
		case TYPE_ROOK: return IMG_ROOK[i];
		case TYPE_QUEEN: return IMG_QUEEN[i];
		default: return null;
		}
	}
	 
	public void paint(Graphics2D g, int width) {
		g.drawImage(getImage(), (int) ((double) getGlobalPos().x / 8 * width), (int) ((double) getGlobalPos().y / 8 * width), width / 8, width / 8, null);
	}
	
	
	public static Figure[] getAllDefault(boolean white){
		Figure[] out = new Figure[16];
		int counter = 0;
		for (int i = 0; i < 8; i++) {
			out[counter] = new Pawn(i, white);
			counter++;
		}
		for (int i = 0; i < 2; i++) {
			out[counter] = new Bishop(i, white);
			counter++;
			out[counter] = new Rook(i, white);
			counter++;
			out[counter] = new Knight(i, white);
			counter++;
		}
		out[counter] = new King(white);
		counter++;
		out[counter] = new Queen(white);
		return out;
	}
	
	public static Figure[] getDistinctDefaultFigures(boolean color) {
		Figure[] out = new Figure[6];
		List<Integer> used = new ArrayList<>();
		int counter = 0;
		for (Figure figure : getAllDefault(color)) {
			if(!used.contains(figure.getType())) {
				out[counter] = figure.clone();
				used.add(figure.getType());
				counter++;
			}
		}
		return out;
	}
	
	public abstract List<Move> getPossibleMoves(List<Figure> field, boolean all);
	
	
	public List<Move> getPossibleMovesChecked(List<Figure> field){
		if(!isAlive()) {
			return new ArrayList<Move>();
		}
		List<Move> tests = getPossibleMoves(field, true);
		deleteInvalidMoves(field, tests);
		return tests;
	}
	
	public List<Point> getAvailabeMoves(){
		List<Point> out = new ArrayList<>();
		return out;
	}
	
	protected Point multiply(Point p, int m) {
		return new Point(p.x * m, p.y * m);
	}
	
	protected Point add(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
	
	protected int freeInDirection(List<Figure> field, Point localDirection, boolean canHit) {
		for (int i = 1; i < 8; i++) {
			Point test = add(getLocalPos(), multiply(localDirection, i));
			if(!pointInField(test)) {
				return i - 1;
			}
			if(enemyOnPos(field, test)) {
				return i - (canHit ? 0 : 1);
			} else if(mateOnPos(field, test)) {
				return i - 1;
			}
		}
		return 7;
	}
	
	public static boolean pointInPoint(Point a, Point b) {
		return a.x == b.x && a.y == b.y;
	}
	
	protected List<Move> getFreeMovesInDirection(List<Figure> field, Point direction){
		return getFreeMovesInDirection(field, direction, 8, true);
	}
	
	protected List<Move> getFreeMovesInDirection(List<Figure> field, Point localDirection, int max, boolean canHit){
		List<Move> out = new ArrayList<>();
		Point local = getLocalPos();
		Point global = getGlobalPos();
		for (int i = 1; i <= Math.min(max, freeInDirection(field, localDirection, canHit)); i++) {
			Point globalTo = convert(add(local, multiply(localDirection, i)));
			Figure kill = getFigureAtPos(field, globalTo, !isWhite());
			Move move = new Move(this, global, globalTo, kill);
			out.add(move);
		}
		return out;
	}
	
	protected boolean enemyOnPos(List<Figure> field, Point local) {
		Point global = convert(local);
		for (Figure figure : field) {
			if(figure == this || figure.isWhite() == isWhite() || !figure.isAlive) {
				continue;
			}
			if(figure.isAtGlobalPoint(global)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean mateOnPos(List<Figure> field, Point local) {
		Point global = convert(local);
		for (Figure figure : field) {
			if(figure == this || figure.isWhite() != isWhite() || !figure.isAlive) {
				continue;
			}
			if(figure.getGlobalPos().x == global.x && figure.getGlobalPos().y == global.y) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * deletes moves wich:
	 * 		would lead to checkmate
	 * 
	 * @param field
	 * @param tests
	 */
	protected void deleteInvalidMoves(List<Figure> field, List<Move> tests) {
		List<Move> removals = new ArrayList<>();
		for (Move test : tests) {
			/**
			 * Cloning field
			 */
			List<Figure> testField = new ArrayList<>();
			for (Figure figure : field) {
				testField.add(figure.clone());
			}
			/**
			 * reconfiguring move to point to cloned field
			 */
			Figure newKill = null;
			if(field.indexOf(test.getKill()) != -1) {
				newKill = testField.get(field.indexOf(test.getKill()));
			}
			Move testCpy = new Move(testField.get(field.indexOf(this)), test.getFrom(), test.getTo(), newKill);
			testCpy.move();//testing move
			if(canEnemyHitKing(testField)) {
				removals.add(test);
			}
		}
		for (Move removal : removals) {
			tests.remove(removal);
		}
	}
	
	public static Figure getKingFrom(boolean white, List<Figure> field) {
		for (Figure figure : field) {
			if(figure.type == TYPE_KING && figure.isWhite() == white) {
				return figure;
			}
		}
		return null;
	}
	
	protected boolean canFigureCanBeHit(Figure[] field, Figure figure) {
		
		return false;
	}
	
	public boolean canEnemyHitKing(List<Figure> field) {
		return canBeHitBy(field, getKingFrom(isWhite(), field).getLocalPos()) > 0;
	}
	
	/**
	 * 
	 * @param field
	 * @param local pos
	 * @param type
	 * @param white
	 * @return is at point
	 */
	protected boolean isFigureAtPoint(List<Figure> field, Point local, int type, boolean white) {
		for (Figure figure : field) {
			if(figure.getType() == type && figure.isWhite() == white && figure.isAlive && figure.isAtGlobalPoint(convert(local))) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isFigureAtPoint(List<Figure> field, Point local) {
		for (Figure figure : field) {
			if(figure.isAlive && figure.isAtGlobalPoint(convert(local))) {
				return true;
			}
		}
		return false;
	}
	
	protected Figure getFigureAtPos(List<Figure> field, Point global, boolean white) {
		for (Figure figure : field) {
			if(figure.isWhite() == white && figure.isAlive && figure.isAtGlobalPoint(global)) {
				return figure;
			}
		}
		return null;
	}
	
	protected Figure getFigureTypeAtPos(List<Figure> field, Point global, boolean white, int type) {
		for (Figure figure : field) {
			if(figure.isWhite() == white && figure.isAlive && figure.isAtGlobalPoint(global) && figure.getType() == type) {
				return figure;
			}
		}
		return null;
	}
	
	/**
	 * own figure
	 * @param type
	 * @return
	 */
	protected Figure getFigure(List<Figure> field, int type) {
		return getFigure(field, isWhite(), type);
	}
	
	protected Figure getFigure(List<Figure> field, boolean white, int type) {
		return getFigure(field, white, type, 0);
	}
	
	/**
	 * Get figure from field by parameters
	 * @param field
	 * @param white
	 * @param type
	 * @param index
	 * @return
	 */
	protected Figure getFigure(List<Figure> field, boolean white, int type, int index) {
		int match = 0;
		for (Figure figure : field) {
			if(figure.isWhite() == isWhite() && figure.getType() == type) {
				if(match == index && figure.isAlive) {
					return figure;
				}
				match++;
			}
		}
		return null;
	}
	
	public int canBeHitBy(List<Figure> field) {
		return canBeHitBy(field, getLocalPos());
	}
	
	protected int canBeHitBy(List<Figure> field, Point local) {
		int counter = 0;
		Figure[] defaultFigures = getDefaultFiguresAtPoint(local, isWhite());
		for (Figure figure : defaultFigures) {
			for (Move possibleMove : figure.getPossibleMoves(field, false)) {
				if(isFigureAtPoint(field, convert(possibleMove.getTo()), possibleMove.getFigure().getType(), !isWhite())){
					counter++;
				}
			}
		}
		return counter;
	}
	
	public int isCoveredBy(List<Figure> field) {
		return isCoveredBy(field, getLocalPos());
	}
	
	protected int isCoveredBy(List<Figure> field, Point local) {
		int counter = 0;
		Figure[] defaultFigures = getDefaultFiguresAtPoint(local, !isWhite());
		for (Figure figure : defaultFigures) {
			for (Move possibleMove : figure.getPossibleMoves(field, false)) {
				if(isFigureAtPoint(field, convert(possibleMove.getTo()), possibleMove.getFigure().getType(), isWhite())){
					counter++;
				}
			}
		}
		return counter;
	}
	
	/**
	 * 
	 * @param local
	 * @return
	 */
	protected Figure[] getDefaultFiguresAtPoint(Point local, boolean white){
		for (Figure figure : defaultFigures[white ? 0 : 1]) {
			figure.setGlobalPos(convert(local));
		}
		return defaultFigures[white ? 0 : 1];
	}
	
	/**
	 * Todo faster method maby inside player?
	 * @param field
	 * @param type
	 * @param white
	 * @return
	 */
	protected boolean doesPlayerHasAliveFigure(List<Figure> field, int type, boolean white) {
		for (Figure figure : field) {
			if(figure.isAlive && figure.isWhite() == white && figure.getType() == type) {
				return true;
			}
		}
		return false;
	}
	
	protected Point getOwnKingPos(List<Figure> field) {
		return getFigure(field, TYPE_KING).getLocalPos();
	}
	
	protected boolean pointInField(Point p) {
		return p.x >= 0 && p.x < 8 && p.y >= 0 && p.y < 8;
	}
	
	protected final Point convert(Point global) {
		if(white) {
			return new Point(global.x, global.y);
		} else {
			return new Point(7 - global.x, 7 - global.y);
		}
	}
	
	/**
	 * Getters Setters
	 * @return
	 */
	public int getType() {
		return type;
	}

	public Point getLocalPos() {
		return pos;
	}

	public void setLocalPos(Point local) {
		if(local.x != pos.x || local.y != pos.y) {
			Point newLocalPos = new Point(local.x, local.y);
			setMovedLastMove(true);
			lastPos = getLocalPos();
			this.pos = newLocalPos;
		}
	}
	
	public Point getGlobalPos() {
		return convert(pos);
	}
	
	public void setGlobalPos(Point global) {
		Point local = convert(global);
		setLocalPos(local);
	}
	
	public boolean isAtGlobalPoint(Point p) {
		Point global = getGlobalPos();
		return p.x == global.x && p.y == global.y;
	}
	
	public Point getLastLocalPos() {
		return new Point(lastPos.x, lastPos.y);
	}
	
	public Point getLastGlobalPos() {
		return convert(lastPos);
	}
	
	public boolean hasMoved() {
		return lastPos != null;
	}

	public boolean isWhite() {
		return white;
	}
	
	public static String typeToString(int type) {
		switch (type) {
		case TYPE_KING: return "King";
		case TYPE_PAWN: return "Pawn";
		case TYPE_BISHOP: return "Bishop";
		case TYPE_KNIGHT: return "Knight";
		case TYPE_ROOK: return "Rook";
		case TYPE_QUEEN: return "Queen";
		default: return null;
		}
	}
	
	public final String getName() {
		return typeToString(getType());
	}
	
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	@Override
	public String toString() {
		return (white ? "white " : "black ") + getName();
	}

	public void kill() {
		isAlive = false;
	}
	
	public void revive() {
		isAlive = true;
	}
	
	public int getIndex() {
		return index;
	}

	public boolean isMovedLastMove() {
		return movedLastMove;
	}

	public void setMovedLastMove(boolean movedLastMove) {
		this.movedLastMove = movedLastMove;
	}

	public double getScore() {
		return score;
	}

	@Override
	public boolean equals(Object in) {
		if(in instanceof Figure) {
			Figure test = (Figure) in;
			return test.isAlive == isAlive
					&& test.getType() == getType()
					&& test.isWhite() == isWhite()
					&& test.getIndex() == getIndex()
					&& test.isMovedLastMove() == isMovedLastMove();
		} else {
			return false;
		}
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public Figure clone() {
		Figure f;
		switch (getType()) {
		case TYPE_BISHOP: f = new Bishop(index, white); break;
		case TYPE_PAWN: f = new Pawn(index, white); break;
		case TYPE_KING: f = new King(white); break;
		case TYPE_QUEEN: f = new Queen(white); break;
		case TYPE_ROOK: f = new Rook(index, white); break;
		case TYPE_KNIGHT: f = new Knight(index, white); break;
		default: return null;
		}
		f.setIndex(index);
		f.setLocalPos(pos);
		f.setAlive(isAlive);
		f.setMovedLastMove(isMovedLastMove());
		return f;
	}
}