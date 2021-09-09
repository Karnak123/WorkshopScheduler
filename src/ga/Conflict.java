package ga;

import java.util.ArrayList;

import domain.Session;

public class Conflict {
    static enum ConflictType {
        SPEAKER_BOOKING________________, ROOM_BOOKING___________________, SPEAKER_IN_CONSECUTIVE_SESSIONS
    }

    Conflict.ConflictType conflictType;
    ArrayList<Session> conflictBetweenSessions = new ArrayList<Session>();

    public Conflict(Conflict.ConflictType conflictType, ArrayList<Session> conflictBetweenSessions) {
        this.conflictType = conflictType;
        this.conflictBetweenSessions = conflictBetweenSessions;
    }

    public Conflict.ConflictType getConflictType() {
        return conflictType;
    }

    public ArrayList<Session> getConflictBetweenSessions() {
        return conflictBetweenSessions;
    }

    public String toString() {
        return new String(this.conflictType + " " + this.conflictBetweenSessions);
    }
}
