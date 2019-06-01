/*
 * Names: Nicholas Tao, Kevin Xu, Mandana Emam, Rohan Ravindran
 * Date: June 12, 2019
 * Assignment Title: Boggle
 */ 

import java.util.*;
public class Boggle {
  public static void main (String [] args) {
    String board[][] = makeBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        System.out.print(board[i][j]);
      }
      System.out.println("");
    }
  }
  public static String[][] makeBoard() {
    ArrayList<String> dice = new ArrayList <String>();
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
    String [][] board = new String [5][5];
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
    return board;
  }
}
