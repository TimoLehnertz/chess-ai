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
	protected static final Point DOWN_RIGHT = new Point(1, -1);
	protected static final Point TOP = new Point(0, -1);
	protected static final Point LEFT = new Point(-1, 0);
	protected static final Point RIGHT = new Point(1, 0);
	protected static final Point DOWN = new Point(0, -1);
	
	public static final BufferedImage[] IMG_PWAN = {loadImage("/pawn_w.png"),loadImage("/pawn_b.png")};
	public static final BufferedImage[] IMG_BISHOP = {loadImage("/bishop_w.png"),loadImage("/bishop_b.png")};
	public static final BufferedImage[] IMG_QUEEN = {loadImage("/queen_w.png"),loadImage("/queen_b.png")};
	public static final BufferedImage[] IMG_KING = {loadImage("/king_w.png"),loadImage("/king_b.png")};
	public static final BufferedImage[] IMG_ROOK = {loadImage("/rook_w.png"),loadImage("/rook_b.png")};
	public static final BufferedImage[] IMG_KNIGHT = {loadImage("/knight_w.png"),loadImage("/knight_b.png")};
	
	private Point pos;
	private String name;
	private boolean isAlive = true;
	private boolean white;
	private boolean hasMoved = false;
	private boolean isKing = false;
	
	protected Figure(int x, int y, String name, boolean white, boolean isKing) {
		this(x, y, name, white);
		this.isKing = isKing;
	}
	
	protected Figure(int x, int y, String name, boolean white) {
		super();
		this.pos = new Point(x, y);
		this.name = name;
		this.white = white;
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
		switch (getName()) {
		case "King": return IMG_KING[i];
		case "Pawn": return IMG_PWAN[i];
		case "Bishop": return IMG_BISHOP[i];
		case "Knight": return IMG_KNIGHT[i];
		case "Rook": return IMG_ROOK[i];
		case "Queen": return IMG_QUEEN[i];
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
	
	public abstract List<Point> getPossibleMoves(List<Figure> field);
	
	
	public List<Point> getPossibleMovesChecked(List<Figure> field){
		List<Point> tests = getPossibleMoves(field);
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
	
	protected int freeInDirection(List<Figure> field, Point direction) {
		for (int i = 1; i < 8; i++) {
			Point test = add(getLocalPos(), multiply(direction, i));
			if(!pointInField(test)) {
				return i - 1;
			}
			if(enemyOnPos(field, test)) {
				return i;
			} else if(mateOnPos(field, test)) {
				return i - 1;
			}
		}
		return 0;
	}
	
	protected List<Point> getFreePointsInDirection(List<Figure> field, Point direction){
		return getFreePointsInDirection(field, direction, 8);
	}
	
	protected List<Point> getFreePointsInDirection(List<Figure> field, Point direction, int max){
		List<Point> out = new ArrayList<>();
		Point local = getLocalPos();
		for (int i = 1; i <= Math.min(max, freeInDirection(field, direction)); i++) {
			out.add(add(local, multiply(direction, i)));
		}
		return out;
	}
	
	protected boolean enemyOnPos(List<Figure> field, Point local) {
		Point global = convert(local);
		for (Figure figure : field) {
			if(figure == this || figure.isWhite() == isWhite() || !figure.isAlive) {
				continue;
			}
			if(figure.getGlobalPos().x == global.x && figure.getGlobalPos().y == global.y) {
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
	
	@Override
	protected abstract Figure clone();
	
	protected void deleteInvalidMoves(List<Figure> field, List<Point> tests) {
		List<Point> removals = new ArrayList<>();
		for (Point test : tests) {
			List<Figure> testField = new ArrayList<>();
			for (Figure figure : field) {
				testField.add(figure.clone());
			}
			testField.get(field.indexOf(this)).setLocalPos(test);
			if(canEnemyHitKing(testField)) {
				removals.add(test);
			}
		}
		for (Point removal : removals) {
			tests.remove(removal);
		}
	}
	
	protected Figure getKingFrom(boolean white, Figure[] field) {
		for (Figure figure : field) {
			if(figure.isKing && figure.isWhite() == white) {
				return figure;
			}
		}
		return null;
	}
	
	protected boolean canFigureCanBeHit(Figure[] field, Figure figure) {
		
		return false;
	}
	
	protected boolean canEnemyHitKing(List<Figure> field) {
		int ires = 3  + 5 / 6;
		List<Point> hitFields = new ArrayList<>();
		for (Figure figure : field) {
			if(figure.isWhite() != isWhite()) {// only enemies
				hitFields.addAll(figure.getPossibleMoves(field));
			}
		}
		Point kingPos = getOwnKingPos(field);
		for (Point hit : hitFields) {
			if(hit.x == kingPos.x && hit.y == kingPos.y) {
				return true;
			}
		}
		return false;
	}
	
	protected Point getOwnKingPos(List<Figure> field) {
		for (Figure figure : field) {
			if(figure.isWhite() == isWhite()) {
				if(figure.name.contentEquals("King")) {
					return figure.getLocalPos();
				}
			}
		}
		return null;
	}
	
	protected boolean canKillKingAfterMove(List<Figure> field, Move move) {
		
		return false;
	}
	
	protected boolean pointInField(Point p) {
		return p.x >= 0 && p.x < 8 && p.y >= 0 && p.y < 8;
	}
	
	protected Point convert(Point global) {
		if(white) {
			return global;
		} else {
			return new Point(7 - global.x, 7 - global.y);
		}
	}
	
	/**
	 * Getters Setters
	 * @return
	 */
	public String getName() {
		return name;
	}

	public Point getLocalPos() {
		return pos;
	}

	public void setLocalPos(Point local) {
		if(local.x != pos.x || local.y != pos.y) {
			hasMoved = true;
			this.pos = local;
		}
	}
	
	public Point getGlobalPos() {
		return convert(pos);
	}
	
	public void setGlobalPos(Point global) {
		Point local = convert(global);
		if(local.x != pos.x || local.y != pos.y) {
			hasMoved = true;
			this.pos = local;
		}
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}

	protected boolean isWhite() {
		return white;
	}
}