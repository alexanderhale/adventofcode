import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Day4 {
    private class Observation {
        private Date date;
        private int minute;
        private int guardID;
        private int action;     // 1 = begins shift, 2 = falls asleep, 3 = wakes up

        public Observation() {
            date = new Date();
            guardID = 0;
            action = 0;
        }

        public Date getDate() {
            return date;
        }
        public int getMinute() {
            return minute;
        }
        public int getID() {
            return guardID;
        }
        public int getAction() {
            return action;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public void setMinute(int minute) {
            this.minute = minute;
        }
        public void setID(int guardID) {
            this.guardID = guardID;
        }
        public void setAction(int action) {
            this.action = action;
        }
    }

    private class Guard {
        private int id;
        private int[] record;

        public Guard(int id) {
            this.id = id;
            this.record = new int[60];
        }

        public int getID() {
            return this.id;
        }
        public int[] getRecord() {
            return this.record;
        }
        public int getRecord(int i) {
            return this.record[i];
        }
        public void setRecord(int i, int value) {
            this.record[i] = value;
        }

        public int getTotalMinsAsleep() {
            int ret = 0;
            for (int i = 0; i < record.length; i++) {
                ret += record[i];
            }
            return ret;
        }

        public int getMinuteMostSlept() {
            int ret = -1;
            int max = 0;
            for (int i = 0; i < record.length; i++) {
                if (record[i] > max) {
                    max = record[i];
                    ret = i;
                }
            }
            return ret;
        }

        public int getInstancesOfMinMostSlept() {
            return record[this.getMinuteMostSlept()];
        }

        public void addSleepRecord(int start, int end) {
            for (int i = start; i < end; i++) {
                record[i]++;
            }
        }
    }

	public static void main(String[] args) {
       try {
           Day4 day4 = new Day4();
           day4.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run(String[] args) {
        ArrayList<Observation> observations = new ArrayList<Observation>();

		try {
            Scanner f = new Scanner(new File("day4input.txt"));

            Pattern datePat = Pattern.compile("\\[.*\\]");
            Pattern minutePat = Pattern.compile(":\\d+");
            Pattern infoPat = Pattern.compile("\\].*");
            Pattern idPat = Pattern.compile("\\d+");

            while (f.hasNextLine()) {
                String input = f.nextLine();
                
                Matcher dateMatcher = datePat.matcher(input);
                Matcher minuteMatcher = minutePat.matcher(input);
                Matcher infoMatcher = infoPat.matcher(input);

                ArrayList<Integer> coordinates = new ArrayList<Integer>();
                
                Observation observation = new Observation();

                while (dateMatcher.find()) {
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd kk:mm");
                    String fullDate = dateMatcher.group();
                    observation.setDate(ft.parse(fullDate.substring(1, fullDate.length() - 1)));
                }
                while (minuteMatcher.find()) {
                    observation.setMinute(Integer.parseInt(minuteMatcher.group().split(":")[1]));
                }
                while (infoMatcher.find()) {
                    String info = infoMatcher.group();
                    Matcher idMatcher = idPat.matcher(info);

                    if (info.contains("begins shift")) {
                        observation.setAction(1);

                        while (idMatcher.find()) {
                            observation.setID(Integer.parseInt(idMatcher.group()));
                        }
                    } else if (info.contains("falls asleep")) {
                        observation.setAction(2);
                    } else {
                        observation.setAction(3);
                    }
                }

                observations.add(observation);
            }
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        observations.sort((Observation o1, Observation o2) -> (o1.getDate().compareTo(o2.getDate())));

        // next step: assign guard IDs to wakeup and fall-asleep actions
            // simultaneously calculate total minutes asleep for each guard and keep track of max minutes asleep
        
        int runningID = 0;
        int minuteAsleep = 0;
        Map<Integer, Guard> guards = new HashMap<Integer, Guard>();
        for (Observation o : observations) {
            if (o.getID() != 0) {
                runningID = o.getID();
            } else {
                o.setID(runningID);
            }

            if (o.getAction() == 2) {
                minuteAsleep = o.getMinute();
            } else if (o.getAction() == 3) {
                Guard g;
                if (guards.containsKey(o.getID())) {
                    g = guards.get(o.getID());
                } else {
                    g = new Guard(o.getID());
                    guards.put(o.getID(), g);
                }
                g.addSleepRecord(minuteAsleep, o.getMinute());
            }
        }

        int maxMinsTotal = 0, maxMinsID = 0;
        int winningGuardID = 0, mostTimesSleptOnSameMinute = 0;
        for (Map.Entry<Integer, Guard> entry : guards.entrySet()) {
            Guard g = entry.getValue();
            if (g.getTotalMinsAsleep() > maxMinsTotal) {
                maxMinsID = g.getID();
                maxMinsTotal = g.getTotalMinsAsleep();
            }

            if (g.getInstancesOfMinMostSlept() > mostTimesSleptOnSameMinute) {
                mostTimesSleptOnSameMinute = g.getInstancesOfMinMostSlept();
                winningGuardID = g.getID();
            }
        }

        // now, find the minute at which the guard with ID maxMinsID was most often asleep
        int maxMinute = guards.get(maxMinsID).getMinuteMostSlept();

        System.out.println("maxID: " + maxMinsID);
        System.out.println("maxMinute: " + maxMinute);
        System.out.println("multiplied result: " + maxMinsID * maxMinute);

        int mostAsleepMinute = guards.get(winningGuardID).getMinuteMostSlept();
        System.out.println("guard #" + winningGuardID + " fell asleep " + mostTimesSleptOnSameMinute + " times on minute " + mostAsleepMinute);
        System.out.println("multiplied result: " + winningGuardID * mostAsleepMinute);
    }
}