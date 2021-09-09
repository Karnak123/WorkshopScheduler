package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import domain.Room;
import domain.SessionTime;
import domain.Speaker;
import domain.Workshop;

public class DBMgr {
    public static String url = "jdbc:sqlite:scheduler.db";
    private ArrayList<Room> rooms;
    private ArrayList<Speaker> speakers;
    private ArrayList<Workshop> workshops;
    private ArrayList<SessionTime> sessionTimes;

    public DBMgr() throws SQLException {
        initialize();
    }

    private DBMgr initialize() throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        rooms = selectRooms(connection);
        sessionTimes = selectSessionTimes(connection);
        speakers = selectSpeakers(connection);
        workshops = selectWorkshops(connection);
        return this;
    }

    public ArrayList<Room> selectRooms(Connection connection) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<Room>();
        ResultSet roomRS = connection.createStatement().executeQuery("SELECT * FROM room");
        while (roomRS.next()) {
            rooms.add(new Room(roomRS.getString("id"), roomRS.getString("name")));
        }
        return rooms;
    }

    public ArrayList<SessionTime> selectSessionTimes(Connection connection) throws SQLException {
        ArrayList<SessionTime> sessionTimes = new ArrayList<SessionTime>();
        ResultSet sessionTimeRS = connection.createStatement().executeQuery("SELECT * FROM session_time");
        while (sessionTimeRS.next()) {
            sessionTimes.add(new SessionTime(sessionTimeRS.getString("id"), sessionTimeRS.getString("time"),
                    sessionTimeRS.getString("previous_id")));
        }
        return sessionTimes;
    }

    public ArrayList<Speaker> selectSpeakers(Connection connection) throws SQLException {
        ArrayList<Speaker> speakers = new ArrayList<Speaker>();
        ResultSet speakerRS = connection.createStatement().executeQuery("SELECT * FROM speaker");
        while (speakerRS.next()) {
            speakers.add(new Speaker(speakerRS.getString("id"), speakerRS.getString("name")));
        }
        return speakers;
    }

    public ArrayList<Workshop> selectWorkshops(Connection connection) throws SQLException {
        ArrayList<Workshop> workshops = new ArrayList<Workshop>();
        ResultSet workshopRS = connection.createStatement().executeQuery("SELECT * FROM workshop");
        while (workshopRS.next()) {
            ResultSet workshopSpeakerRS = connection.createStatement().executeQuery(
                    "SELECT * FROM workshop_speaker WHERE workshop_id = '" + workshopRS.getString("id") + "'");
            ArrayList<Speaker> workshopSpeakers = new ArrayList<Speaker>();
            while (workshopSpeakerRS.next()) {
                for (Speaker speaker : speakers) {
                    if (speaker.getId().equals(workshopSpeakerRS.getString("speaker_id"))) {
                        workshopSpeakers.add(speaker);
                    }
                }
            }
            workshops.add(
                    new Workshop(workshopRS.getString("id"), workshopRS.getString("name"), workshopSpeakers.get(0)));
        }
        return workshops;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<SessionTime> getSessionTimes() {
        return sessionTimes;
    }

    public ArrayList<Speaker> getSpeakers() {
        return speakers;
    }

    public ArrayList<Workshop> getWorkshops() {
        return workshops;
    }
}
