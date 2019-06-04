import java.util.*;

/*
import statements for timer and media player

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
*/

public class testMethod {
	public static void main(String args[]) {
		
		String[][] input = {{"B", "A", "D", "E", "L"}, 
							{"Q", "D", "W", "P", "O"}, 
							{"S", "F", "D", "N", "T"}, 
							{"L", "O", "F", "A", "S"}, 
							{"V", "H", "K", "S", "N"}};
		
		String word = "SFS"; 
		System.out.println(checkAdjacent(input, word));
		
	}
		
	public static boolean checkAdjacent(String input[][], String word) {
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		for (int i=0; i<input.length; i++) { //check ancestor
			for (int j=0; j<input[i].length; j++) {
				if (input[i][j].equals(Character.toString(word.charAt(0)))) {
					System.out.println("Starting Coordinates:" + i + ", " +j);
					coordinates.add(i);
					coordinates.add(j);
				}
			}
		}
		
		if (coordinates.size() == 0) return false;
		
		int counter = 0;
		for (int i=0; i<coordinates.size()/2; i++) {
			int[] coordinatesArray = {coordinates.get(counter), coordinates.get(counter+1)};
			if (getAdjacent(input, word, coordinatesArray)) {
				return true;
			}
			counter += 2;
		}
		return false;
	}
	
	public static boolean getAdjacent(String[][] input, String word, int[] startingCoordinates) {
		boolean[][] visited = new boolean[5][5];
		visited[startingCoordinates[0]][startingCoordinates[1]] = true;
		int[] row = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] column = {-1, 0, 1, -1, 1, -1, 0, 1}; 
		boolean charFound = true;
		int charCounter = 0;
		
		if (word.length() == 1) {
			return true;
		}
		
		for (int j=1; j<word.length(); j++) {
			String currentLetter = Character.toString(word.charAt(j));
			if (charFound) {
				charFound = false;
				for (int i=0; i<8; i++) {
					int xPos = startingCoordinates[0] + row[i];
					int yPos = startingCoordinates[1] + column[i];
					
					if (xPos < 0 || yPos < 0 || xPos >= input.length || yPos >= input.length) {
						continue;
					} else {
						
						if (input[xPos][yPos].equals(currentLetter) && visited[xPos][yPos] != true) {
							visited[xPos][yPos] = true;
							System.out.println(currentLetter);
							startingCoordinates[0] = xPos;
							startingCoordinates[1] = yPos;
							charFound = true;
							break;
						}
					}
				}
			} else {
				break;
			}
			charCounter++;
		}
		
		if (charCounter == word.length()-1 && charFound == true) {
			return true;
		} else {
			return false;
		}
	}
}
