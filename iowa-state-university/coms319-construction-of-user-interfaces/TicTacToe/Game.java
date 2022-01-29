package layout;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Game extends JFrame {
	
	JButton board[] = new JButton[9];
	int alternate = 0;
	JPanel turns = new JPanel();
	JLabel goTurn = new JLabel("<html><span style='font-size:14px'>"+"Your turn:  X"+"</span></html>");
	static Game frame = new Game("Tic Tac Toe");
	JButton TTT = new JButton("TTT");
	GridLayout Layout = new GridLayout(0, 2);
	int lastdraw=0;

	public Game(String name) {setResizable(false);}				
	
	//main that sets up our frame
	public static void main(String[] args) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 100, 300, 300);
		frame.Board(frame.getContentPane());
		frame.setVisible(true);
	}

	//puts down buttons, tunes the panels.
	public void Board(Container pane) {
		final JPanel theBoard = new JPanel();
		//Set up for
		for (int i = 0; i <= 8; i++) {
			board[i] = new JButton();
			board[i].setText("");
			board[i].setBackground(Color.WHITE);
			board[i].addActionListener(new buttonListener());//clicking does something
			theBoard.add(board[i]);
		}
		//set up layout of buttons
		theBoard.setLayout(new GridLayout(3, 3));
		turns.setLayout(new GridLayout(1, 1));
		JButton b = new JButton("xxxxxxxxx");
		Dimension buttonSize = b.getPreferredSize();
		theBoard.setPreferredSize(new Dimension((int) (buttonSize.getWidth() * 2.5) + 20, (int) (buttonSize.getHeight() * 7.5) + 20* 2));
		turns.add(goTurn);
		TTT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Layout.layoutContainer(theBoard);
			}});

		pane.add(theBoard, BorderLayout.NORTH);
		pane.add(turns, BorderLayout.SOUTH);
	}
	
	//What happens when our button is clicked?
	private class buttonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
		
           //Getting and scaling the images
			Image myPictureX = new ImageIcon(this.getClass().getResource("/x.jpg")).getImage();
			ImageIcon X = new ImageIcon(myPictureX);
			Image imageX = X.getImage(); 
			Image newimgX = imageX.getScaledInstance(100, 100,
					java.awt.Image.SCALE_SMOOTH); 
			X = new ImageIcon(newimgX);


			Image myPictureO= new ImageIcon(this.getClass().getResource("/o.jpg")).getImage();
			ImageIcon O = new ImageIcon(myPictureO);
			Image imageO = O.getImage();
			Image newimgO = imageO.getScaledInstance(95, 95,
					java.awt.Image.SCALE_SMOOTH); 
			O = new ImageIcon(newimgO);

			//------------------------------------------------------------------------------------
			//Changes the buttons based on who's turn it is.
			JButton buttonClicked = (JButton) e.getSource();
			if (alternate % 2 == 0) {
				buttonClicked.setIcon(X);//set the X picture	
				buttonClicked.setText("X");//set value of button
				buttonClicked.setEnabled(false);//make button not clickable
				buttonClicked.setDisabledIcon(buttonClicked.getIcon());//make button not grayed out
			} else {

				buttonClicked.setIcon(O);
				buttonClicked.setText("O");
				buttonClicked.setEnabled(false);
				buttonClicked.setDisabledIcon(buttonClicked.getIcon());
			}
			
			
			alternate++;
			
			String yourTurn;
			if (alternate % 2 == 0) {
				yourTurn = "X";
			} else {
				yourTurn = "O";
			}

		
			//CHECK FOR A DRAW
			if (alternate % 9 == 0 && checkW() == false) {
				int n = JOptionPane.showOptionDialog(null,
						"It's a draw!  Play again?", "Game Over",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, null, null);
				if (n == 0) {
					resetButtons();
				} else {
					frame.setVisible(false);
					System.exit(0);
				}
				
			}
			//CHECK FOR A WINNER WINNER
			if (checkW() == true) {
				if (alternate % 2 == 1) {
					yourTurn = "X";
				} else {
					yourTurn = "O";
				}

				int n = JOptionPane.showOptionDialog(null,
						"Player " + yourTurn + " wins!  Play again?",
						"Game Over", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (n == 0) {
					resetButtons();
				} else {
					frame.setVisible(false);
					System.exit(0);
				}
				
			

			}
			
				
			if (yourTurn == "O" && alternate%2==0){yourTurn = "X";}
			goTurn.setText("<html><span style='font-size:14px'>"+"Your turn:  "+yourTurn+"</span></html>");
		}
		
		//goes through all buttons, sets them back to their natural state.
		public void resetButtons() {
			for (int i = 0; i <= 8; i++) {
				board[i].setText("");
				board[i].setIcon(null);
				board[i].setEnabled(true);
			}

			alternate = 0;
		}
		
		//Checks cases for a winner (2 specific Adjacencies means that there are 3 in a row)
		public boolean checkW() {

			// vertical
			if (isA(0, 3) && isA(3, 6)){
				return true;
			}
			else if (isA(2, 5) && isA(5, 8)){
				return true;
			}
			else if (isA(1, 4) && isA(4, 7)){
				return true;
			}
			
			// horizontal
			else if (isA(0, 1) && isA(1, 2)){
				return true;
			}
			else if (isA(6, 7) && isA(7, 8)){
				return true;
			}
			else if (isA(3, 4) && isA(4, 5)){
				return true;	
			}

			// diagonal
			else if (isA(0, 4) && isA(4, 8)){
				return true;
			}
			else if (isA(2, 4) && isA(4, 6)){
				return true;
			}
				return false;
		}

		//check to see if Xs or Os are next to eachother.  Text is used as the buttons "value"
		public boolean isA(int a, int b) {
			if (board[a].getText().equals(board[b].getText())
					&& !board[a].getText().equals("")) {
				return true;
			}else{
				return false;
			}
		}
	}




}
