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

        boolean changeMade = true;
        while (changeMade) {
            changeMade = false;
            for (int i = 0; i < input.length() - 1; i++) {
                if (input.charAt(i) == input.charAt(i+1) + 32 || input.charAt(i) == input.charAt(i+1) - 32) {
                    input = input.substring(0, i) + input.substring(i+2);
                    changeMade = true;
                }
            }
        }

        System.out.println(input.length());
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