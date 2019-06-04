import java.util.*;

/*
import statements for timer and media player

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
*/

public class recursiveWordSearch {
	static boolean found = false;
	
	public static void main(String args[]) {
		String[][] board = {{"B", "A", "D", "E", "L"}, 
							{"Q", "D", "W", "P", "O"}, 
							{"F", "F", "F", "N", "T"}, 
							{"L", "O", "F", "A", "S"}, 
							{"V", "H", "K", "S", "N"}};

		String word = "VOA"; 
		
		System.out.println(checkAdjacent(board, word));
				
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
