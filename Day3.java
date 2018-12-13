import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Day3 {
	public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> claims = new ArrayList<ArrayList<Integer>>();
            // index = claim #
            // value 1 = horizontal start point
            // value 2 = vertical start point
            // value 3 = horizontal end point
            // value 4 = vertical end point

		try {
            Scanner f = new Scanner(new File("day3input.txt"));

            Pattern startCoord = Pattern.compile("\\d+,\\d+");
            Pattern dimensions = Pattern.compile("\\d+x\\d+");


            while (f.hasNextLine()) {
                String input = f.nextLine();
                
                Matcher sCMatcher = startCoord.matcher(input);
                Matcher dimMatcher = dimensions.matcher(input);

                ArrayList<Integer> coordinates = new ArrayList<Integer>();
                
                while (sCMatcher.find()) {
                    coordinates.add(Integer.parseInt(sCMatcher.group().split(",")[0]));
                    coordinates.add(Integer.parseInt(sCMatcher.group().split(",")[1]));
                }
                while (dimMatcher.find()) {
                    coordinates.add(Integer.parseInt(dimMatcher.group().split("x")[0]) + coordinates.get(0));
                    coordinates.add(Integer.parseInt(dimMatcher.group().split("x")[1]) + coordinates.get(1));
                }

                claims.add(coordinates);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }

        // find size of fabric using max & min values inside claims
        int minHorizontal = Integer.MAX_VALUE;      // should always start at (0, 0), but worth checking
        int minVertical = Integer.MAX_VALUE;
        int maxHorizontal = Integer.MIN_VALUE;      // start from min possible and move up from there
        int maxVertical = Integer.MIN_VALUE;
        
        for (ArrayList<Integer> claim : claims) {
            if (claim.get(0) < minHorizontal) {
                minHorizontal = claim.get(0);
            }
            if (claim.get(1) < minVertical) {
                minVertical = claim.get(1);
            }
            if (claim.get(2) > maxHorizontal) {
                maxHorizontal = claim.get(2);
            }
            if (claim.get(3) > maxVertical) {
                maxVertical = claim.get(3);
            }
        }

        // each index contains a count of how many claims have requested that square of fabric
        int[][] fabric = new int[maxHorizontal - minHorizontal][maxVertical - minVertical];

        for (ArrayList<Integer> claim : claims) {
            for (int i = claim.get(0); i < claim.get(2); i++) {
                for (int j = claim.get(1); j < claim.get(3); j++) {
                    fabric[i][j]++;
                }
            }
        }

        int overlapped = 0;
        ArrayList<ArrayList<Integer>> uniques = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < fabric.length; i++) {
            for (int j = 0; j < fabric[i].length; j++) {
                if (fabric[i][j] >=2) {
                    overlapped++;
                } else if (fabric[i][j] == 1) {
                    ArrayList<Integer> pair = new ArrayList<Integer>();
                    pair.add(i);
                    pair.add(j);
                    uniques.add(pair);
                }
            }
        }

        System.out.println(overlapped); // Part 1

        for (ArrayList<Integer> claim : claims) {
            boolean fullMatch = true;

            for (int i = claim.get(0); i < claim.get(2); i++) {
                for (int j = claim.get(1); j < claim.get(3); j++) {
                    ArrayList<Integer> pair = new ArrayList<Integer>();
                    pair.add(i);
                    pair.add(j);
                    if (!uniques.contains(pair)) {
                        fullMatch = false;
                        break;
                    }
                }
                if (!fullMatch) {
                    break;
                }
            }

            if (fullMatch) {
                System.out.println(claims.indexOf(claim) + 1);      // Part 2
            }
        }
	}
}