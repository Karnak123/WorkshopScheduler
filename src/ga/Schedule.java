package ga;

import java.util.ArrayList;

import db.DBMgr;
import domain.Session;
import domain.Speaker;

public class Schedule {
    private ArrayList<Session> sessions;
    private ArrayList<Conflict> conflicts;
    private DBMgr dbMgr;
    private double fitness = -1;
    private boolean isFitnessChanged = true;
    private int sessionId = 0;

    public Schedule(DBMgr dbMgr) {
        this.dbMgr = dbMgr;
        this.sessions = new ArrayList<Session>(dbMgr.getWorkshops().size());
    }

    public Schedule initialize() {
        dbMgr.getWorkshops().forEach(workshop -> {
            Session newSession = new Session(sessionId++, workshop);
            newSession.setSessionTime(
                    dbMgr.getSessionTimes().get((int) (Math.random() * dbMgr.getSessionTimes().size())));
            newSession.setRoom(dbMgr.getRooms().get((int) (Math.random() * dbMgr.getRooms().size())));
            sessions.add(newSession);
        });
        return this;
    }

    private double calculateFitness() {
        conflicts = new ArrayList<Conflict>();
        sessions.forEach(x -> {
            ArrayList<Session> speakerInConsecutiveSessionsConflict = new ArrayList<Session>();
            speakerInConsecutiveSessionsConflict.add(x);
            if (getAllSpeakerSessionTimes(x.getSpeaker()).contains(x.getSessionTime().getPreviousId())) {
                conflicts.add(new Conflict(Conflict.ConflictType.SPEAKER_IN_CONSECUTIVE_SESSIONS,
                        speakerInConsecutiveSessionsConflict));
            }
            sessions.stream().filter(y -> sessions.indexOf(y) >= sessions.indexOf(x)).forEach(y -> {
                if (x.getSessionTime() == y.getSessionTime() && x.getId() != y.getId()) {
                    if (x.getRoom().equals(y.getRoom())) {
                        ArrayList<Session> roomBookingConflict = new ArrayList<Session>();
                        roomBookingConflict.add(x);
                        roomBookingConflict.add(y);
                        conflicts.add(new Conflict(Conflict.ConflictType.ROOM_BOOKING___________________,
                                roomBookingConflict));
                    }
                    if (x.getSpeaker().equals(y.getSpeaker())) {
                        ArrayList<Session> speakerBookingConflict = new ArrayList<Session>();
                        speakerBookingConflict.add(x);
                        speakerBookingConflict.add(y);
                        conflicts.add(new Conflict(Conflict.ConflictType.SPEAKER_BOOKING________________,
                                speakerBookingConflict));
                    }
                }
            });
        });
        return 1 / (double) (conflicts.size() + 1);
    }

    public double getFitness() {
        if (isFitnessChanged) {
            fitness = calculateFitness();
            isFitnessChanged = false;
        }
        return fitness;
    }

    private ArrayList<String> getAllSpeakerSessionTimes(Speaker speaker) {
        ArrayList<String> sessionTimes = new ArrayList<String>();
        sessions.stream().forEach(x -> {
            if (x.getSpeaker().getId().equals(speaker.getId()))
                sessionTimes.add(x.getSessionTime().getId());
        });
        return sessionTimes;
    }

    public ArrayList<Session> getSessions() {
        isFitnessChanged = true;
        return sessions;
    }

    public ArrayList<Conflict> getConflicts() {
        return this.conflicts;
    }

    public DBMgr getDbMgr() {
        return dbMgr;
    }

    public String toString() {
        String returnValue = new String();
        for (int i = 0; i < sessions.size() - 1; ++i) {
            returnValue += sessions.get(i) + ",";
        }
        returnValue += sessions.get(sessions.size() - 1);
        return returnValue;
    }
}
