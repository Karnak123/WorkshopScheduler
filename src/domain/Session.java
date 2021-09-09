package domain;

public class Session {
    private int id;
    private Workshop workshop;
    private Speaker speaker;
    private SessionTime sessionTime;
    private Room room;

    public Session(int id, Workshop workshop) {
        this.id = id;
        this.workshop = workshop;
        this.speaker = workshop.getSpeaker();
    }

    public int getId() {
        return id;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public SessionTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(SessionTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String toString() {
        return "[" + workshop.getId() + "," + room.getId() + "," + speaker.getId() + "," + sessionTime.getId() + "]";
    }
}
