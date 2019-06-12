/*
 * Names: Rohan Ravindran, Kevin Xu, Mandana Emam, Nicholas Tao
 * Date: June 12, 2019
 * Assignment Title: Boggle
 * 
 */ 

//import needed libraries
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class frame extends JFrame implements ActionListener { 
  
  //declare global variables
  static ArrayList<String> wordsEntered = new ArrayList<String>();
  static String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"}; //array storing the 25 die
  static String [] wordList; //stores dictionary words
  static int scoreToWin; //stores score to win
  static String board[][]; //board of letters
  static int minWordLen; //minimum word length
  static boolean found = false;
  static boolean gameRunning = true;
  static Scanner sc = new Scanner(System.in); 
  static JLabel[][] boardLabelGrid = new JLabel[5][5];
  static int playerNum; //number of players
  static String[] name; //names of players
  static int playerTurn = 0; //keep track of turn
  static int[] playerScore; //scores of players
  static int [] passCounter; //counts times passed
  static boolean hasWon = false; //keeps track of whether game is paused
  static int onePlayerTimeInterval; //time limit for one player mode
  
  static int interval = 15; //for 15s timer (2P mode), it is one second higher than 15 because the timer stops at 1s, so starting from 16 ensures a 15s timer
  static Timer timer; //declare timer object
  
  //Declaring the panels used
  static JPanel gridPan = new JPanel();
  JPanel totalPan = new JPanel();  //the panel where we put all the other panels inside of
  JPanel bottomPanel = new JPanel(); //the panel where we have the enter textfield andenter button
  JPanel infoPanel = new JPanel();///topmost panel
  JPanel bottomButtons = new JPanel();//the bottom-most panel
  static JPanel validWordPanel = new JPanel();//bellow bottomPanel
  JPanel timeRemainingPanel = new JPanel();//has the remaining time label
  JPanel scorePanel = new JPanel();//has the score labels
JPanel playerTurnPanel = new JPanel();//has the players name when it is 2 players
  
  //declaring the fonts
  static Font font = new Font("Sans Serif", Font.BOLD, 20);
  static Font biggerFont = new Font ("Sans Serif", Font.BOLD, 30);
  static Font smallerFont = new Font("Sans Serif", Font.BOLD, 15);
	
  
  //declaring Components
  JButton enterButton = new JButton("Enter");
  static JLabel introLabel = new JLabel("Welcome To Boggle", SwingConstants.CENTER); //labels
  static JTextField enterField = new JTextField ("", 30);// blank text field
  JLabel timeRemainingTitle = new JLabel("Time Remaining: ", JLabel.CENTER); 
  static JLabel timeRemaining = new JLabel("0", JLabel.CENTER); //displays time remaining
  JLabel scoreTitle = new JLabel("Your Score: ", JLabel.CENTER);
  static JLabel score = new JLabel("0", JLabel.CENTER); //displays player's score, changes as program progresses
  static JLabel validWord = new JLabel("Word is ", JLabel.CENTER); //indicates whether word is valid
	 JLabel playerTurnTitle = new JLabel("Player Turn:", JLabel.CENTER);
  static JLabel playerTurnLabel = new JLabel("", JLabel.CENTER);//shows the name of the player when it is their turn
	
  //borders
  static Border labelBorder = BorderFactory.createEtchedBorder();
  Border upperBorder = BorderFactory.createDashedBorder(Color.BLUE, 4, 3); //a border
	
	
  //Bottom Buttons (options to randomize board, restart, exit, pass)
  JButton shakeBoard = new JButton("Randomize Board!");
  JButton restartGame = new JButton("Restart"); //switch text to play when game state is paused
  JButton exitGame = new JButton("Exit");
  JButton pass = new JButton("Pass");
  

  /*
   * Method sets up the game and prompts the user to enter game setup questions regarding minimum word length and score to win
   */
  public static void setupQuestions() {
    Object[] minLengthValues = { "2", "3", "4" }; //getting the min length of a word
    Object selectedValue2 = JOptionPane.showInputDialog(null,"Please choose a minimum word length", "Input",JOptionPane.INFORMATION_MESSAGE, null, minLengthValues, minLengthValues[0]);
    minWordLen = Integer.parseInt(selectedValue2.toString());//changing the value to an int value
    
    scoreToWin = 0;//initializing it to 0
    
    //do-while loop for input validation to get score to win from user
    do {
      String scoreToWinInput = JOptionPane.showInputDialog("Please choose a score to win");//getting the score limit
      try {//try-CATCH VALIDATION	
        scoreToWin = Integer.valueOf(scoreToWinInput); 
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "You have to an appropriate value", "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
        continue;
      }
      if (scoreToWin < 1) {
        JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 1", "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
      }
    } while(scoreToWin < 1);//END OF VALIDATING scoreToWin
    
    //if one player, they choose how long they get to reach the score to win (i.e. the time)
    if (playerNum == 1) {
      //do-while loop for input validation
      do {
        String input = JOptionPane.showInputDialog(null, "Enter time (secs) available for input", "Enter time");
        try {
          onePlayerTimeInterval = Integer.valueOf(input); 
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "You have enter an appropriate value", "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
          continue;
        }
        if (onePlayerTimeInterval < 5) {
          JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 5", "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
        }
      } while(onePlayerTimeInterval < 5);//end of validation
    }//end of getting the time
  }//end of set-up questions
  
  /*
   * Constructor method for the frame of the game
   */
  public frame() {
    //Intro Questions and Game Rules
	  //setting a variable gameRules to all the rules
    String gameRules = "Welcome to Boggle. Here's how to play: \n\n You must find words on the board that that are:\n" + 
      " - Touching each other, either horizontally vertically, or diagonally\n" + 
      " - Not repeating the same letter in a single word\n" + 
      " - Have more than or the same number of letters as the minimum word length\n" + 
      " - Exist in the English dictionary \n\n" + 
      "You must find all the words you can within the time limit and you will gain points by how many letters are in\nthe words you find with each letter earning you 1 point. " + 
      "If two payers are playing and they both pass twice or if\nit is one player and the player passes, the board will be shaken up. " + 
      "The players are able to restart and exit\nthe game at any given time. They can also continue playing another round after the game has finished.\n" + 
      "\nIn the end, the player to pass the score limit first wins the game and there are a few things to note:\n" + 
      " - A word can’t be repeated in the same round\n" + 
      " - A word can’t be counted twice even if it has 2 different meanings\n" +
      " - NOTE: For 2 player mode, if a player passes, the next player's turn starts IMMEDIATELY after the current player's turn. Be ready!";
    JOptionPane.showMessageDialog(null, gameRules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);//printing gameRules in an info frame
   
	  //asking for the number of players
	  Object[] possibleValues = {"One", "Two"};//scroll down ptions
    Object selectedValue = JOptionPane.showInputDialog(null,"How many players are there?", "Input",JOptionPane.INFORMATION_MESSAGE, null,possibleValues, possibleValues[0]);   
    
    if(selectedValue.equals("One")) playerNum=1; //indicates 1 player mode
    else playerNum=2; //2 player mode
    
    name = new String[playerNum]; //declaring variable num with the number of players
    
    setupQuestions(); //call method to ask setup questions
    
    //getting the name of players
    for(int i = 0; i < playerNum; i++) { 
      name[i] = JOptionPane.showInputDialog(null, "Enter name of Player "+(i+1)+".", "Enter name");//a frame pops up and they enter their name
    }
    
    //MARK: Frame Setup
    setTitle("Boggle");//the frame title
    setSize(800, 700);//the frame size
    
    //Set panel layouts
    totalPan.setLayout(new BoxLayout(totalPan, BoxLayout.PAGE_AXIS));//so that all the panels stack upon each other
    gridPan.setLayout(new GridLayout(5,5));//the board of letters
    bottomPanel.setLayout(new FlowLayout());
    infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));//appear in the center of the frame
    
    //Intro Label
    introLabel.setFont(font);//the label that communicates with the user after game has started
    introLabel.setAlignmentX(CENTER_ALIGNMENT);//making sure the label is at the center of the frame
    totalPan.add(introLabel);//adding label to the panel
    
    //Time remaining panel
    timeRemainingPanel.setLayout(new BoxLayout(timeRemainingPanel, BoxLayout.PAGE_AXIS)); //setting to flow layout
    timeRemainingTitle.setFont(font);//setting the font
    timeRemaining.setFont(font);
    timeRemainingTitle.setAlignmentX(CENTER_ALIGNMENT);//making sure its centered
    timeRemaining.setAlignmentX(CENTER_ALIGNMENT);
    timeRemainingPanel.add(timeRemainingTitle);//adding them to the panel
    timeRemainingPanel.add(timeRemaining);
    timeRemainingPanel.setBorder(upperBorder);//adding borders
    
    //Score Panel 
    scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));//setting the layout
    scoreTitle.setFont(font);//setting the font
    score.setFont(font);
    scoreTitle.setAlignmentX(CENTER_ALIGNMENT);//making sure its centered
    score.setAlignmentX(CENTER_ALIGNMENT);
    scorePanel.add(scoreTitle);//adding to panels
    scorePanel.add(score);
    scorePanel.setBorder(upperBorder);//adding borders
    
    //ValidWord Panel
    validWord.setFont(smallerFont);//setting fonts
    validWord.setAlignmentX(CENTER_ALIGNMENT);//making sure its centered
    validWordPanel.add(validWord);//adding to panel
    validWordPanel.setVisible(false);//making it invisible until necessary
    
    //Bottom buttons (restart, exit)
    restartGame.setPreferredSize(new Dimension(165, 25));//changing the default size of the buttons
    exitGame.setPreferredSize(new Dimension(165, 25));
    
    bottomButtons.add(restartGame);//adding to panel
    restartGame.addActionListener(this);//adding to actionLitsener
    
    bottomButtons.add(exitGame);
    exitGame.addActionListener(this);
    
    validWordPanel.setPreferredSize(new Dimension(400, 30));//setting the size for the panel
    
    //Unique components if 2 player mode
    if (playerNum == 2) {
      playerTurnPanel.setLayout(new BoxLayout(playerTurnPanel, BoxLayout.PAGE_AXIS));//set the layout
      playerTurnTitle.setFont(font);//set the font
      playerTurnLabel.setFont(font);
      
      playerTurnTitle.setAlignmentX(CENTER_ALIGNMENT);//making sure its centered
      playerTurnLabel.setAlignmentX(CENTER_ALIGNMENT);
      
      playerTurnPanel.add(playerTurnTitle);//adding to panel
      playerTurnPanel.add(playerTurnLabel);
      
      playerTurnPanel.setBorder(upperBorder);//setting border
      
      playerTurnPanel.setPreferredSize(new Dimension(240, 65));//changing the size of the panel
      scorePanel.setPreferredSize(new Dimension(240, 65));
      timeRemainingPanel.setPreferredSize(new Dimension(240, 65));
      
      infoPanel.add(playerTurnPanel);//adding panel to panel
      
      pass.setPreferredSize(new Dimension(165, 25));
      pass.addActionListener(this);//adding to actionLitsener
      
      bottomButtons.add(pass);
      playerScore = new int[2];//if its 2 players, we have an array
      passCounter = new int [2];
      playerTurnLabel.setText(name[playerTurn]);// setting the label to the player who's turn it is
      
    } else { //components for 1 player mode
      timeRemainingPanel.setPreferredSize(new Dimension(350, 65));
      scorePanel.setPreferredSize(new Dimension(350, 65));
      shakeBoard.setPreferredSize(new Dimension(165, 25));
      shakeBoard.addActionListener(this); //button actionLitsener
      
      bottomButtons.add(shakeBoard);//adding to the panel
      playerScore = new int[1]; //an array with length of 1 since thwere is only 1 player
    }
    
    //add panels to panels
    infoPanel.add(timeRemainingPanel);//upper segment
    infoPanel.add(scorePanel);
    
    bottomPanel.add(enterField);//lower segment
    bottomPanel.add(enterButton);
    bottomPanel.add(validWordPanel);
    
    gridPan.setPreferredSize(new Dimension(600, 300));//he board size
    
    //adding panels to totalPan
    totalPan.add(infoPanel);//adding all the segmets together
    totalPan.add(gridPan);
    totalPan.add(bottomPanel);
    totalPan.add(bottomButtons); //adding panels
    
    enterButton.addActionListener(this);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    add(totalPan);//adding the panwel to the frame
    setVisible(true); //setting frame to be visible
    setLocationRelativeTo(null); //centering the frame
  }
  
  /*
   * Method restarts the game by calling appropriate methods and reseting variables
   */
  public static void resartGame() {
    setupQuestions();//ask the elementary questions
    randomizeBoard(board);//randomize die position and die face
    updateBoard(board);//tranfer it to show on GUI
    enterField.setText("");//emphtying the textfield
    validWordPanel.setVisible(false);//still invisible
    wordsEntered.removeAll(wordsEntered);//removing all past history
    
    hasWon = false;
    if (playerNum == 1) {
      playerScore[0] = 0;//initializing score to 0
      score.setText(Integer.toString(playerScore[0]));//changing the score label
      interval = onePlayerTimeInterval;
      startTimer();//timer started
    } else {//if there are 2 players
      playerScore[0] = 0;//initializing score to 0
      playerScore[1] = 0;
      playerTurn = 0;//initializing turn to 0
      passCounter[0] = 0;//initializing number of passes to 0
      passCounter[1] = 0;
      
      score.setText(Integer.toString(playerScore[playerTurn]));//changing the score label
      playerTurnLabel.setText(name[playerTurn]);//setting the name of the player whos's turn it is
      interval = 15;
      startTimer();//starting timer
    }
  }
  
  /*
   * Method determines the winner by comparing users' score with score limit
   */
  public static void checkWon() {//checks to see if win conditions have been met
    if (playerScore[playerTurn] >= scoreToWin) { //if player has reached score limit
      timer.cancel(); //cancel timer
      hasWon = true;
	    //dialogue frame asking if they want to play again
      Object[] resartGameValues = {"Yes", "No"};//scroll down options
      Object selectedValue = JOptionPane.showInputDialog(null, "Congrats, " + name[playerTurn] +". You have finished the game by reaching " + scoreToWin + " points! \nWould you like to play again?", "Game Finished!",JOptionPane.INFORMATION_MESSAGE, null,resartGameValues, resartGameValues[1]);
      
      //try-catch for input validation
      try {
        if (selectedValue.equals("Yes")) {
          resartGame();//restarting the game if they want to play again
        } else {
          System.exit(0);// exiting the game if they didnt want to play again
        }
      } catch (Exception e) {
	  JOptionPane.showMessageDialog(null, "Error Message: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
      }//end of try catch
    }//end of if they won
  }//end of checkWon
  
  /*
   * Method that calls appropriate methods when actions are performed
   */ 
  public void actionPerformed(ActionEvent event) { //GUI ACTION RESPONSES
    if (event.getSource() == enterButton) { //Pressed Enter Button
      String word = enterField.getText();//gets the inputed word
      validWordPanel.setVisible(true);//sets the validWordPanel to be visible
      
      if (validate(word, minWordLen, wordList, wordsEntered, board)) { //call validate method to check if word is valid
        playerScore[playerTurn] += word.length();//dding the score, 1 point for every letter in a valid word
        score.setText(Integer.toString(playerScore[playerTurn]));
        wordsEntered.add(word); //add word to list of entered words
        validWord.setText("Word is VALID!");// setting the text inside validWordPanel
        validWordPanel.setBorder(BorderFactory.createDashedBorder(Color.GREEN, 4, 3));//adding green border around validWordPanel
        enterField.setText(""); //clear text field
        
        checkWon(); //checks if player has won
        
      } else {// if the entered word was not valid
        validWord.setText("Word is INVALID!");// setting the text inside validWordPanel
        validWordPanel.setBorder(BorderFactory.createDashedBorder(Color.RED, 4, 3));//adding red border around validWordPanel
      }//end of validating the inputted word and following actions
	    
    } else if (event.getSource() == shakeBoard) { //Pressed Randomize Board Button
      randomizeBoard(board); //randomize the board
      updateBoard(board); //update board in GUI
	    introLabel.setText("Board Has Been Randomized");//informing the player the board has been randomized
      
    } else if (event.getSource() == exitGame) { //Pressed Exit Game Button
      timer.cancel();  //cancel timer to make sure it doesn't run while they are trying to exit
      String exitMessage = "";
      
      //if 2 players, must get other player to confirm that they would like to exit game as well
      if (playerNum == 2) {
        if (playerTurn == 0) {
          exitMessage = name[playerTurn+1] + ", are you sure you would like to exit?"; //asking the other player if they also want to exit
        } else {
          exitMessage = name[playerTurn-1] + ", are you sure you would like to exit?";//get a pop up frame
        }
      } else {//if there was only 1 player
        exitMessage = "Are you sure you would like to exit?";//asking for confirmation
      }
      //pause timer
      Object[] exitGameValues = {"Yes", "No"};// scroll down options
      Object selectedValue = JOptionPane.showInputDialog(null, exitMessage, "Exit Game",JOptionPane.INFORMATION_MESSAGE, null, exitGameValues, exitGameValues[1]);
      
      //try-catch for input validation
      try {
        if (selectedValue.equals("Yes")) {
          System.exit(0);//exiting the game
        } else {
          startTimer();//starting the timer for another turn
        }
      } catch (Exception e) {
        startTimer();
       JOptionPane.showMessageDialog(null, "Error Message: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
      }//end of try-catch
      
    } else if (event.getSource() == restartGame) {
      timer.cancel(); //cancel timer to make sure it doesn't run while they are trying to restart
      
      //ensure that other player wants to restart as well
      String exitMessage = "";
      if (playerNum == 2) {
        if (playerTurn == 0) {
          exitMessage = name[playerTurn+1] + ", are you sure you would like to restart?";//asking the other player if they also want to restart
        } else {
          exitMessage = name[playerTurn-1] + ", are you sure you would like to restart?";//get a pop up frame
        }
      } else {// if there is only 1 player
        exitMessage = "Are you sure you would like to restart?";//initializing exit message
      }
      
      Object[] resartGameValues = {"Yes", "No"};//scroll down option
	    //printing exit message
      Object selectedValue = JOptionPane.showInputDialog(null, exitMessage, "Restart Game",JOptionPane.INFORMATION_MESSAGE, null, resartGameValues, resartGameValues[1]);
      
      //try-catch for input validation
      try {
        if (selectedValue.equals("Yes")) {
          resartGame();//restrat the game 
        } else {
          startTimer();//start the timer for another turn
        }
      } catch (Exception e) {
        startTimer();
        JOptionPane.showMessageDialog(null, "Error Message: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
      }
    } else if (event.getSource() == pass) { //if player passes
      passCounter[playerTurn]++; //add one to counter counting number of times passed
      if (playerTurn == 0) playerTurn = 1; //switch to next player 
      else playerTurn = 0;//switching turns
      score.setText(Integer.toString(playerScore[playerTurn]));//switching the score to that of the next player
      
      //Resetting timer
      timer.cancel();
      interval = 15;
      startTimer();
      playerTurnLabel.setText(name[playerTurn]);//changing the name since the turn changes
      if (passCounter[0] >=2 && passCounter[1] >= 2) { //both players have to pass twice to randomize the board
        passCounter[0] = 0;//renewing the values
        passCounter[1] = 0;
        randomizeBoard(board);//randomizing the board
        updateBoard(board);//transferring it to GUI
      }
    }
  }
  /*
   * Main method to run GUI and setup game
   */ 
  public static void main(String[] args) throws Exception {
    frame frame1 =new frame(); //putting the frame
    startMusic("sound.aiff"); //start music
    
    final int BOARD_SIZE = 5; //board size is 5
    board = new String[BOARD_SIZE][BOARD_SIZE]; //declare a 2D array for the board
    wordList = readFromFile(); //wordList stores dictionary words by calling the method
    
    randomizeBoard(board); //randomize the board
    updateBoard(board); //update the board in the GUI
    
    if (playerNum == 1) interval = onePlayerTimeInterval; //if one player mode, set the timer to the time they inputted
    if (playerNum==2)  playerTurn = (int)(Math.random()*2); // if 2 player, "flip a coin" (generate a random number from 0 to 1) to see who goes first
    getReady();
    startTimer(); //start the timer
    
  }
  
  /*
   * Method lets user know that their turn is starting and counts down
   */ 
  public static void getReady () {//countdown to start the timer
    introLabel.setText(name[playerTurn] +", your turn starts in...");
    playerTurnLabel.setText(name[playerTurn]);
    timeDelays(1500);
    introLabel.setText("3....");
    timeDelays(1000);//delays for a second
    introLabel.setText("2.....");
    timeDelays(1000);
    introLabel.setText("1.....");
    timeDelays(1000);
    introLabel.setText("The Timer has Started!!");
  }
  /*
   * Method sets time delays which are used for countdown messages in getReady()
   */ 
  public static void timeDelays (int time) { // method to add time delays between print statements.
    try {  
      Thread.sleep(time);
    } catch (Exception e) {}
    //timeDelays(1000);
  } // end of time delays
  
  /*
   * Method starts a timer and runs for certain time interval.
   * The time interval is called by setTimerInterval method
   */ 
  public static void startTimer() {//starts timer
		 int delay = 1000;//delay of 1 second
		 int period = 1000;
		 timer = new Timer();
		 timeRemaining.setText(Integer.toString(interval));//changing the timeRemaining Label
		 timer.scheduleAtFixedRate(new TimerTask() {
		     public void run() {
		    	 	//System.out.println(setTimerInterval());
		    	 	timeRemaining.setText(Integer.toString(setTimerInterval()));//changing the timeRemaining Label
		     }
		 }, delay, period);
	  }
	  
  
  /*
   * Method sets the interval of a timer 
   */
  public static int setTimerInterval() {
	    if (interval == 0 && playerNum == 2) { //once timer is finished and there are 2 players
	    		if (playerTurn == 0) { playerTurn = 1; }//switching players
	    		else { playerTurn = 0; }
	    		playerTurnLabel.setText(name[playerTurn]);//changing the name to the other players
	    		score.setText(Integer.toString(playerScore[playerTurn]));//output the players score
	        timer.cancel();
	        checkWon();//check to see if player won
  			if (!hasWon) {//continue if they dint win
  				interval = 16;
  				startTimer();
  			}
		} else if (interval == 0 && playerNum == 1) {//if thwere is 1 player
			if (playerScore[playerTurn] < scoreToWin) {
				timer.cancel();//stop timer
				hasWon = true;
				Object[] resartGameValues = {"Yes", "No"};// scroll down options
				//telling them that they lost and asking if they want to play again
				Object selectedValue = JOptionPane.showInputDialog(null,"Sorry, " + name[playerTurn] +". You have lost! \nWould you like to play again?", "Game Over!", JOptionPane.INFORMATION_MESSAGE, null, resartGameValues, resartGameValues[1]);
				try {
					if (selectedValue.equals("Yes")) {
					  resartGame();//restarting game
					} else {
					  System.exit(0);//exiting game
					}
				} catch (Exception e) {
				  
        JOptionPane.showMessageDialog(null, "Error Message: "+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);//ERROR MESSAGE
				}//end of try catch
			}//end of if they lost
		}//end of if its 1 player
	    return --interval;
	}//end of setTimerInterval method
  /*
   * Method updates the board by removing everything and setting the border and font
   */
  public static void updateBoard(String[][] board) {//tranfers the letters to the GUI
    gridPan.removeAll();
    for (int i = 0; i < boardLabelGrid.length; i++) {
      for (int j = 0; j< boardLabelGrid.length; j++) {
        boardLabelGrid[i][j] = new JLabel(board[i][j], JLabel.CENTER);//making sure it is centered
        boardLabelGrid[i][j].setBorder(labelBorder);//adding a border
        boardLabelGrid[i][j].setFont(biggerFont);//making the font bigger
        gridPan.add(boardLabelGrid[i][j]);//adding the label to the panel
      }
    }
    gridPan.revalidate();
    gridPan.repaint();
  }//end of updateBoard method
  
  /*
   * Method checks if the word is valid by calling methods to check a certain characteristic. If all methods return true,
   * then validate returns true; otherwise, it returns false.
   */
  public static boolean validate(String word, int wordLen, String[] wordList, ArrayList<String> wordsEntered, String[][] board) {//checks to see if the input matches all the criteria
    int min = 0;
    int max = wordList.length-1;
    String wordLowerCase = word.toLowerCase();//changing it all to lower case
    if (checkDict(wordList, wordLowerCase, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {//checking the dictionary for the word, checking if the length matchwes the minLength, checking if the letters are touching each other and they are on the board
      return true;//they met all the conditions
    }
    return false;//they did not meet ALL conditions
  }
  
  /*
   * Method checks if the word is in the dictionary by using recursive binary search
   * if the word is in the dictionary, then method returns the index of the word; otherwise, it returns -1
   */
  public static int checkDict(String[] wordList, String word, int min, int max) {//checks to see if the word is in an English Dictionary
    int middle = (max + min)/2;
    if (max < min) {
      return -1;
    }
    if (word.compareTo(wordList[middle])==0) {//recursive search
      return middle;
    } else if (word.compareTo(wordList[middle]) > 0) {
      return checkDict(wordList, word, middle+1, max);
    } else if (word.compareTo(wordList[middle]) < 0) {
      return checkDict(wordList, word, min, middle-1);
    }
    return -1;// if not in the dictionary
  }
  
  /*
   * Method returns true if word length is at least the minimum word length. Otherwise,
   * it returns false.
   */ 
  public static boolean checkLength(String word, int wordLen) {//checks to see if the letter length is longer than the minLength
    if (word.length() >= wordLen) {
      return true;//it is
    }
    return false;// it isnt
  }
  /*
   * Method returns true if word entered has not been entered before. Otherwise, it 
   * returns false.
   */ 
  public static boolean checkDuplicateWord(ArrayList<String> usedWords, String word) {//checks to see if they had already found this word before and are repeating the same word
    if (usedWords.contains(word)) {//arrayList
      return false;
    }    
    return true;
  }
  
  /*
   * Method checks if the word's letters are horizontally, vertically and diagonally adjacent to each other.
   * If the word is found, it returns true; otherwise it returns false
   */
  public static boolean checkAdjacent(String[][] board, String word) {
    word = word.toUpperCase(); //Setting the word to upper case as string comparison is case sensitive 
    for (int i=0; i<board.length; i++) { //nested for-loop looping over the board 2d array
      for (int j=0; j<board[i].length; j++) { 
        if (board[i][j].equals(Character.toString(word.charAt(0)))) { //if the board at index (i, j) equals to the first character of the word
          gridSearch(board, i, j, -1, -1, word, 0, word.length()-1); //run the recursive gridSearch method with (i, j) as the initial coordinates
        }
        if (found) { //if global variable found is true
          found = false; //set true to false
          return true; //return true
        }
      }
    }
    return false; //otherwise return false
  }
  /*
   * Method returns true if a pair of coordinates (x, y) are of valid
   * position on the board of characters. Otherwise, returns false.
   */
  public static boolean indexValid(String[][] board, int row, int col, int prevRow, int prevCol) {
    int len = board.length; //initlizing variable len to be the length of the board (grid of characters
    if ((row >= 0 && col >= 0 && row < len && col < len) && !(prevRow == row && prevCol == col)) { //checking if the index (row, col) is valid
      return true;
    } else {
      return false;
    }
  }
  /*
   *  Method recursively searches in 8 directions around the character until it is
   *  able to find all the characters of the word on the grid.
   */
  public static void gridSearch(String[][] board, int row, int col, int prevRow, int prevCol, String word, int index, int wordLen) {
    int[] x = {-1, -1, -1, 0, 0, 1, 1, 1}; //initializing an array of x positions to search around the previous character
    int[] y = {-1, 0, 1, -1, 1, -1, 0, 1}; //initializing an array of y positions to search around the previous character
    
    if (index > wordLen || !board[row][col].equals(Character.toString(word.charAt(index)))) { //checking if the current index is larger than the length of 
      return;                    //the word or if the board at index (row, col) does not equal
    }                      //the word at current index. If true, return and end search.
    if (index == wordLen) { //base case: if index equals the length of the word, that means that the search has successfully found all characters in the 
      found = true;     //word on the board. 
      return;      //if true: Set found equals to true, return and end search.
    }
    for (int i=0; i < 8; i++) { //for loop for searching in 8 directions around current character
      if (indexValid(board, (row + x[i]), (col + y[i]), prevRow, prevCol)) { //if the index (row, col) is valid 
        gridSearch(board, row + x[i], col + y[i], row, col, word, index+1, wordLen); //run recursive method gridSearch to search around current character
      }
    }
  }
  /*
   * Method generates a random board of letters
   */ 
  public static void randomizeBoard(String [][] board) {
    ArrayList <Integer> ranNums = new ArrayList <Integer>(); 
    for (int i = 0; i < die.length; i++) {
      ranNums.add(i); //initialize arraylist with values from 0 to die.length-1 (inclusive)
    }
    Collections.shuffle(ranNums); //randomize order of values
    
    int count = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = die[ranNums.get(count)]; //set board[i][j] to a previously generated random indexed String from die array
        int randIndex = (int)(Math.random() * (board[i][j]).length()); //generate random number from 0 to length of String
        board[i][j] = Character.toString(board[i][j].charAt(randIndex)); //set board[i][j] to a random character within the String
        count++; //add one to counter so that next previously generated random number gets used
      }
    }
  }
  
  /*
   * Method reads from a dictionary file and adds them to an arraylist.
   * It returns the arraylist converted to an array
   */ 
  
  public static String [] readFromFile()  throws Exception  {
    Scanner readFile = new Scanner(new File("wordlist.txt"),"UTF-8"); //declare scanner to read text file
    ArrayList<String> wordArrayList = new ArrayList<String>(); //arraylist to store words from file
    while(readFile.hasNext()) {
      wordArrayList.add(readFile.nextLine());
    }
    readFile.close();
    return wordArrayList.toArray(new String [wordArrayList.size()]); //convert arraylist to array
  }
  /*
   * Method plays music on a continuous loop
   */ 
  public static void startMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    AudioInputStream audioInputStream;
    audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile()); 
    Clip clip = AudioSystem.getClip(); 
    clip.open(audioInputStream);
    clip.loop(Clip.LOOP_CONTINUOUSLY); 
    clip.start();
  }
  
}
