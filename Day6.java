import java.util.*;
import java.io.*;

public class Day6 {
    private class GridSlot {
        private Coordinate nearest;
        private int lowestDist;
        private boolean tiedNearestCoords;
        private int totalDistance;

        public GridSlot(int i, int j, Coordinate c) {
            this.lowestDist = Integer.MAX_VALUE;
            this.nearest = new Coordinate("-1000, -1000");
            this.tiedNearestCoords = false;
            this.totalDistance = 0;
            this.add(i, j, c);
        }

        public void add(int i, int j, Coordinate c) {
            int distance = Math.abs(i - c.getX()) + Math.abs(j - c.getY());
            this.totalDistance += distance;
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
                return (new Coordinate("-1000, -1000"));
            } else {
                this.nearest.incrementNumClosest();
                return nearest;
            }
        }

        public int getTotalDistance() {
            return this.totalDistance;
        }
    }

    private class Coordinate {
        private int x;
        private int y;
        private boolean infinite;
        private int numClosest;

        public Coordinate(String data) {
            this.x = Integer.parseInt(data.split(", ")[0]);
            this.y = Integer.parseInt(data.split(", ")[1]);
            this.infinite = false;
            this.numClosest = 0;
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
        b[0] = Integer.MAX_VALUE;   // min x
        b[1] = Integer.MIN_VALUE;   // max x
        b[2] = Integer.MAX_VALUE;   // min y
        b[3] = Integer.MIN_VALUE;   // max y

        try {
            Scanner f = new Scanner(new File("day6input.txt"));

            while (f.hasNextLine()) {
                Coordinate c = new Coordinate(f.nextLine());
                coords.add(c);

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

        // calculate distance to each coordinate from each slot
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

        // get the closest coordinate to each grid slot
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j == 0 || j == height - 1 || i == 0 || i == width - 1) {
                    grid[i][j].getClosest().setInfinite(true);
                } else {
                    grid[i][j].getClosest();
                }
            }
        }

        // part 1: find the largest area
        int largestArea = 0;
        for (Coordinate c : coords) {
            if (c.getNumClosest() > largestArea && c.getInfinite() == false) {
                largestArea = c.getNumClosest();
            }
        }
        System.out.println(largestArea);

        // part 2: find the number of slots with accumulated distance < 10000
        int slotsBelowTenThousand = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].getTotalDistance() < 10000) {
                    slotsBelowTenThousand++;
                }
            }
        }
        System.out.println(slotsBelowTenThousand);
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