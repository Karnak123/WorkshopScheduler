package ga;

import java.util.ArrayList;
import java.util.stream.IntStream;

import db.DBMgr;

public class Population {
    private ArrayList<Schedule> schedules;

    public Population(int size, DBMgr dbMgr) {
        schedules = new ArrayList<Schedule>(size);
        IntStream.range(0, size).forEach(i -> schedules.add(new Schedule(dbMgr).initialize()));
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public Population sortByFitness() {
        schedules.sort((s1, s2) -> {
            int returnVal = 0;
            if (s1.getFitness() > s2.getFitness()) {
                returnVal = -1;
            } else if (s1.getFitness() < s2.getFitness()) {
                returnVal = 1;
            }
            return returnVal;
        });
        return this;
    }
}
