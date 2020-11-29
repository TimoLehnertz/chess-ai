package game;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public Window(int width, int height) {
		super("Chess God");
		this.setBounds(0, 0, width, height);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void displayGame(Game game) {
		getContentPane().add(game);
	}
}