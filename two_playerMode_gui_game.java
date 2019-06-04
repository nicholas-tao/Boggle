import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.math.*;

public class two_playerMode_gui_game extends JFrame {
	static int[] comp_turn = new int[2]; //array storing the ExpertAI moves
	static int max_int = 3;
	static int stones_inpile = (int)(Math.random() * 15 + 15);
	JLabel stones_reamining_label = new JLabel("The stones remaining are " + stones_inpile, JLabel.CENTER);
	JLabel turn = new JLabel("It is " + name_user1 + "'s" + " turn.", JLabel.CENTER);
	JButton takeOne = new JButton("Take one stone");
	JButton takeTwo = new JButton("Take two stones");
	JButton takeThree = new JButton("Take three stones");
	static double turns_counter = 0;
	static String name_user1 = "";
	static String name_user2 = "";
	static int counter2 = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					two_playerMode_gui_game frame = new two_playerMode_gui_game();
					frame.setVisible(true);
					//put 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public two_playerMode_gui_game() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		setLayout(new BorderLayout(0, 0));
		setTitle("Game of Nim - 2 Player Mode");
		add(stones_reamining_label, BorderLayout.PAGE_START);
		
		add(takeOne, BorderLayout.LINE_START);
		
		add(takeTwo, BorderLayout.CENTER);
		
		add(takeThree, BorderLayout.LINE_END);
		
		
		stone_1taken stone_1taken = new stone_1taken();
		stone_2taken stone_2taken = new stone_2taken();
		stone_3taken stone_3taken = new stone_3taken();
		takeOne.addActionListener(stone_1taken);
		
		takeTwo.addActionListener(stone_2taken);

		takeThree.addActionListener(stone_3taken);
		name_user1 = JOptionPane.showInputDialog(null, "Enter name of Player 1: ", "Enter name");
		name_user2 = JOptionPane.showInputDialog(null, "Enter name of Player 2: ", "Enter name");
		turn.setText("It is " + name_user1 + "'s" + " turn.");
		JOptionPane.showMessageDialog(null, "Welcome to the game of nim, " + name_user1 + ". How to play: The game starts with 15-30 stones in a pile. \nThe first player will be able to take away 1-3 stones from the pile. The second player will do the same. " + "\nEventually, there will be only one stone left, and the player to pick up the last stone will lose. The CPU will go first.", "How to play the game/rules", JOptionPane.INFORMATION_MESSAGE);
		add(turn, BorderLayout.PAGE_END);
		}
		
	public class stone_1taken implements ActionListener {
		public void actionPerformed(ActionEvent stone_1taken) {
			counter2++;
			turns_counter++;
			if (turns_counter%2==0) {
				turn.setText("It is " + name_user1 + "'s" + " turn.");
				if (counter2 < 1) {
					turn.setText("It is " + name_user2 + "'s" + " turn.");
				}
				
			} else if ((turns_counter%2)!=0) { //check if number is even or not
				turn.setText("It is " + name_user2 + "'s" + " turn.");
			}
			stones_inpile = stones_inpile-1;
			stones_reamining_label.setText("The stones remaining are " + stones_inpile);
			if (stones_inpile == 1 && turns_counter%2==0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user1 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			} else if (stones_inpile == 1 && turns_counter%2!=0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user2 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
		}
	}
	
	public class stone_2taken implements ActionListener {
		public void actionPerformed(ActionEvent stone_2taken) {
			counter2++;
			turns_counter++;
			if (turns_counter%2==0) {
				turn.setText("It is " + name_user1 + "'s" + " turn.");
				if (counter2 < 1) {
					turn.setText("It is " + name_user2 + "'s" + " turn.");
				}
				
			} else if ((turns_counter%2)!=0) { //check if number is even or not
				turn.setText("It is " + name_user2 + "'s" + " turn.");
			}
			if (stones_inpile - 2 <= 0 && turns_counter%2==0) {
				JOptionPane.showMessageDialog(null, "Sorry " + name_user1 + " you lost since you took the last stone.", "Game Ended - You have lost!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
			else if (stones_inpile - 2 <= 0 && turns_counter%2!=0) {
				JOptionPane.showMessageDialog(null, "Sorry " + name_user1 + " you lost since you took the last stone.", "Game Ended - You have lost!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
			
			stones_inpile = stones_inpile-2;
			stones_reamining_label.setText("The stones remaining are " + stones_inpile);
			if (stones_inpile == 1 && turns_counter%2==0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user1 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			} else if (stones_inpile == 1 && turns_counter%2!=0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user2 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
		}
	}
	
	public class stone_3taken implements ActionListener {
		public void actionPerformed(ActionEvent stone_3taken) {	
			counter2++;
			turns_counter++;
			if (turns_counter%2==0) {
				turn.setText("It is " + name_user1 + "'s" + " turn.");
				if (counter2 < 1) {
					turn.setText("It is " + name_user2 + "'s" + " turn.");
				}
				
			} else if ((turns_counter%2)!=0) { //check if number is even or not
				turn.setText("It is " + name_user2 + "'s" + " turn.");
			}
			if (stones_inpile - 3 <= 0 && turns_counter%2==0) {
				JOptionPane.showMessageDialog(null, "Sorry " + name_user1 + " you lost since you took the last stone.", "Game Ended - You have lost!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
			else if (stones_inpile - 3 <= 0 && turns_counter%2!=0) {
				JOptionPane.showMessageDialog(null, "Sorry " + name_user1 + " you lost since you took the last stone.", "Game Ended - You have lost!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
			
			stones_inpile = stones_inpile-3;
			stones_reamining_label.setText("The stones remaining are " + stones_inpile);
			if (stones_inpile == 1 && turns_counter%2==0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user1 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			} else if (stones_inpile == 1 && turns_counter%2!=0) {
				JOptionPane.showMessageDialog(null, "Congratulations " + name_user2 + " you have won!", "Game Ended - You have won!", JOptionPane.INFORMATION_MESSAGE);	       
				System.exit(0);
			}
			
	}
	}
}
	
	
	
