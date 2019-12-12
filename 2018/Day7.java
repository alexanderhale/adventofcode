import java.util.*;
import java.io.*;

public class Day7 {
    private class Steps {
        private ArrayList<Step> steps;
        private ArrayList<Character> characters;

        public Steps () {
            steps = new ArrayList<Step>();
            characters = new ArrayList<Character>();
        }

        public void add(Step s) {
            steps.add(s);
            characters.add(s.getLabel());
        }
        public void add(Character c) {
            characters.add(c);
            steps.add(new Step(c));
        }
        public void add(String s) {
            if (s.length() == 1) {
                characters.add(s.charAt(0));
                steps.add(new Step(s.charAt(0)));
            }
        }
        public void remove(Step s) {
            steps.remove(s);
            characters.remove(s.getLabel());
        }
        public void remove(Character c) {
            characters.remove(c);
            steps.remove(new Step(c));
        }
        public void remove(String s) {
            if (s.length() == 1) {
                characters.remove(s.charAt(0));
                steps.remove(new Step(s.charAt(0)));
            }
        }
        public Step get(Step s) {
            if (steps.contains(s)) {
                return s;
            }
            return null;
        }
        public Step get(Character c) {
            if (characters.contains(c)) {
                for (Step s : steps) {
                    if (s.getLabel() == c) {
                        return s;
                    }
                }
            }
            return null;
        }
        public Step get(String st) {
            if (st.length() == 1 && characters.contains(st.charAt(0))) {
                for (Step s : steps) {
                    if (s.getLabel() == st.charAt(0)) {
                        return s;
                    }
                }
            }
            return null;
        }
        public Step getEarliest() {
            Character earliest = 'Z';
            for (Step s : this.steps) {
                if (!s.checkComplete() && s.checkRequirementsComplete() && s.getLabel() < earliest) {
                    earliest = s.getLabel();
                }
            }
            return this.get(earliest);
        }
        public ArrayList<Step> iterator() {
            return this.steps;
        }
        public boolean contains(Step s) {
            if (steps.contains(s)) {
                return true;
            }
            return false;
        }
        public boolean contains(Character c) {
            if (characters.contains(c)) {
                return true;
            }
            return false;
        }
        public boolean contains(String st) {
            if (st.length() == 1 && characters.contains(st.charAt(0))) {
                return true;
            }
            return false;
        }
        public boolean checkComplete() {
            for (Step s : steps) {
                if (!s.checkComplete()) {
                    return false;
                }
            }
            return true;
        }
        public String toString() {
            String ret = "";
            for (Step s : steps) {
                if (!s.checkComplete()) {
                    String line = s.getLabel() + ": ";
                    for (Step r : s.getRequirements().iterator()) {
                        if (!r.checkComplete())
                            line += r.getLabel() + ", ";
                    }
                    ret += line + "\n";
                }
            }
            return ret;
        }
    }

    private class Step {
        private Steps requirements;
        private Character label;
        private boolean complete;
        private boolean started;
        private int startTime;
        private int duration;
        private int endTime;

        public Step (Character label) {
            this.requirements = new Steps();
            this.label = label;
            this.complete = false;
            this.started = false;
            this.startTime = 0;
            this.duration = 60 + (label - 64);
            this.endTime = Integer.MAX_VALUE;
        }

        public Character getLabel() {
            return this.label;
        }
        public Steps getRequirements() {
            return this.requirements;
        }
        public void addRequirement(Step s) {
            this.requirements.add(s);
        }
        public void setStartTime(int startTime) {
            this.startTime = startTime;
            this.endTime = startTime + duration;
        }
        public int getEndTime() {
            return this.endTime;
        }
        public boolean checkComplete() {
            return this.complete;
        }
        public void complete() {
            this.complete = true;
        }
        public void incomplete() {
            this.complete = false;
        }
        public void start() {
            this.started = true;
        }
        public boolean started() {
            return this.started;
        }
        public boolean checkRequirementsComplete() {
            return this.requirements.checkComplete();
        }
    }

    public void run(String[] args) {
        Steps steps = new Steps();

        try {
            Scanner f = new Scanner(new File("day7input.txt"));

            while (f.hasNextLine()) {
                String[] line = f.nextLine().split(" ");

                if (!steps.contains(line[1])) {
                    steps.add(line[1]);
                }
                if (!steps.contains(line[7])) {
                    steps.add(line[7]);
                }
                steps.get(line[7]).addRequirement(steps.get(line[1]));
            }
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        }

        // Part 1
        while (!steps.checkComplete()) {
            Step s = steps.getEarliest();
            s.complete();
            System.out.print(s.getLabel());
        }

        // reset for part 2
        System.out.println();
        for (Step s : steps.iterator()) {
            s.incomplete();
        }

        int workers = 5;
        int elapsedTime = 0;
        while (!steps.checkComplete()) {
            for (Step s : steps.iterator()) {
                if (s.started() && s.getEndTime() == elapsedTime) {
                    s.complete();
                    workers++;
                }
            }

            Steps ready = new Steps();
            for (Step s : steps.iterator()) {
                if (!s.checkComplete() && s.checkRequirementsComplete() && !s.started()) {
                    ready.add(s);
                }
            }

            while (workers > 0 && !ready.iterator().isEmpty()) {
                Step s = ready.getEarliest();
                s.setStartTime(elapsedTime);
                s.start();
                ready.remove(s);
                workers--;
            }

            elapsedTime++;
        }
        System.out.println(elapsedTime - 1);
    }

    public static void main(String[] args) {
        try {
            Day7 day7 = new Day7();
            day7.run(args);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
}