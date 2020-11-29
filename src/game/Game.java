package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import figures.Figure;

public class Game extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public Color cp1 = new Color(200,200,200);
	public Color cp2 = new Color(70, 70, 70);
	
	/**
	 * 2 players
	 * and their figures
	 */
	Player[] players = new Player[2];
	
	int player = 1;
	List<Move> moves = new ArrayList<>();
	
	
	public Game(Player p1, Player p2) {
		super();
		this.players[0] = p1;
		this.players[1] = p2;
		nextPlayer();
		this.addMouseListener(mouse);
	}
	
	public void nextPlayer() {
		System.out.println("Player: " + player + " turn");
		player = ((player + 1) % 2);
		players[player].yourTurn(players[((player + 1) % 2)].getFigures(), (Move move) -> {makeMove(move);});
	}
	 
	private void makeMove(Move move){
		move.move();
		moves.add(move);
		nextPlayer();
		repaint();
	}
	
	private int getMinWidth() {
		return Math.min(Math.max(100, this.getWidth()), this.getHeight());
	}
	
	@Override
	public void paint(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		super.paintComponents(g);
		int width = getMinWidth();
		int size = 8;
		boolean white = false;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				white = !white;
				g.setColor(white ? cp1 : cp2);
				g.fillRect((int) ((double)x / size * width), (int) ((double)y / size * width), width / size, width / size);
			}
			white = !white;
		}
		for (Player player : players) {
			for (Figure figure : player.getFigures()) {
				figure.paint(g, width);
			}
		}
		for (Player player : players) {
			player.paint(g, width);
		}
	}
	
	private Point getFieldOfPoint(Point p) {
		return new Point((int) ((double)p.getX() / getWidth() * 8), (int) ((double)p.getY() / getWidth() * 8));
	}
	
	private void fieldClicked(Point field) {
		for (Player player : players) {
			player.globalFieldClicked(field);
		}
	}
	
	MouseListener mouse = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			Point mouse = e.getPoint();
			fieldClicked(getFieldOfPoint(mouse));
			repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
}