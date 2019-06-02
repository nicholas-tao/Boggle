import java.util.*;

public class adjacentMethodTest {
	public static void main(String args[]) {
		
		String[][] input = {{"R", "A", "D", "E", "L"}, 
							{"B", "O", "W", "P", "O"}, 
							{"L", "O", "W", "W", "T"}, 
							{"L", "O", "W", "L", "T"}, 
							{"L", "O", "W", "W", "A"}};
		
		int[] coordinates = new int[2]; // can create an arraylist to deal with multiple occurances of same letter
		String word = "DWIOZ";		
		coordinates = getFirst(input, word);
		
		int x = coordinates[0];
		int y = coordinates[1];
		
		System.out.println(checkAdjacent(input, coordinates, word, 1, false));

	}
	
	public static int[] getFirst(String input[][], String word) {
		
		int[] coordinates = new int[2];
		for (int i=0; i<input.length; i++) { //fix if can't find first letter
			for (int j=0; j<input[i].length; j++) {
				if (input[i][j].equals(word.substring(0, 1))) {
					coordinates[0] = i;
					coordinates[1] = j;
				}
			}
		}
		
		return coordinates;
		
	}
	
	public static boolean checkAdjacent(String[][] input, int[] coordinates, String word, int wordCount, boolean letterFound) {
		if (wordCount > word.length()-1) {
			return true;
		}
			letterFound = false;
			String letter = Character.toString(word.charAt(wordCount));
			System.out.println(letter);
			
			int[] s = {-1, 0, 1};
			
			for (int i=0; i<3; i++) {
				int column = coordinates[1] + s[i];
				

				if (column < 0 || column >= input.length) {
					continue;
				} else {
					for (int j=0; j<3; j++) {
						
						int row = coordinates[0] + s[j];
						if (row < 0 || row >= input.length) {
							continue;
						} else {
							if (input[row][column].equals(letter)) {
								
								System.out.println(row + ", " + column);
								coordinates[0] = row;
								coordinates[1] = column;
								System.out.println("Word Count: "+wordCount);
								letterFound = true;
								wordCount += 1;
								
								checkAdjacent(input, coordinates, word, wordCount, letterFound);
							}
					}
					
				}
			}
			
		}
		if (letterFound == true) {
			return true;
		} else {
			return false;
		}
	}
	
}
