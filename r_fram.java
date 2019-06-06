
import javax.swing.*;
import java.awt.*;
public class frame extends JFrame{ 
	
	/*
	 * TO DO
	 * - fix label ARRAY
	 * - add score, name, and timer
	 * - work on boogle.java
	 * */
	
	static String[][] board = {{"B", "A", "D", "E", "L"}, 
			{"Q", "D", "W", "P", "O"}, 
			{"F", "I", "F", "N", "T"}, 
			{"L", "O", "F", "A", "S"}, 
			{"V", "H", "K", "S", "N"}};
	static JLabel[][] boardLabelGrid = new JLabel[5][5];
  //variables
  
  //layout
  FlowLayout flowLayout = new FlowLayout();
  JPanel pan1 = new JPanel();  
  JPanel pan3 = new JPanel();
  JPanel pan4 = new JPanel(); 
  JPanel pan2 = new JPanel();
  
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
 
  //constructor
  public frame() {       
    setTitle("Boggle");
    setSize(700, 600);
    
    pan1.setLayout(new BoxLayout(pan1, BoxLayout.PAGE_AXIS));
    pan3.setLayout(new GridLayout(5,5));
    pan4.setLayout(flowLayout);
    pan2.setLayout(new FlowLayout(FlowLayout.CENTER));
    timeRemainingPanel.setLayout(new BoxLayout(timeRemainingPanel, BoxLayout.PAGE_AXIS)); //setting to flow layout
    timeRemainingPanel.setBackground(Color.BLACK);
    
    timeRemainingTitle.setFont(new Font("Courier New", Font.ITALIC, 20));

    
    timeRemainingPanel.add(timeRemainingTitle);
    timeRemainingPanel.add(timeRemaining);

    
    scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
    
    scorePanel.add(scoreTitle);
    scorePanel.add(score);
    
    pan2.add(timeRemainingPanel);
    pan2.add(scorePanel);
    
    pan1.add(introLabel, BorderLayout.CENTER);
    System.out.println(pan2);
    pan1.add(pan2);
    pan1.add(pan3);
    pan1.add(pan4);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //////// //message frame
    
    //intro
    JOptionPane.showMessageDialog(null, "Welcome to Boggle.", "How to play the game/rules", JOptionPane.INFORMATION_MESSAGE);
    Object[] possibleValues = { "One", "Two"};
    Object selectedValue = JOptionPane.showInputDialog(null,
                                                       "How many players are there?", "Input",
                                                       JOptionPane.INFORMATION_MESSAGE, null,
                                                       possibleValues, possibleValues[0]);   
    int playerNum;
    if(selectedValue.equals("One")){
      playerNum=1;
    }else{
      playerNum=2;
    }// end of getting the number of players
    int[] score=new int[playerNum];
    String[] name= new String[playerNum];
    
    Object[] possibleValues2 = { "2", "3", "4" }; //getting the min length of a word
    Object selectedValue2 = JOptionPane.showInputDialog(null,
                                                        "Please choose a minimum word length", "Input",
                                                        JOptionPane.INFORMATION_MESSAGE, null,
                                                        possibleValues2, possibleValues2[0]);
    int minLength;
    if(possibleValues2.equals("2")){
      minLength=2;
    }else if(possibleValues2.equals("3")){
      minLength=3;
    }else if(possibleValues2.equals("4")){
      minLength=4;
    }
    
    int scoreLimit;
    do{
      String scoreLimit1 = JOptionPane.showInputDialog("Please choose a score limit");//getting the score limit
      scoreLimit=Integer.valueOf(scoreLimit1); 
      if (scoreLimit < 1) {
        JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 1", "alert", JOptionPane.ERROR_MESSAGE);
      }
    }while(scoreLimit<1);
    
    for(int i=0; i<playerNum; i++){//getting the name of players
      name[i] = JOptionPane.showInputDialog(null, "Enter name of Player "+(i+1)+".", "Enter name");
    }

    //grid
    for (int i=0; i<boardLabelGrid.length; i++) {
		for (int j=0; j<boardLabelGrid.length; j++) {
			boardLabelGrid[i][j] = new JLabel(board[i][j], JLabel.CENTER);
			pan3.add(boardLabelGrid[i][j]);
		}
	}
    //score name timer
    
    
    //other components
    
    pan4.add(enterField);
    pan4.add(enterButton);
    
    //adding panels
    add(pan1);
    setVisible(true); 
    setLocationRelativeTo(null);// centering the frame
  }//end of constructor
  
  public static void main(String[] args){
    frame frame1 =new frame(); 
    

	
	
    
  }//end of main method
  
}
