
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
public class frame extends JFrame{ 
	static String[][] board = {{"B", "A", "D", "E", "L"}, 
			{"Q", "D", "W", "P", "O"}, 
			{"F", "I", "F", "N", "T"}, 
			{"L", "O", "F", "A", "S"}, 
			{"V", "H", "K", "S", "N"}};
	static JLabel[][] boardLabelGrid = new JLabel[5][5];

  
  //layout
  FlowLayout flowLayout = new FlowLayout();
  JPanel pan1 = new JPanel();  
  JPanel pan3 = new JPanel();
  JPanel pan4 = new JPanel(); 
  JPanel infoPanel = new JPanel();
  JPanel bottomButtons = new JPanel();
  
  JPanel timeRemainingPanel = new JPanel();
  JPanel scorePanel = new JPanel();
  
  JLabel timeRemainingTitle = new JLabel("Time Remaining", JLabel.CENTER);
  JLabel timeRemaining = new JLabel("0", JLabel.CENTER);
  
  JLabel scoreTitle = new JLabel("Your Score", JLabel.CENTER);
  JLabel score = new JLabel("0", JLabel.CENTER);

  //other components
  JButton enterButton = new JButton("Enter");
  JLabel introLabel = new JLabel("Welcome To Boggle", SwingConstants.CENTER); //labels
  JTextField enterField = new JTextField ("", 30);// blank text field
  Font font = new Font("Sans Serif", Font.BOLD, 20);
  Border labelBorder = BorderFactory.createEtchedBorder();
  Border upperBorder = BorderFactory.createDashedBorder(Color.BLUE, 4, 3);  
  
  //bottom buttons
  JButton shakeBoard = new JButton("Randomize Board!");
  JButton restartGame = new JButton("Restart"); //switch text to play when game state is paused
  JButton continueGame = new JButton("Continue");
  JButton exitGame = new JButton("Exit");
  JButton pass = new JButton("Pass");
  
  JPanel playerTurnPanel = new JPanel();
  JLabel playerTurnTitle = new JLabel("Player Turn:", JLabel.CENTER);
  JLabel playerTurnLabel = new JLabel("Rohan's", JLabel.CENTER);
	
  
  
  
  public frame() {       
    setTitle("Boggle");
    setSize(700, 400);
    
  //intro
    JOptionPane.showMessageDialog(null, "Welcome to Boggle.", "How to play the game/rules", JOptionPane.INFORMATION_MESSAGE);
    Object[] possibleValues = {"One", "Two"};
    Object selectedValue = JOptionPane.showInputDialog(null,
                                                       "How many players are there?", "Input",
                                                       JOptionPane.INFORMATION_MESSAGE, null,
                                                       possibleValues, possibleValues[0]);   
    int playerNum;
    if(selectedValue.equals("One")) playerNum=1;
    else playerNum=2;
    // end of getting the number of players
    String[] name = new String[playerNum];
    
    Object[] possibleValues2 = { "2", "3", "4" }; //getting the min length of a word
    Object selectedValue2 = JOptionPane.showInputDialog(null,
                                                        "Please choose a minimum word length", "Input",
                                                        JOptionPane.INFORMATION_MESSAGE, null,
                                                        possibleValues2, possibleValues2[0]);
    int minLength;
    if (possibleValues2.equals("2")) {
      minLength = 2;
    } else if(possibleValues2.equals("3")) {
      minLength = 3;
    } else if(possibleValues2.equals("4")) {
      minLength = 4;
    }
    
    int scoreLimit;
    do {
      String scoreLimit1 = JOptionPane.showInputDialog("Please choose a score limit");//getting the score limit
      scoreLimit=Integer.valueOf(scoreLimit1); 
      if (scoreLimit < 1) {
        JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 1", "alert", JOptionPane.ERROR_MESSAGE);
      }
    } while(scoreLimit<1);
    
    for(int i=0; i<playerNum; i++) { //getting the name of players
      name[i] = JOptionPane.showInputDialog(null, "Enter name of Player "+(i+1)+".", "Enter name");
    }
    
    
    pan1.setLayout(new BoxLayout(pan1, BoxLayout.PAGE_AXIS));
    pan3.setLayout(new GridLayout(5,5));
    pan4.setLayout(flowLayout);
    infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
    //Intro Label
    introLabel.setFont(font);
    introLabel.setAlignmentX(CENTER_ALIGNMENT);
    pan1.add(introLabel);
    
    //time remaining panel
    timeRemainingPanel.setLayout(new BoxLayout(timeRemainingPanel, BoxLayout.PAGE_AXIS)); //setting to flow layout
    timeRemainingTitle.setFont(font);
    timeRemaining.setFont(font);
    timeRemainingTitle.setAlignmentX(CENTER_ALIGNMENT);
    timeRemaining.setAlignmentX(CENTER_ALIGNMENT);
    timeRemainingPanel.add(timeRemainingTitle);
    timeRemainingPanel.add(timeRemaining);
    timeRemainingPanel.setBorder(upperBorder);

    //Score Panel 
    scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
    scoreTitle.setFont(font);
    score.setFont(font);
    scoreTitle.setAlignmentX(CENTER_ALIGNMENT);
    score.setAlignmentX(CENTER_ALIGNMENT);
    scorePanel.add(scoreTitle);
    scorePanel.add(score);
    scorePanel.setBorder(upperBorder);
    
    //bottom buttons
    restartGame.setPreferredSize(new Dimension(165, 25));
    continueGame.setPreferredSize(new Dimension(165, 25));
    exitGame.setPreferredSize(new Dimension(165, 25));
    
    bottomButtons.add(restartGame);
    bottomButtons.add(continueGame);
    bottomButtons.add(exitGame);
    
    if (playerNum == 2) { //If 2 player
    		
    		playerTurnPanel.setLayout(new BoxLayout(playerTurnPanel, BoxLayout.PAGE_AXIS));
    		playerTurnTitle.setFont(font);
    		playerTurnLabel.setFont(font);
    		
    		playerTurnTitle.setAlignmentX(CENTER_ALIGNMENT);
    		playerTurnLabel.setAlignmentX(CENTER_ALIGNMENT);
    		
    		playerTurnPanel.add(playerTurnTitle);
    		playerTurnPanel.add(playerTurnLabel);
    		
    		playerTurnPanel.setBorder(upperBorder);
    		
    		playerTurnPanel.setPreferredSize(new Dimension(220, 65));
    		scorePanel.setPreferredSize(new Dimension(220, 65));
    	    timeRemainingPanel.setPreferredSize(new Dimension(230, 65));
    	    infoPanel.add(playerTurnPanel);
    	    
    	    pass.setPreferredSize(new Dimension(165, 25));
    	    bottomButtons.add(pass);
    } else { //If 1 player
        timeRemainingPanel.setPreferredSize(new Dimension(325, 65));
        scorePanel.setPreferredSize(new Dimension(325, 65));
        shakeBoard.setPreferredSize(new Dimension(165, 25));

        bottomButtons.add(shakeBoard);
    }

    infoPanel.add(timeRemainingPanel);
    infoPanel.add(scorePanel);
    
    
    pan1.add(infoPanel);
    pan1.add(pan3);
    pan1.add(pan4);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //grid
    for (int i=0; i<boardLabelGrid.length; i++) {
		for (int j=0; j<boardLabelGrid.length; j++) {
			boardLabelGrid[i][j] = new JLabel(board[i][j], JLabel.CENTER);
			boardLabelGrid[i][j].setBorder(labelBorder);
			pan3.add(boardLabelGrid[i][j]);
		}
	}
    pan4.add(enterField);
    pan4.add(enterButton);
    
    pan1.add(bottomButtons); 
    
    add(pan1);
    setVisible(true); 
    setLocationRelativeTo(null); // centering the frame
  }
  
  public static void main(String[] args){
    frame frame1 =new frame(); 
  }
  
}
