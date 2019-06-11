/*
 * Names: Rohan Ravindran, Kevin Xu, Mandana Emam, Nicholas Tao
 * Date: June 12, 2019
 * Assignment Title: Boggle
 * 
 */ 

/*
 * TO DO:
 *  - comment code
 *  - clean up code
 *  - implement mandana's stuff?
 * EXTRAS
 * - implement welcome screen, and score board and analysis after each game
 * - Score Board & End Game Screen
 * */

//import needed libraries
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.sound.sampled.*;
import java.util.ArrayList;
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
  static long remaining = 15000000000L;  //equivalent to 15s
  static JLabel[][] boardLabelGrid = new JLabel[5][5];
  static int playerNum; //number of players
  static String[] name; //names of players
  static int playerTurn = 0; //keep track of turn
  static int[] playerScore; //scores of players
  static int passCounter = 0; //counts times passed
  static boolean gamePause = false;
  static int onePlayerTimeInterval;
  
  
  static int interval = 15; //for 15s timer 
  static Timer timer; //declare timer object
  
  //GUI Components
  static JPanel gridPan = new JPanel();
  JPanel totalPan = new JPanel();  
  JPanel bottomPanel = new JPanel(); 
  JPanel infoPanel = new JPanel();
  JPanel bottomButtons = new JPanel();
  static JPanel validWordPanel = new JPanel();
  
  JPanel timeRemainingPanel = new JPanel();
  JPanel scorePanel = new JPanel();
  
  JLabel timeRemainingTitle = new JLabel("Time Remaining: ", JLabel.CENTER); 
  static JLabel timeRemaining = new JLabel("0", JLabel.CENTER); //displays time remaining
  
  JLabel scoreTitle = new JLabel("Your Score: ", JLabel.CENTER);
  static JLabel score = new JLabel("0", JLabel.CENTER); //displays player's score
  
  static JLabel validWord = new JLabel("Word is ", JLabel.CENTER); //indicates whether word is valid
  
  //Other Components
  JButton enterButton = new JButton("Enter");
  JLabel introLabel = new JLabel("Welcome To Boggle", SwingConstants.CENTER); //labels
  static JTextField enterField = new JTextField ("", 30);// blank text field
  static Font font = new Font("Sans Serif", Font.BOLD, 20);
  static Font smallerFont = new Font("Sans Serif", Font.BOLD, 15);
  static Border labelBorder = BorderFactory.createEtchedBorder();
  Border upperBorder = BorderFactory.createDashedBorder(Color.BLUE, 4, 3);  
  
  //Bottom Buttons (options to randomize board, restart, exit, pass)
  JButton shakeBoard = new JButton("Randomize Board!");
  JButton restartGame = new JButton("Restart"); //switch text to play when game state is paused
  JButton exitGame = new JButton("Exit");
  JButton pass = new JButton("Pass");
  
  JPanel playerTurnPanel = new JPanel();
  JLabel playerTurnTitle = new JLabel("Player Turn:", JLabel.CENTER);
  static JLabel playerTurnLabel = new JLabel("", JLabel.CENTER);
  
  /*
   * Method sets up the game and prompts the user to enter game setup questions regarding minimum word length and score to win
   */
  public static void setupQuestions() {
    Object[] minLengthValues = { "2", "3", "4" }; //getting the min length of a word
    Object selectedValue2 = JOptionPane.showInputDialog(null,
                                                        "Please choose a minimum word length", "Input",
                                                        JOptionPane.INFORMATION_MESSAGE, null,
                                                        minLengthValues, minLengthValues[0]);
    minWordLen = Integer.parseInt(selectedValue2.toString());
    
    scoreToWin = 0;
    
    //do-while loop for input validation to get score to win from user
    do {
      String scoreToWinInput = JOptionPane.showInputDialog("Please choose a score to win");//getting the score limit
      
      try {
        scoreToWin = Integer.valueOf(scoreToWinInput); 
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "You have to an appropriate value", "ERROR", JOptionPane.ERROR_MESSAGE);
        continue;
      }
      
      if (scoreToWin < 1) {
        JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 1", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
      
    } while(scoreToWin < 1);
    
    //if one player, they choose how long they get to reach the score to win
    if (playerNum == 1) {
      //do-while loop for input validation
      do {
        String input = JOptionPane.showInputDialog(null, "Enter time (secs) available for input", "Enter time");
        
        try {
          onePlayerTimeInterval = Integer.valueOf(input); 
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "You have to an appropriate value", "ERROR", JOptionPane.ERROR_MESSAGE);
          continue;
        }
        
        if (onePlayerTimeInterval < 5) {
          JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 5", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
      } while(onePlayerTimeInterval < 5);
    }
  }
  
  /*
   * Constructor method for the frame of the game
   */
  public frame() {
    //Intro Questions and Game Rules
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
      " - A word can’t be counted twice even if it has 2 different meanings\n";
    JOptionPane.showMessageDialog(null, gameRules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    Object[] possibleValues = {"One", "Two"};
    Object selectedValue = JOptionPane.showInputDialog(null,
                                                       "How many players are there?", "Input",
                                                       JOptionPane.INFORMATION_MESSAGE, null,
                                                       possibleValues, possibleValues[0]);   
    
    if(selectedValue.equals("One")) playerNum=1; //indicates 1 player mode
    else playerNum=2; //2 player mode
    
    name = new String[playerNum]; 
    
    setupQuestions(); //call method to ask setup questions
    
    //getting the name of players
    for(int i=0; i<playerNum; i++) { 
      name[i] = JOptionPane.showInputDialog(null, "Enter name of Player "+(i+1)+".", "Enter name");
    }
    
    //MARK: Frame Setup
    setTitle("Boggle");
    setSize(800, 530);
    
    //Set panel layouts
    totalPan.setLayout(new BoxLayout(totalPan, BoxLayout.PAGE_AXIS));
    gridPan.setLayout(new GridLayout(5,5));
    bottomPanel.setLayout(new FlowLayout());
    infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
    //Intro Label
    introLabel.setFont(font);
    introLabel.setAlignmentX(CENTER_ALIGNMENT);
    totalPan.add(introLabel);
    
    //Time remaining panel
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
    
    //ValidWord Panel
    validWord.setFont(smallerFont);
    validWord.setAlignmentX(CENTER_ALIGNMENT);
    validWordPanel.add(validWord);
    validWordPanel.setVisible(false);
    
    //Bottom buttons (restart, exit)
    restartGame.setPreferredSize(new Dimension(165, 25));
    exitGame.setPreferredSize(new Dimension(165, 25));
    
    bottomButtons.add(restartGame);
    restartGame.addActionListener(this);
    
    bottomButtons.add(exitGame);
    exitGame.addActionListener(this);
    
    validWordPanel.setPreferredSize(new Dimension(400, 30));
    
    //Unique components if 2 player mode
    if (playerNum == 2) {
      playerTurnPanel.setLayout(new BoxLayout(playerTurnPanel, BoxLayout.PAGE_AXIS));
      playerTurnTitle.setFont(font);
      playerTurnLabel.setFont(font);
      
      playerTurnTitle.setAlignmentX(CENTER_ALIGNMENT);
      playerTurnLabel.setAlignmentX(CENTER_ALIGNMENT);
      
      playerTurnPanel.add(playerTurnTitle);
      playerTurnPanel.add(playerTurnLabel);
      
      playerTurnPanel.setBorder(upperBorder);
      
      playerTurnPanel.setPreferredSize(new Dimension(257, 65));
      scorePanel.setPreferredSize(new Dimension(257, 65));
      timeRemainingPanel.setPreferredSize(new Dimension(257, 65));
      
      infoPanel.add(playerTurnPanel);
      
      pass.setPreferredSize(new Dimension(165, 25));
      pass.addActionListener(this);
      
      bottomButtons.add(pass);
      playerScore = new int[2];
      playerTurnLabel.setText(name[playerTurn]);
      
    } else { //components for 1 player mode
      timeRemainingPanel.setPreferredSize(new Dimension(390, 65));
      scorePanel.setPreferredSize(new Dimension(390, 65));
      shakeBoard.setPreferredSize(new Dimension(165, 25));
      shakeBoard.addActionListener(this); 
      
      bottomButtons.add(shakeBoard);
      playerScore = new int[1]; 
    }
    
    //add panels to panels
    infoPanel.add(timeRemainingPanel);
    infoPanel.add(scorePanel);
    
    bottomPanel.add(enterField);
    bottomPanel.add(enterButton);
    bottomPanel.add(validWordPanel);
    
    gridPan.setPreferredSize(new Dimension(800, 200));
    
    //adding panels to totalPan
    totalPan.add(infoPanel);
    totalPan.add(gridPan);
    totalPan.add(bottomPanel);
    totalPan.add(bottomButtons); //adding panels
    
    enterButton.addActionListener(this);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    add(totalPan);
    setVisible(true); 
    setLocationRelativeTo(null); //centering the frame
  }
  
  /*
   * Method restarts the game by calling appropriate methods and reseting variables
   */
  public static void resartGame() {
    setupQuestions();
    randomizeBoard(board);
    updateBoard(board);
    enterField.setText("");
    validWordPanel.setVisible(false);
    wordsEntered.removeAll(wordsEntered);
    
    gamePause = false;
    if (playerNum == 1) {
      playerScore[0] = 0;
      score.setText(Integer.toString(playerScore[0]));
      interval = onePlayerTimeInterval;
      startTimer();
    } else {
      playerScore[0] = 0;
      playerScore[1] = 0;
      playerTurn = 0;
      passCounter = 0;
      
      score.setText(Integer.toString(playerScore[playerTurn]));
      playerTurnLabel.setText(name[playerTurn]);
      interval = 15;
      startTimer();
    }
  }
  
  /*
   * Method determines the winner by comparing users' score with score limit
   */
  public static void checkWon() {
    if (playerScore[playerTurn] >= scoreToWin) {
      timer.cancel();
      gamePause = true;
      Object[] resartGameValues = {"Yes", "No"};
      Object selectedValue = JOptionPane.showInputDialog(null,
                                                         "Congrats, " + name[playerTurn] +". You have finished the game by reaching " + scoreToWin + " points! \nWould you like to play again?", "Game Finished!",
                                                         JOptionPane.INFORMATION_MESSAGE, null,
                                                         resartGameValues, resartGameValues[1]);
      
      try {
        if (selectedValue.equals("Yes")) {
          resartGame();
        } else {
          System.exit(0);
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
  /*
   * Method that calls appropriate methods when actions are performed
   */ 
  public void actionPerformed(ActionEvent event) { //GUI ACTION RESPONSES
    if (event.getSource() == enterButton) { //Pressed Enter Button
      String word = enterField.getText();
      validWordPanel.setVisible(true);
      
      //if word is valid
      if (validate(word, minWordLen, wordList, wordsEntered, board)) {
        playerScore[playerTurn] += word.length();
        score.setText(Integer.toString(playerScore[playerTurn]));
        wordsEntered.add(word);
        validWord.setText("Word is VALID!");
        validWordPanel.setBorder(BorderFactory.createDashedBorder(Color.GREEN, 4, 3));
        enterField.setText("");
        
        checkWon();
        
      } else {
        validWord.setText("Word is INVALID!");
        validWordPanel.setBorder(BorderFactory.createDashedBorder(Color.RED, 4, 3));
        
      }
    } else if (event.getSource() == shakeBoard) { //Pressed Randomize Board Button
      randomizeBoard(board);
      updateBoard(board);
      System.out.println("Random Board");
      
    } else if (event.getSource() == exitGame) { //Pressed Exit Game Button
      timer.cancel();  //cancel timer to make sure it doesn't run while they are trying to exit
      String exitMessage = "";
      
      //if 2 players, must get other player to confirm that they would like to exit game as well
      if (playerNum == 2) {
        if (playerTurn == 0) {
          exitMessage = name[playerTurn+1] + ", are you sure you would like to exit?"; 
        } else {
          exitMessage = name[playerTurn-1] + ", are you sure you would like to exit?";
        }
      } else {
        exitMessage = "Are you sure you would like to exit?";
      }
      //pause timer
      Object[] exitGameValues = {"Yes", "No"};
      Object selectedValue = JOptionPane.showInputDialog(null,
                                                         exitMessage, "Exit Game",
                                                         JOptionPane.INFORMATION_MESSAGE, null,
                                                         exitGameValues, exitGameValues[1]);
      
      try {
        if (selectedValue.equals("Yes")) {
          System.exit(0);
        } else {
          startTimer();
        }
      } catch (Exception e) {
        startTimer();
        System.out.println(e.getMessage());
      }
      
    } else if (event.getSource() == restartGame) {
      timer.cancel(); //cancel timer to make sure it doesn't run while they are trying to restart
      
      String exitMessage = "";
      if (playerNum == 2) {
        if (playerTurn == 0) {
          exitMessage = name[playerTurn+1] + ", are you sure you would like to restart?";
        } else {
          exitMessage = name[playerTurn-1] + ", are you sure you would like to restart?";
        }
      } else {
        exitMessage = "Are you sure you would like to restart?";
      }
      //pause timer
      Object[] resartGameValues = {"Yes", "No"};
      Object selectedValue = JOptionPane.showInputDialog(null,
                                                         exitMessage, "Restart Game",
                                                         JOptionPane.INFORMATION_MESSAGE, null,
                                                         resartGameValues, resartGameValues[1]);
      try {
        if (selectedValue.equals("Yes")) {
          resartGame();
        } else {
          startTimer();
        }
      } catch (Exception e) {
        startTimer();
        System.out.println(e.getMessage());
      }
    } else if (event.getSource() == pass) {
      passCounter++;
      if (playerTurn == 0) playerTurn = 1;
      else playerTurn = 0;
      score.setText(Integer.toString(playerScore[playerTurn]));
      
      //Resetting timer
      timer.cancel();
      interval = 15;
      startTimer();
      playerTurnLabel.setText(name[playerTurn]);
      if (passCounter % 4 == 0) { //both players have to pass twice to randomize the board
        randomizeBoard(board);
        updateBoard(board);
      }
    }
  }
  /*
   * Main method to run GUI and setup game
   */ 
  public static void main(String[] args) throws Exception {
    frame frame1 =new frame(); 
    startMusic("sound.aiff"); //start music
    
    final int BOARD_SIZE = 5; //board size is 5
    board = new String[BOARD_SIZE][BOARD_SIZE]; //declare a 2D array for the board
    wordList = readFromFile(); //wordList stores dictionary words by calling the method
    
    randomizeBoard(board);
    updateBoard(board);
    
    if (playerNum == 1) interval = onePlayerTimeInterval;
    
    startTimer();
    
  }
  /*
   * Method starts a timer and runs for certain time interval.
   * The time interval is called by setTimerInterval method
   */ 
  public static void startTimer() {
    int delay = 1000;
    int period = 1000;
    timer = new Timer();
    timeRemaining.setText(Integer.toString(interval));
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        timeRemaining.setText(Integer.toString(setTimerInterval()));
      }
    }, delay, period);
  }
  
  /*
   * Method sets the interval of a timer 
   */
  public static int setTimerInterval() {
    if (interval == 1 && playerNum == 2) { //once timer is finished
      if (playerTurn == 0) { playerTurn = 1; }
      else { playerTurn = 0; }
      playerTurnLabel.setText(name[playerTurn]);
      score.setText(Integer.toString(playerScore[playerTurn]));
      timer.cancel();
      checkWon();
      if (!gamePause) {
        interval = 16;
        startTimer();
      }
    } else if (interval == 1 && playerNum == 1) {
      if (playerScore[playerTurn] < scoreToWin) {
        timer.cancel();
        gamePause = true;
        Object[] resartGameValues = {"Yes", "No"};
        Object selectedValue = JOptionPane.showInputDialog(null,
                                                           "Sorry, " + name[playerTurn] +". You have lost! \nWould you like to play again?", "Game Over!",
                                                           JOptionPane.INFORMATION_MESSAGE, null,
                                                           resartGameValues, resartGameValues[1]);
        
        try {
          if (selectedValue.equals("Yes")) {
            resartGame();
          } else {
            System.exit(0);
          }
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
    return --interval;
  }
  /*
   * Method updates the board by removing everything and setting the border and font
   */
  public static void updateBoard(String[][] board) {
    gridPan.removeAll();
    for (int i=0; i<boardLabelGrid.length; i++) {
      for (int j=0; j<boardLabelGrid.length; j++) {
        boardLabelGrid[i][j] = new JLabel(board[i][j], JLabel.CENTER);
        boardLabelGrid[i][j].setBorder(labelBorder);
        boardLabelGrid[i][j].setFont(font);
        gridPan.add(boardLabelGrid[i][j]);
      }
    }
    gridPan.revalidate();
    gridPan.repaint();
  }
  
  /*
   * Method checks if the word is valid by calling methods to check a certain characteristic. If all methods return true,
   * then validate returns true; otherwise, it returns false.
   */
  public static boolean validate(String word, int wordLen, String[] wordList, ArrayList<String> wordsEntered, String[][] board) {
    int min = 0;
    int max = wordList.length-1;
    String wordLowerCase = word.toLowerCase();
    if (checkDict(wordList, wordLowerCase, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {
      return true;
    }
    return false;
  }
  
  /*
   * Method checks if the word is in the dictionary by using recursive binary search
   * if the word is in the dictionary, then method returns the index of the word; otherwise, it returns -1
   */
  public static int checkDict(String[] wordList, String word, int min, int max) {
    int middle = (max + min)/2;
    if (max < min) {
      return -1;
    }
    if (word.compareTo(wordList[middle])==0) {
      return middle;
    } else if (word.compareTo(wordList[middle]) > 0) {
      return checkDict(wordList, word, middle+1, max);
    } else if (word.compareTo(wordList[middle]) < 0) {
      return checkDict(wordList, word, min, middle-1);
    }
    return -1;
  }
  
  /*
   * Method returns true if word length is at least the lminimum word length. Otherwise,
   * it returns false.
   */ 
  public static boolean checkLength(String word, int wordLen) {
    if (word.length() >= wordLen) {
      return true;
    }
    return false;
  }
  
  public static boolean checkDuplicateWord(ArrayList<String> usedWords, String word) {
    if (usedWords.contains(word)) {
      return false;
    }    
    return true;
  }
  
  /*
   * Method checks if the word's letters are horizontally, vertically and diagonally adjacent to each other.
   * If the word is found, it returns true; otherwise it returns false
   */
  public static boolean checkAdjacent(String[][] board, String word) {
    word = word.toUpperCase();
    for (int i=0; i<board.length; i++) {
      for (int j=0; j<board[i].length; j++) {
        if (board[i][j].equals(Character.toString(word.charAt(0)))) {
          gridSearch(board, i, j, -1, -1, word, 0, word.length()-1);
        }
        if (found) {
          found = false;
          return true;
        }
      }
    }
    return false;
  }
  /*
   * @ROHAN
   */
  public static boolean indexValid(String[][] board, int row, int col, int prevRow, int prevCol) {
    int len = board.length;
    if ((row >= 0 && col >= 0 && row < len && col < len) && !(prevRow == row && prevCol == col)) {
      return true;
    } else {
      return false;
    }
  }
  /*
   *  @ROHAN
   */
  public static void gridSearch(String[][] board, int row, int col, int prevRow, int prevCol, String word, int index, int wordLen) {
    int[] x = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] y = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    if (index > wordLen || !board[row][col].equals(Character.toString(word.charAt(index)))) {
      return;
    }
    if (index == wordLen) {
      found = true;
      return;
    }
    for (int i=0; i < 8; i++) {
      if (indexValid(board, (row + x[i]), (col + y[i]), prevRow, prevCol)) {
        gridSearch(board, row + x[i], col + y[i], row, col, word, index+1, wordLen);
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
  /*
   public static void flipCoin () {
   int flipCoin = (int)(Math.random()*2);
   if(flipCoin == 0){
   System.out.println(p1 + " you go first!");
   }
   if(flipCoin == 1){
   System.out.println(p2 + " you go first!");
   String temp = p1;
   p1 = p2;
   p2 = temp;
   }
   
   }
   */
}
