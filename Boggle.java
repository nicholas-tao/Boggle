/*
 * Names: Nicholas Tao, Kevin Xu, Mandana Emam, Rohan Ravindran
 * Date: June 12, 2019
 * Assignment Title: Boggle
 */ 

import java.util.*;
public class Boggle {
  public static void main (String [] args) {
    final int BOARD_SIZE = 5;
    String board[][] = new String[BOARD_SIZE][BOARD_SIZE];
    String [] die = {"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};
    
    randomizeBoard(board, die);
    
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j]);
      }
      System.out.println("");
    }
  }/*
   public static void randomizeBoard(String [][] board) {
   dice.add("AAAFRS");
   dice.add("AAEEEE");
   dice.add("AAFIRS");
   dice.add("ADENNN");
   dice.add("AEEEEM");
   dice.add( "AEEGMU");
   dice.add("AEGMNN");
   dice.add( "AFIRSY");
   dice.add( "BJKQXZ");dice.add( "CCNSTW");dice.add( "CEIILT"); dice.add( "CEILPT");dice.add( "CEIPST");dice.add( "DDLNOR");dice.add( "DHHLOR");dice.add( "DHHNOT");dice.add( "DHLNOR");dice.add( "EIIITT");dice.add( "EMOTTT");dice.add( "ENSSSU");
   dice.add( "FIPRSY");dice.add( "GORRVW");dice.add( "HIPRRY");dice.add( "NOOTUW");dice.add( "OOOTTU");
   //"ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU");
   for (int i = 0; i < board.length; i++) {
   for (int j = 0; j < board[i].length; j++) {
   //generate ran num from 0-24 and another from 0-5
   //int face = (int) Math.random()*dice.get(j).length(); //generate random number from 0 to number of sides on one dice
   int face = (int) Math.random()*6;
   int randIndex = (int) Math.random()*dice.size(); //generate random number from 0 to number of die
   
   
   board[i][j] = Character.toString(dice.get(randIndex).charAt(face)); //add randomly generated letter to array
   dice.remove(randIndex); //remove the used die fom dice to prevent duplicates
   }
   }
   */
  
  public static void randomizeBoard (String [][] board, String [] die) {
    ArrayList <Integer> ranNums = new ArrayList <Integer>(); 
    /*for (int i = 0; i < die.length; i++) {
     int temp = (int)(Math.random() * die.length);
     boolean unique = true;  //number is initially unique
     
     for (int j = 0; j < ranNums.length;j++) {
     if (temp == ranNums[j]) { //if number is already generated, it is not unique
     unique = false;
     break;
     }
     }
     
     if (unique == true) {
     ranNums[i] = temp; //number is stored in array if unique
     } else if (unique==false) {
     i--; //reduce counter by one
     continue; //go back to top of loop
     }
     }
     */
    
    for (int i = 0; i < die.length; i++) {
      ranNums.add(i);
    }
    Collections.shuffle(ranNums);
    
    int count = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = die[ranNums.get(count)];
        int randIndex = (int)(Math.random() * (board[i][j]).length());
        board[i][j] = Character.toString(board[i][j].charAt(randIndex));
        count++;
      }
    }
    
  }
  
  
  
  
}

