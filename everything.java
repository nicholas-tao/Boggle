import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class Boggle {
  static boolean found = false;
  public static void main (String [] args) throws Exception{
    boolean gameRunning = true;
    Scanner input = new Scanner("wordList.txt");
    Scanner sc = new Scanner(System.in);
    ArrayList<String> wordArrayList = new ArrayList<String>();
    
    String [] wordList = wordArrayList.toArray(new String [wordArrayList.size()]); //convert arraylist to arraY
    
    final int BOARD_SIZE = 5;
    String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};
    int playerNumber, scoreLimit, minWordLen;
    while(input.hasNext()){
      wordArrayList.add(input.nextLine());
    }
    
    while(gameRunning){
      startMusic("sound.aiff");
      System.out.println("Do you want to have 1 player or 2 players?");
      playerNumber = sc.nextInt();
      System.out.println("Enter the score level you intend to play up to");
      scoreLimit = sc.nextInt();
      System.out.println("Enter the minimum word length you prefer");
      minWordLen = sc.nextInt();
      
      if (playerNumber==1) {
        ArrayList<String> wordsEntered = new ArrayList<String>();
        int score = 0;
        int timesPassed = 0;
        String board[][] = new String[BOARD_SIZE][BOARD_SIZE];
        randomizeBoard (board, die);
        
        while (gameRunning) {
          //music();
          if (timesPassed == 2) {
            randomizeBoard(board, die);
            wordsEntered.clear();
            timesPassed = 0;
          }
          printBoard(board);
          System.out.println("Would you like to:\n1. Pass\n 2.Continue\n3.Restart\4.Exit” [Enter the number]");
          int passRestartExit = sc.nextInt();
          
          if (passRestartExit == 1) {
            timesPassed++;
            continue; //go back to top of loop
          }else if (passRestartExit == 2) {
            System.out.println("Okay, get ready!");
          }else if (passRestartExit == 3) {
            //Continue from outer while loop
          }else if (passRestartExit == 4 ) {
            System.out.println("Thank you for playing”");
            gameRunning = false;
            //break from outer while loop
          }
          new Reminder (15);
          System.out.println("Timer started\nEnter 1 to pause");
          while (new Reminder(15).remaining > 0) {
            System.out.println("Enter any words you see");
            String word = sc.nextLine();
            if (word.equals("1")) {
              new Reminder(15).pause();
              System.out.println("Click ENTER to resume");
              //resume timer
            }
            if(validate(word, minWordLen, wordList, wordsEntered, board)){
              wordsEntered.add(word);
              score+= word.length();
            }
          }
          System.out.println("Times up!");
          
          if (score > scoreLimit) {
            System.out.println("User score: " +score +"\nCongratulations! You won!");
            System.out.println("“Do you want to play again? [Enter ‘y’ for yes or ‘n’ for no]");
            String continueGame = sc.nextLine();
            if (continueGame.equals("n")) {
              gameRunning = false;
              System.out.println("Thank you for playing");
            } else {
              //conitnue from outer while loop
            }
          }
        }
      }
      
      if(playerNumber == 2)
      {
        System.out.println("Player 1, please enter your name:");
        String p1 = sc.next();
        System.out.println("Player 2, please enter your name:");
        String p2 = sc.next();
        int score1 = 0;
        int score2 = 0;
        
        //flip a coin to see who goes first
        int flipCoin = (int)(Math.random()*2);
        if(flipCoin == 0){
          System.out.println(p1 + " you go first!");
        }
        if(flipCoin == 1){
          System.out.println(p1 + " you go first!");
          String temp = p1;
          p1 = p2;
          p2 = temp;
        }
        
        String board[][] = new String[BOARD_SIZE][BOARD_SIZE];
        ArrayList<String> wordsEnteredP1 = new ArrayList<String>();
        ArrayList<String> wordsEnteredP2 = new ArrayList<String>();
        int timesPassed1 = 0;
        int timesPassed2 = 0;
        randomizeBoard(board, die);
        
        while(gameRunning){
          //Start music
          if (timesPassed1 ==2 && timesPassed2 == 2) {
            randomizeBoard(board, die);
            wordsEnteredP1.clear();
            wordsEnteredP2.clear();
            timesPassed1 = 0;
            timesPassed2 = 0;
          }
          
          //print board
          printBoard(board);
          
          //P1
          System.out.println("“Player 1, do you want to pass? Enter ‘y’ for yes and ‘n’ for no”");
          String wantToPass1 = sc.next();
          if(wantToPass1.equals("y")){
            timesPassed1++;
          }
          if(wantToPass1.equals("n")){
            new Reminder(15);
            System.out.println("Time has started");
            System.out.println("Enter 1 to pause");
            while(new Reminder(15).remaining > 0){ //NEEDS WORK
              System.out.println("Please enter the words: ");
              String word = sc.next();
              if(word.equals("1")){
                //pause timer
                System.out.println("Resume game by clicking ENTER");
                //resume timer
              }
              if(validate(word, minWordLen, wordList, wordsEnteredP1, board)) {
                score1+=word.length();
                wordsEnteredP1.add(word);
              }
            }
            System.out.println("TIMES UP!");          
          }
          
          //P2
          System.out.println("“Player 2, do you want to pass? Enter ‘y’ for yes and ‘n’ for no”");
          String wantToPass2 = sc.next();
          if(wantToPass2.equals("y")){
            timesPassed2++;
          }
          if(wantToPass2.equals("n")){
            new Reminder(15);
            System.out.println("Time has started");
            System.out.println("Enter 1 to pause");
            while(new Reminder(15).remaining > 0){ //NEEDS WORK
              System.out.println("Please enter the words: ");
              String word = sc.next();
              if(word.equals("1")){
                new Reminder(15).pause();
                System.out.println("Resume game by clicking ENTER");
                //resume timer
              }
              if(validate(word, minWordLen, wordList, wordsEnteredP2, board)) {
                score2+=word.length();
                wordsEnteredP2.add(word);
              }
            }
            System.out.println("TIMES UP!");          
          }
          
          System.out.println(p1 + " :score: " + score1);
          System.out.println(p2 + " :score: " + score2);
          if(score1>score2 && score1 >= scoreLimit){
            System.out.println(p1+ " wins!");
          }else if(score2>score1 && score2 >= scoreLimit){
            System.out.println(p2+" wins!"); 
          }else if (score1 ==score2 && score1 >=scoreLimit){
            System.out.print("Tie game"); 
          }
          System.out.println("Do you want to restart or exit the game? [Enter ‘r’ for restart, or ‘e’ to exit]");
          String restartOrExit = sc.next();
          if(restartOrExit.equals("r")){
            //continue from outer while loop
          }else if(restartOrExit.equals("e")){
            //break from outer while loop
          }
          
          System.out.println("Do you want to play again?");
          String continueGame = sc.next();
          if(continueGame.equals("n")){
            gameRunning = false;
            System.out.println("Thanks for playing!");
          }else if(continueGame.equals("y")){
            //continue from outer while loop
          }
        }
      }
    }
  }
  
  public static void randomizeBoard (String [][] board, String [] die) {
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
  
  public static void printBoard (String [][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j] +" ");
      }
      System.out.println("");
    }
  }
  
  public static boolean validate(String word, int wordLen, String[] wordList, ArrayList<String> wordsEntered, String[][] board) {
    int min = 0;
    int max = wordList.length-1;
    
    if (checkDict(wordList, word, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {
      return true;
    }
    return false;
  }
  
  public static int checkDict(String[] wordList, String word, int min, int max) {
    int middle = (max + min)/2;
    
    if (word.compareTo(wordList[middle]) > 0) {
      return checkDict(wordList, word, middle+1, max);
    } else if (word.compareTo(wordList[middle]) < 0) {
      return checkDict(wordList, word, min, middle-1);
    } else if (word.compareTo(wordList[middle])==0) {
      return middle;
    }
    return -1;
  }
  
  public static boolean checkLength(String word, int wordLen) {
    if (word.length()>=wordLen) {
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
    
    for (int i=0; i<8; i++) {
      if (indexValid(board, (row + x[i]), (col + y[i]), prevRow, prevCol)) {
        gridSearch(board, row + x[i], col + y[i], row, col, word, index+1, wordLen);
      }
    }
  }
  
  public static void startMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    AudioInputStream audioInputStream;
    audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile()); 
    
    Clip clip = AudioSystem.getClip(); 
    
    clip.open(audioInputStream);
    clip.loop(Clip.LOOP_CONTINUOUSLY); 
    clip.start();
  }
  }
