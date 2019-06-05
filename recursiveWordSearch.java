import java.util.*;

/*
import statements for timer and media player

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
*/
import java.util.*;
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 
  
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 


public class recursiveGridSearch {
	
	
	static boolean found = false;
	
	public static void main(String args[]) {
		String[][] board = {{"B", "A", "D", "E", "L"}, 
							{"Q", "D", "W", "P", "O"}, 
							{"F", "I", "F", "N", "T"}, 
							{"L", "O", "F", "A", "S"}, 
							{"V", "H", "K", "S", "N"}};

		String word = "ADEL";
		
		System.out.println(checkAdjacent(board, word));
		
		
	}
	
	public static boolean validate(String word, int wordLen, String[] wordlist, ArrayList<String> wordsEntered, String[][] board) {
		int min = 0;
		int max = wordlist.length-1;
		//int posistion  = checkDict(wordlist, word, min, max);
		
		if (checkDict(wordlist, word, min, max) > -1 && checkLength(word, wordLen) && checkAdjacent(board, word) && checkDuplicateWord(wordsEntered, word)) {
			return true;
		}
		
		return false;
	}
	
	public static int checkDict(String[] wordlist, String word, int min, int max) {
		int middle = (max + min)/2;
		
		if (word.compareTo(wordlist[middle]) > 0) {
			return checkDict(wordlist, word, middle+1, max);
		} else if (word.compareTo(wordlist[middle]) < 0) {
			return checkDict(wordlist, word, min, middle-1);
		} else if (word.compareTo(wordlist[middle])==0) {
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
	
	public static void startMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		 
        // create AudioInputStream object 
		AudioInputStream audioInputStream;
		audioInputStream = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile()); 
				                
        // create clip reference 
        Clip clip = AudioSystem.getClip(); 
          
        // open audioInputStream to the clip 
        clip.open(audioInputStream);
          
        clip.loop(Clip.LOOP_CONTINUOUSLY); 
        clip.start();
	}
	
	public static boolean checkAdjacent(String[][] board, String word) {
		for (int i=0; i<board.length; i++) {
			for (int j=0; j<board[i].length; j++) {
				if (board[i][j].equals(Character.toString(word.charAt(0)))) {
					gridSearch(board, i, j, -1, -1, word, 0, word.length()-1);
				}
				
				if (found) {
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
	
}
