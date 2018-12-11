import java.util.*;
import java.io.*;

public class Day1 {
	public static void main(String[] args) {
		int total = 0;
        ArrayList<Integer> inputFreqs = new ArrayList<Integer>();
        ArrayList<Integer> firstFreqs = new ArrayList<Integer>();
        firstFreqs.add(total);
        boolean freqFound = false;
        int firstFreq = 11111111;

		try {
            Scanner f = new Scanner(new File("day1input.txt"));

            while (f.hasNextLine()) {
                int input = Integer.parseInt(f.nextLine());
                inputFreqs.add(input);
            	total += input;
                
                if (!freqFound && firstFreqs.contains(total)) {
                    firstFreq = total;
                    freqFound = true;
                } else {
                    firstFreqs.add(total);
                }
            }

            while (!freqFound) {
                for (Integer i : inputFreqs) {
                    total += i;

                    if (!freqFound && firstFreqs.contains(total)) {
                        firstFreq = total;
                        freqFound = true;
                    } else {
                        firstFreqs.add(total);
                    }
                }

                System.out.println("next total: " + total);
            }

            System.out.println("first repeated frequency: " + firstFreq);
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            System.exit(1);
        }
	}
}