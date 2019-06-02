import java.util.*;

public class adjacentMethodTest {
	public static void main(String args[]) {
		
		String[][] input = {{"B", "A", "D", "E", "L"}, 
							{"Q", "D", "W", "P", "O"}, 
							{"X", "I", "C", "N", "T"}, 
							{"L", "O", "F", "A", "S"}, 
							{"V", "H", "K", "S", "N"}};
		
		String word = "ADIOH";
		System.out.println(checkAdjacent(input, word));
		
	}
		
	public static boolean checkAdjacent(String input[][], String word) {
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		for (int i=0; i<input.length; i++) {
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
		int[] parentCoordinates = startingCoordinates;
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
						System.out.println(xPos + ", " + yPos);
						String newPos = xPos + " " + yPos;
						String parentPos = parentCoordinates[0] + " " + parentCoordinates[1];
						if (input[xPos][yPos].equals(currentLetter) && (!newPos.equals(parentPos))) {
							System.out.println(currentLetter);
							parentCoordinates[0] = startingCoordinates[0];
							parentCoordinates[1] = startingCoordinates[1];
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
