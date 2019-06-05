/*
 * Names: Nicholas Tao, Kevin Xu, Mandana Emam, Rohan Ravindran
 * Date: June 12, 2019
 * Assignment Title: Boggle
 */ 

import java.util.*;
import java.io.*;
public class Boggle {
  static boolean found = false;
  public static void main (String [] args) throws Exception {
    final int BOARD_SIZE = 5;
    boolean gameRunning = true;
    Scanner readFile= new Scanner ("wordlist.txt");
    Scanner sc = new Scanner(System.in);
    ArrayList<String> wordArrayList = new ArrayList<String>();
    String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};
    int playerNumber, scoreLimit, minWordLen;
    
    while (readFile.hasNext()) {
      wordArrayList.add(readFile.nextLine());
    }
    
    String [] wordList = wordArrayList.toArray(new String [wordArrayList.size()]); //convert arraylist to array
    
    while (gameRunning) {
      System.out.println("Do you want to have 1 or 2 players?");
      playerNumber = sc.nextInt();
      
      System.out.println("Enter the score level you intend to play up to");
      scoreLimit = sc.nextInt();
      
      System.out.println("Enter the minimum word length");
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
          
          
        }
      }
      
    }
    
    
  }
  
  public static void randomizeBoard (String [][] board, String [] die) {
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
  
  public static void printBoard (String [][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j] +" ");
      }
      System.out.println("");
    }
  }
}

