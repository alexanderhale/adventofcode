import java.util.*;
import java.io.*;

public class Day6 {
    private class GridSlot {
        private Coordinate nearest;
        private int lowestDist;
        private boolean tiedNearestCoords;

        public GridSlot(int i, int j, Coordinate c) {
            this.lowestDist = Integer.MAX_VALUE;
            this.nearest = new Coordinate("-1000, -1000", '.');
            this.tiedNearestCoords = false;
            this.add(i, j, c);
        }

        public void add(int i, int j, Coordinate c) {
            int distance = Math.abs(i - c.getX()) + Math.abs(j - c.getY());
            if (distance == this.lowestDist) {
                this.tiedNearestCoords = true;
            } else if (distance < this.lowestDist) {
                this.tiedNearestCoords = false;
                this.nearest = c;
                this.lowestDist = distance;
            }
        }

        public Coordinate getClosest() {
            // if 2 or more coordinates tie for closest, it isn't added to either of their tallies
            if (this.tiedNearestCoords) {
                return (new Coordinate("-1000, -1000", '.'));
            } else {
                this.nearest.incrementNumClosest();
                return nearest;
            }
        }
    }

    private class Coordinate {
        private String fullData;
        private Character label;
        private int x;
        private int y;
        private boolean infinite;
        private int numClosest;

        public Coordinate(String data, Character label) {
            this.fullData = data;
            this.label = label;
            this.x = Integer.parseInt(fullData.split(", ")[0]);
            this.y = Integer.parseInt(fullData.split(", ")[1]);
            this.infinite = false;
            this.numClosest = 0;
        }

        public String getFull() {
            return this.fullData;
        }
        public Character getLabel() {
            return this.label;
        }
        public int getX() {
            return this.x;
        }
        public int getY() {
            return this.y;
        }
        public boolean getInfinite() {
            return this.infinite;
        }
        public int getNumClosest() {
            return this.numClosest;
        }
        public void incrementNumClosest() {
            this.numClosest++;
        }
        public void setInfinite(boolean infinite) {
            this.infinite = infinite;
        }
    }


    public void run(String[] args) {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        int[] b = new int[4];       // grid boundaries
            // b[0] = min x, b[1] = max x, b[2] = min y, b[3] = max y
            b[0] = Integer.MAX_VALUE;
            b[1] = Integer.MIN_VALUE;
            b[2] = Integer.MAX_VALUE;
            b[3] = Integer.MIN_VALUE;
        Coordinate blank = new Coordinate("-1000, -1000", '.');

        try {
            Scanner f = new Scanner(new File("day6input.txt"));
            Character label = 'A';

            while (f.hasNextLine()) {
                Coordinate c = new Coordinate(f.nextLine(), label);
                coords.add(c);
                label++;
                if (label == 'Z' + 1) {
                    label = 'a';        // handle up to 52 coordinates
                }

                int x = c.getX();
                int y = c.getY();
                if (x < b[0]) {
                    b[0] = x;
                } else if (x > b[1]) {
                    b[1] = x;
                }

                if (y < b[2]) {
                    b[2] = y;
                } else if (y > b[3]) {
                    b[3] = y;
                }
            }
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }

        int width = b[1] - b[0] + 2;
        int height = b[3] - b[2] + 2;

        GridSlot[][] grid = new GridSlot[width][height];
        
        for (Coordinate c : coords) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (grid[i][j] == null) {
                        GridSlot gs = new GridSlot(i + b[0], j + b[2], c);
                        grid[i][j] = gs;
                    } else {
                        grid[i][j].add(i + b[0], j + b[2], c);
                    }
                }
            }
        }

        // // printer for testing
        // for (int j = 0; j < height; j++) {
        //     String line = "";
        //     for (int i = 0; i < width; i++) {
        //         Coordinate closest = grid[i][j].getClosest();
        //         if (closest.getX() == -1000 && closest.getY() == -1000) {
        //             closest = blank;    // period for tied squares
        //         }
        //         line += closest.getLabel();
        //     }
        //     System.out.println(line);
        // }

        // find infinite areas (i.e. areas that touch the border)
        for (int i = 0; i < grid.length; i++) {
            // top and bottom edges
                // it's ok that getClosest() is called again here because
                // it's set to infinite -> numClosest value isn't read
            grid[i][0].getClosest().setInfinite(true);
            grid[i][grid.length - 1].getClosest().setInfinite(true);
        }

        for (int j = 0; j < grid[0].length; j++) {
            // left and right edges
                // it's ok that getClosest() is called again here because
                // it's set to infinite -> numClosest value isn't read
            grid[0][j].getClosest().setInfinite(true);
            grid[grid.length - 1][j].getClosest().setInfinite(true);
        }

        int largestArea = 0;
        for (Coordinate c : coords) {
            if (c.getNumClosest() > largestArea && c.getInfinite() == false) {
                largestArea = c.getNumClosest();
            }
        }

        System.out.println(largestArea);
    }
    public static void main(String[] args) {
        try {
            Day6 day6 = new Day6();
            day6.run(args);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}