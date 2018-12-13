import java.util.*;
import java.io.*;

public class Day2 {
	public static void main(String[] args) {
		int twos = 0;
        int threes = 0;
        ArrayList<String> inputCodes = new ArrayList<String>();

		try {
            Scanner f = new Scanner(new File("day2input.txt"));

            while (f.hasNextLine()) {
                String input = f.nextLine();
                inputCodes.add(input);
                
                ArrayList<Integer> letterCounts = new ArrayList<Integer>();
                for (int i = 0; i < 26; i++) {
                    letterCounts.add(0);
                }

                for (int i = 0; i < input.length(); i++) {
                    int position = input.charAt(i) - 97;
                    letterCounts.set(position, letterCounts.get(position) + 1);
                }

                if (letterCounts.contains(2)) {
                    twos++;
                }
                if (letterCounts.contains(3)) {
                    threes++;
                }
            }

            System.out.println("checksum: " + (twos * threes));
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }

        findPrototypes(inputCodes);
	}

    public static void findPrototypes(ArrayList<String> inputCodes) {
        for (int j = 0; j < inputCodes.size(); j++) {
            String inputCode = inputCodes.get(j);
            for (int i = j + 1; i < inputCodes.size(); i++) {
                String otherInputCode = inputCodes.get(i);
                int lettersMismatched = 0;
                int indexMismatched = -1;

                for (int t = 0; t < inputCode.length(); t++) {
                    if (inputCode.charAt(t) != otherInputCode.charAt(t)) {
                        lettersMismatched++;
                        indexMismatched = t;
                    }
                }

                if (lettersMismatched == 1) {
                    System.out.println(inputCode.substring(0, indexMismatched) + inputCode.substring(indexMismatched + 1, inputCode.length()));
                    break;
                }
            }
        }
    }
}