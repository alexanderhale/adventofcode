import java.util.*;
import java.io.*;

public class Day5 {

    public void run(String[] args) {
        // this is definitely a slow brute force approach
        String input = "";
        try {
            Scanner f = new Scanner(new File("day5input.txt"));
            input = f.nextLine();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }

        System.out.println("part 1: " + fullReductionFinalLength(input));

        ArrayList<Integer> shortestResults = new ArrayList<>(26);
        for (int i = 0; i < 26; i++) {
            shortestResults.add(fullReductionFinalLength(removeLetter(input, i)));
        }

        System.out.println("part 2: " + Collections.min(shortestResults));
    }

    public int fullReductionFinalLength(String in) {
        boolean changeMade = true;
        while (changeMade) {
            changeMade = false;
            for (int i = 0; i < in.length() - 1; i++) {
                if (in.charAt(i) == in.charAt(i+1) + 32 || in.charAt(i) == in.charAt(i+1) - 32) {
                    in = in.substring(0, i) + in.substring(i + 2);
                    changeMade = true;
                }
            }
        }
        return in.length();
    }

    public String removeLetter(String in, int letter) {
        boolean changeMade = true;
        while (changeMade) {
            changeMade = false;
            for (int i = 0; i < in.length(); i++) {
                if (in.charAt(i) == 65 + letter || in.charAt(i) == 97 + letter) {
                    in = in.substring(0, i) + in.substring(i + 1);
                    changeMade = true;
                }
            }
        }
        return in;
    }
    public static void main(String[] args) {
        try {
            Day5 day5 = new Day5();
            day5.run(args);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}