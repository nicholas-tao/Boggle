/*
 * Names: Rohan Ravindran, Kevin Xu, Mandana Emam, Nicholas Tao
 * Date: june 12, 2019
 * Assignment Title: Boggle
 * 
 */ 

//import needed libraries
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class frame extends JFrame implements ActionListener { 
  
  /*
   * TO DO:
   *  1 Player 
   *    - fix timer
   *     - when timer goes off, game ends
   *     - check timer is > 0 when player reaches score, then they win, else lose
   *  2 Player
   *    - fix timer
   *     - timer 15 seconds
   *     - when timer up, switch player turn
   *     - if player pass --> reset timer, switch player turn
   *     - keep going until 1 player reaches the desired score
   * EXTRAS
   * - implement welcome screen, and score board and analysis after each game
   * - Score Board & End Game Screen
   * */
  static ArrayList<ArrayList<String>> wordsEntered = new ArrayList<ArrayList<String>>();
  static String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"}; //array storing the 25 die
  
  static String [] wordList;
  static int scoreLimit;
  static String board[][];
  static int minWordLen;
  static boolean found = false;
  static boolean gameRunning = true;
  static Scanner sc = new Scanner(System.in); 
  static long remaining = 15000000000L;  
  static JLabel[][] boardLabelGrid = new JLabel[5][5];
  static int playerNum;
  static String[] name;
  static int playerTurn = 0;
  static int[] playerScore;
  static int passCounter = 0;
  static Font font2 = new Font ("Sans Serif", Font.BOLD, 20);
  
  static JPanel pan3 = new JPanel();
  JPanel pan1 = new JPanel();  
  JPanel pan4 = new JPanel(); 
  JPanel infoPanel = new JPanel();
  JPanel bottomButtons = new JPanel();
  
  JPanel timeRemainingPanel = new JPanel();
  JPanel scorePanel = new JPanel();
  
  JLabel timeRemainingTitle = new JLabel("Time Remaining: ", JLabel.CENTER);
  JLabel timeRemaining = new JLabel("0", JLabel.CENTER);
  
  JLabel scoreTitle = new JLabel("Your Score: ", JLabel.CENTER);
  JLabel score = new JLabel("0", JLabel.CENTER);
  
  //other components
  JButton enterButton = new JButton("Enter");
  JLabel introLabel = new JLabel("Welcome To Boggle", SwingConstants.CENTER); //labels
  JTextField enterField = new JTextField ("", 30);// blank text field
  Font font = new Font("Sans Serif", Font.BOLD, 20);

  static Border labelBorder = BorderFactory.createEtchedBorder();
  Border upperBorder = BorderFactory.createDashedBorder(Color.BLUE, 4, 3);  
  
  //bottom buttons
  JButton shakeBoard = new JButton("Randomize Board!");
  JButton restartGame = new JButton("Restart"); //switch text to play when game state is paused
  JButton exitGame = new JButton("Exit");
  JButton pass = new JButton("Pass");
  
  JPanel playerTurnPanel = new JPanel();
  JLabel playerTurnTitle = new JLabel("Player Turn:", JLabel.CENTER);
  JLabel playerTurnLabel = new JLabel("", JLabel.CENTER);
  
  public void setupQuestions() {
    Object[] minLengthValues = { "2", "3", "4" }; //getting the min length of a word
    Object selectedValue2 = JOptionPane.showInputDialog(null,
                                                        "Please choose a minimum word length", "Input",
                                                        JOptionPane.INFORMATION_MESSAGE, null,
                                                        minLengthValues, minLengthValues[0]);
    minWordLen = Integer.parseInt(selectedValue2.toString());
    
    scoreLimit = 0;
    do {
      String scoreLimitInput = JOptionPane.showInputDialog("Please choose a score limit");//getting the score limit
      
      try {
        scoreLimit = Integer.valueOf(scoreLimitInput); 
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "You have to an integer value", "alert", JOptionPane.ERROR_MESSAGE);
        continue;
      }
      
      if (scoreLimit < 1) {
        JOptionPane.showMessageDialog(null, "You have to enter a value bigger than 1", "alert", JOptionPane.ERROR_MESSAGE);
      }
      
    } while(scoreLimit < 1);
  }
  
  public frame() {
    //Intro Questions
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
    JOptionPane.showMessageDialog(null, gameRules, "Boggle Rules", JOptionPane.INFORMATION_MESSAGE);
    Object[] possibleValues = {"One", "Two"};
    Object selectedValue = JOptionPane.showInputDialog(null,
                                                       "How many players are there?", "Input",
                                                       JOptionPane.INFORMATION_MESSAGE, null,
                                                       possibleValues, possibleValues[0]);   
    
    if(selectedValue.equals("One")) playerNum=1;
    else playerNum=2;
    
    name = new String[playerNum];
    
    setupQuestions();
    
    for(int i=0; i<playerNum; i++) { //getting the name of players
      name[i] = JOptionPane.showInputDialog(null, "Enter name of Player "+(i+1)+".", "Enter name");
    }
    
    
    //MARK: Frame Setup
    setTitle("Boggle");
    setSize(800, 500);
    
    pan1.setLayout(new BoxLayout(pan1, BoxLayout.PAGE_AXIS));
    pan3.setLayout(new GridLayout(5,5));
    pan4.setLayout(new FlowLayout());
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
    exitGame.setPreferredSize(new Dimension(165, 25));
    
    bottomButtons.add(restartGame);
    restartGame.addActionListener(this);
    
    bottomButtons.add(exitGame);
    exitGame.addActionListener(this);
    
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
      
    } else {
      timeRemainingPanel.setPreferredSize(new Dimension(390, 65));
      scorePanel.setPreferredSize(new Dimension(390, 65));
      shakeBoard.setPreferredSize(new Dimension(165, 25));
      shakeBoard.addActionListener(this); //have to add action listeners for each button
      
      bottomButtons.add(shakeBoard);
      playerScore = new int[1];
    }
    
    infoPanel.add(timeRemainingPanel);
    infoPanel.add(scorePanel);
    
    pan4.add(enterField);
    pan4.add(enterButton);
    
    pan3.setPreferredSize(new Dimension(800, 200));
    
    //adding panels to pan1
    pan1.add(infoPanel);
    pan1.add(pan3);
    pan1.add(pan4);
    pan1.add(bottomButtons); //adding panels
    
    enterButton.addActionListener(this);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    add(pan1);
    setVisible(true); 
    setLocationRelativeTo(null); //centering the frame
  }
  
  public void resartGame() {
    setupQuestions();
    if (playerNum == 1) {
      playerScore[0] = 0;
      score.setText(Integer.toString(playerScore[0]));
      //reset timer
    } else {
      playerScore[0] = 0;
      playerScore[1] = 0;
      playerTurn = 0;
      passCounter = 0;
      score.setText(Integer.toString(playerScore[playerTurn]));
      playerTurnLabel.setText(name[playerTurn]);
      //reset timer
    }
    randomizeBoard(board);
    updateBoard(board);
    
    //reset timer
  }
  
  public void actionPerformed(ActionEvent event) { //GUI ACTION RESPONSES
    if (event.getSource() == enterButton) { //Pressed Enter Button
      String word = enterField.getText();
      ArrayList<String> wordsUsed;
      
      if (validate(word, minWordLen, wordList, wordsEntered.get(playerTurn), board)) {
        playerScore[playerTurn] += word.length();
        score.setText(Integer.toString(playerScore[playerTurn]));
        wordsEntered.get(playerTurn).add(word);
        enterField.setText("");
        
        if (playerScore[playerTurn] >= scoreLimit) {
          Object[] resartGameValues = {"Yes", "No"};
          Object selectedValue = JOptionPane.showInputDialog(null,
                                                             "Congrats, " + name[playerTurn] +". You have finished the game by reaching " + scoreLimit + " points! \nWould you like to play again?", "Game Finished!",
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
          //do have to check for time, to make sure they got the number of points in the desired time?
        }
      }
    } else if (event.getSource() == shakeBoard) { //Pressed Randomize Board Button
      randomizeBoard(board);
      updateBoard(board);
      System.out.println("Random Board");
    } else if (event.getSource() == exitGame) { //Pressed Exit Game Button
      String exitMessage = "";
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
          //show analysis
          System.exit(0);
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      
    } else if (event.getSource() == restartGame) {
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
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    } else if (event.getSource() == pass) {
      passCounter++;
      if (playerTurn == 0) playerTurn = 1;
      else playerTurn = 0;
      score.setText(Integer.toString(playerScore[playerTurn]));
      if (passCounter % 4 == 0) { //both players have to pass twice
        randomizeBoard(board);
        updateBoard(board);
        playerTurnLabel.setText(name[playerTurn]);
      } else {
        playerTurnLabel.setText(name[playerTurn]);
      }
    }
  }
  
  public static void main(String[] args) throws Exception {
    frame frame1 =new frame(); 
    
    wordsEntered.add(new ArrayList<String>());
    
    if (playerNum == 2) wordsEntered.add(new ArrayList<String>());
    
    
    final int BOARD_SIZE = 5; //board size is 5
    board = new String[BOARD_SIZE][BOARD_SIZE]; //declare a 2D array for the board
    wordList = readFromFile(); //wordList stores dictionary words by calling the method
    
    randomizeBoard(board);
    updateBoard(board);
  }
  
  public static void updateBoard(String[][] board) {
    pan3.removeAll();
    for (int i=0; i<boardLabelGrid.length; i++) {
      for (int j=0; j<boardLabelGrid.length; j++) {
        boardLabelGrid[i][j] = new JLabel(board[i][j], JLabel.CENTER);
        boardLabelGrid[i][j].setBorder(labelBorder);
       boardLabelGrid[i][j].setFont(font2);
        pan3.add(boardLabelGrid[i][j]);
      }
    }
    pan3.revalidate();
    pan3.repaint();
  }
  
  public static boolean validate(String word, int wordLen, String[] wordList, ArrayList<String> wordsEntered, String[][] board) {
    int min = 0;
    int max = wordList.length-1;
    String wordLowerCase = word.toLowerCase();
    if (checkDict(wordList, wordLowerCase, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {
      return true;
    }
    return false;
  }
  
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
  
  public static boolean indexValid(String[][] board, int row, int col, int prevRow, int prevCol) {
    int len = board.length;
    if ((row >= 0 && col >= 0 && row < len && col < len) && !(prevRow == row && prevCol == col)) {
      return true;
    } else {
      return false;
    }
  }
  
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
  
  public static void randomizeBoard(String [][] board) {
    ArrayList <Integer> ranNums = new ArrayList <Integer>(); 
    for (int i = 0; i < die.length; i++) {
      ranNums.add(i); //initialize arraylist with values from 0 to die.length-1 (inclusive)
    }
    Collections.shuffle(ranNums); //randomize order of valyes
    
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
  
  public static String [] readFromFile()  throws Exception  {
    Scanner readFile = new Scanner(new File("wordlist.txt"),"UTF-8"); //declare scanner to read text file
    ArrayList<String> wordArrayList = new ArrayList<String>(); //arraylist to store words from file
    while(readFile.hasNext()) {
      wordArrayList.add(readFile.nextLine());
    }
    readFile.close();
    return wordArrayList.toArray(new String [wordArrayList.size()]); //convert arraylist to array
    
  } 
}
