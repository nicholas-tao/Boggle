//USE THIS FILE!!!
/*
 * Names: Rohan Ravindran, Kevin Xu, Mandana Emam, Nicholas Tao
 * Date: june 12, 2019
 * Assignment Title: Boggle
 */ 

//import needed libraries
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

public class BoggleAttemptToOptimize {
  static boolean found = false; //boolean variable to indicate whether a word was found on the board
  static boolean gameRunning = true; //boolean variable to indicate whether the game is running or not
  static Scanner sc = new Scanner(System.in); //declare scanner to read keyboard input
  static long remaining = 15000000000L; //equivalent to 15s, the time each player has for their turn
  public static void main (String [] args)  throws Exception  {
    
    final int BOARD_SIZE = 5; //board size is 5
    String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"}; //array storing the 25 die
    int playerNumber, scoreToWin; //variables to determine number of players and score to win
    int minWordLen = 3; //default value for wordLength
    String board[][] = new String[BOARD_SIZE][BOARD_SIZE]; //declare a 2D array for the board
    String [] wordList = readFromFile(); //wordList stores dictionary words by calling the method
    System.out.println("Welcome to Boggle, a game where you enter words that you see on a randomized board.\nThe letters must be horizontally, vertically, or diagonally adjcanet.\nThe words must be unique (Cannot enter same word twice), as well as in the English dictionary.\nEach letter in a valid word is one point and your objective is to reach a certain score first!\nLet's get started!\n"); //welcome and rules
    
    //main loop for whole game
    while(gameRunning){
      //startMusic("sound.aiff"); //start music
      System.out.println("Do you have 1 player or 2 players?");
      playerNumber = sc.nextInt(); //store number of players
      System.out.println("Enter the score you intend to play up to");
      scoreToWin = sc.nextInt(); //store score needed to win
      System.out.println("Enter the minimum word length you prefer");
      minWordLen = sc.nextInt(); //score minimum word length
      
      //1 Player Mode
      if (playerNumber==1) {
        if(onePlayer(scoreToWin, minWordLen, board, die, wordList)) {
          continue; //if the method returns true, the user wants to restart or play again
        }
      } else if(playerNumber == 2){
        sc.nextLine(); //clear scanner
        if (twoPlayer(scoreToWin, minWordLen, board, die, wordList)) {
          continue;//if the method returns true, the user wants to restart or play again
        }
      }
    }
  }
  
  /*
   * Method randomizes the board of letters by choosing a random (no duplicates) die to place in each positon. Then,
   * it chooses a random letter from each die. The 25 letters chosen make up the board
   */ 
  public static void randomizeBoard (String [][] board, String [] die) {
    ArrayList <Integer> ranNums = new ArrayList <Integer>(); //arraylist to store random numbers
    for (int i = 0; i < die.length; i++) {
      ranNums.add(i); //initialize arraylist with values from 0 to die.length-1 (inclusive)
    }
    Collections.shuffle(ranNums); //randomize order of values
    
    int count = 0; //keeps track of which element in ranNums to access
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
   * Method prints the elements of the 2D board array
   */ 
  public static void printBoard (String [][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j] +" "); //prints element at index [i][j]
      }
      System.out.println("");
    }
  }
  /*
   * Method checks if the user-entered word is valid by calling the methods that check certain criteria in order for the word to be valid.
   * If all methods indicate that the word is valid, the method returns true. If they do not all indicate this, the method returns false.
   */ 
  public static boolean validate(String word, int wordLen, String[] wordList, ArrayList<String> wordsEntered, String[][] board) {
    int min = 0;
    int max = wordList.length-1;
    String wordLowerCase = word.toLowerCase();
    if (checkDict(wordList, wordLowerCase, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {
      return true;  //if all of the check methods return true, then word is valid and validate returns true
    }
    return false;  //otherwise it returns false
  }
  
  /*
   * Method uses recursive binary search to search for the user's word in an array of words in the dictionary
   * It returns the index of the word if it is found, or -1 if it is not.
   */ 
  public static int checkDict(String[] wordList, String word, int min, int max) {
    int middle = (max + min)/2;
    if (max < min) {
      return -1;
    }
    if (word.compareTo(wordList[middle])==0) {  
      return middle;  //if word is equal to the middle, the method returns middle position
    } else if (word.compareTo(wordList[middle]) > 0) {
      return checkDict(wordList, word, middle+1, max);  //if word is alphabetically after the middle, the method recursively calls and sets the min to mid-1
    } else if (word.compareTo(wordList[middle]) < 0) {
      return checkDict(wordList, word, min, middle-1);  //if word is alphabetically before the middle, the method recursively calls and sets the max to mid-1
    }
    return -1; //if not found, return -1
  }
  
  /*
   * Method checks whether the length of the word is greater than or equal to the minimum word length and returns true if it is.
   * If the word is not of correct length, it returns false.
   */ 
  public static boolean checkLength(String word, int wordLen) {
    if (word.length()>=wordLen) {
      return true;  //if the length of the word user enters is less than the initial length, then checkLength returns true
    }
    return false; //otherwise it returns false
  }
  
  /*
   * Method checks if the word has been used before by seeing if the arraylist containing words entered with the specific board, contains the 
   * entered word. If the word has been entered, it returns false, otherwise, it returns true.
   */ 
  public static boolean checkDuplicateWord(ArrayList<String> usedWords, String word) {
    if (usedWords.contains(word)) {
      return false;  //if the usedWords arraylist contains the word user entered, then method returns false
    }    
    return true;  //otherwise the user returns true
  }
  
  /*
   * Method checks if the word's letters are horizontally, vertically and diagonally adjacent to each other
   * If the word is found, it returns true; otherwise it returns false
   */
  public static boolean checkAdjacent(String[][] board, String word) {
    word = word.toUpperCase(); //change the entered word to upper case
    for (int i=0; i<board.length; i++) {
      for (int j=0; j<board[i].length; j++) {
        if (board[i][j].equals(Character.toString(word.charAt(0)))) {
          gridSearch(board, i, j, -1, -1, word, 0, word.length()-1); //call gridSearch to recursively search the board
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
   * Method checks if the index of the letter in gridSearch is valid on the board
   * If the index is valid, it returns true; Otherwise, it returns false
   */
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
   * Method reads from a text file and adds the words from the file to an arraylist. The method returns the arraylist with
   * all the words, but as an array.
   */ 
  public static String [] readFromFile ()  throws Exception  {
    Scanner readFile = new Scanner(new File("wordlist.txt"),"UTF-8"); //declare scanner to read text file
    ArrayList<String> wordArrayList = new ArrayList<String>(); //arraylist to store words from file
    while(readFile.hasNext()){
      wordArrayList.add(readFile.nextLine());
    }
    return wordArrayList.toArray(new String [wordArrayList.size()]); //convert arraylist to array
  }
  
  /*
   * Method runs the main Boggle game for one player.
   */ 
  public static boolean onePlayer (int scoreToWin, int minWordLen, String[][]board, String [] die, String [] wordList) {
    ArrayList<String> wordsEntered = new ArrayList<String>(); //arraylist to store words user enters
    int score = 0; //initial score is 0
    System.out.println("Here is the board");
    randomizeBoard (board, die); //randomize the board
    inner1:
      while (gameRunning) {
      printBoard(board); //print the board
      System.out.println("Would you like to:\n1.Randomize Board\n2.Continue\n3.Restart\n4.Exit[Enter the number]");
      int randContRestartExit = sc.nextInt();
      
      if (randContRestartExit == 1) {
        randomizeBoard(board, die); //randomize the board
        wordsEntered.clear(); //clear the arraylist storing words user entered
        System.out.println("Board has been randomized!");
        printBoard(board); //print new board
      }else if (randContRestartExit == 2) {
        System.out.println("Okay, get ready!");
      }else if (randContRestartExit == 3) {
        return true; //starts the game from the very beginning
      }else if (randContRestartExit == 4 ) {
        System.out.println("Thank you for playing!");
        gameRunning = false;
        return false; //breaks from loop
      } 
      sc.nextLine();//clear scanner
      System.out.println("Timer started");
      long startTime = System.nanoTime(); //current time as startTime
      long stopTime = 15000000000L + startTime; //initialize stopTime which is 15 seconds after current time
      while (remaining > 0) {  //while there is time remaining, user is prompted to enter words
        long currentTime = System.nanoTime();
        if (currentTime >= stopTime) {
          //textField.setVisble(false);
          System.out.println("TIMES UP!");
          remaining = 15000000000L;
          break;
        }
        System.out.println("Enter any words you see");
        String word = sc.nextLine();
        //validate the word as they are entered
        if(validate(word, minWordLen, wordList, wordsEntered, board)){ 
          wordsEntered.add(word);
          score+= word.length();
          System.out.println("VALID WORD ENTERED\nCurrent score: " +score);
        } else {
          System.out.println("INVALID WORD");
        }
        //if user's score exceeds the score goal, user wins and is asked if they want to play again
        if (score >= scoreToWin) {
          System.out.println("Congratulations! You won!");
          System.out.println("Do you want to play again? [Enter ‘y’ for yes or ‘n’ for no]");
          String continueGame = sc.nextLine();
          if (continueGame.equals("n")) {
            gameRunning = false;
            System.out.println("Thank you for playing!");
            remaining = 0L;
          } else {
            return true;
          }
        }
      }        
    }
      return false;
  }
  /*
   * Method runs the main Boggle game for two players.
   */
  public static boolean twoPlayer (int scoreToWin, int minWordLen, String[][]board, String [] die, String [] wordList) {
    System.out.println("Player 1, please enter your name:");
    String p1 = sc.nextLine();
    System.out.println("Player 2, please enter your name:");
    String p2 = sc.nextLine();
    int score1 = 0;
    int score2 = 0;
    
    //flip a coin to see who goes first
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
    
    ArrayList<String> wordsEntered2P = new ArrayList<String>();  //declare a new arraylist to store all words users entered
    int timesPassed1 = 0; //record number of times user1 passed
    int timesPassed2 = 0; //record number of times user2 passed
    randomizeBoard(board, die);
    boolean p1Wins = false;
    outer2:
      while(gameRunning){
      if (timesPassed1 >=2 && timesPassed2 >= 2) { //if both players pass twice, randomize board and clear wordEntered2P
        System.out.println("Board has been randomized!");
        randomizeBoard(board, die);
        wordsEntered2P.clear();
        timesPassed1 = 0;
        timesPassed2 = 0;
      }
      
      //print board
      printBoard(board);
      
      //P1
      System.out.println(p1+", do you want to pass? Enter ‘y’ for yes and ‘n’ for no");
      String wantToPass1 = sc.nextLine();
      if(wantToPass1.equals("y")){
        timesPassed1++;
      }
      if(wantToPass1.equals("n")){
        System.out.println("Timer has started");
        long startTime1 = System.nanoTime();
        long stopTime1 = 15000000000L + startTime1; 
        while (remaining > 0) {
          long currentTime1 = System.nanoTime();
          if (currentTime1 >= stopTime1) {
            //textField.setVisble(false);
            System.out.println("TIMES UP!");
            remaining = 15000000000L;
            break;
          }
          System.out.println("Enter any words you see");
          String word = sc.nextLine();
          
          if(validate(word, minWordLen, wordList, wordsEntered2P, board)) {
            score1+=word.length();
            wordsEntered2P.add(word);
            System.out.println("VALID WORD ENTERED\nCurrent score: " +score1);
            if(score1 > score2 && score1 >= scoreToWin){
              p1Wins = true;
              break;
            } 
          } else {
            System.out.println("INVALID WORD"); 
          } 
        }
      } 
      
      //P2
      if (!p1Wins){
        System.out.println(p2+", do you want to pass? Enter ‘y’ for yes and ‘n’ for no");
        String wantToPass2 = sc.nextLine();
        if(wantToPass2.equals("y")){
          timesPassed2++;
        }
        if(wantToPass2.equals("n")){
          System.out.println("Timer has started");
          long startTime2 = System.nanoTime();
          long stopTime2 = 15000000000L + startTime2; 
          while (remaining > 0) {
            long currentTime2 = System.nanoTime();
            if (currentTime2 >= stopTime2) {
              //textField.setVisble(false);
              System.out.println("TIMES UP!");
              remaining = 15000000000L;
              break;
            }
            System.out.println("Enter any words you see");
            String word = sc.nextLine();
            
            if(validate(word, minWordLen, wordList, wordsEntered2P, board)) {
              score2+=word.length();
              wordsEntered2P.add(word);
              System.out.println("VALID WORD ENTERED\nCurrent score: " +score2);
              if(score2>score1 && score2 >= scoreToWin){
                break;
              }
            } else {
              System.out.println("INVALID WORD");
            }
          }       
        }
      }
      
      //display final results
      System.out.println(p1 + "'s score: " + score1);
      System.out.println(p2 + "'s score: " + score2);
      if(score1>score2 && score1 >= scoreToWin){
        System.out.println(p1+ " wins!");
      } else if(score2>score1 && score2 >= scoreToWin){
        System.out.println(p2+" wins!"); 
      } else if (score1 ==score2 && score1 >=scoreToWin){
        System.out.print("Tie game"); 
      } else {
        System.out.println("Would you like to:\n1.Continue\n2.Restart\n3.Exit[Enter the number]");
        int contRestartExit = sc.nextInt();
        sc.nextLine(); //clear scanner
        if(contRestartExit == 1) {
          continue;
        } else if(contRestartExit == 2){
          return true;
        } else if (contRestartExit == 3) {
          System.out.println("Thank you for playing!");
          return false;
        }
      }
      //Prompt users if they want to play again
      System.out.println("Do you want to play again? [Enter 'y' for yes and 'n' for no]");
      String continueGame = sc.nextLine();
      if(continueGame.equals("n")){
        gameRunning = false;
        System.out.println("Thanks for playing!");
      }else if(continueGame.equals("y")){
        return true;
      }
    }  
      return false;
  }
}
