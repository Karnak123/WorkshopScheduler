package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB01 {
        public static void main(String args[]) throws SQLException {
                Connection connection = DriverManager.getConnection(DBMgr.url);
                if (connection != null) {
                        System.out.println("New db created");
                        handleSpeaker(connection);
                        handleRoom(connection);
                        handleSessionTime(connection);
                        handleWorkshop(connection);
                        handleWorkshopSpeaker(connection);
                }
        }

        static void handleSpeaker(Connection connection) throws SQLException {
                connection.createStatement().execute("create table speaker (id text not null, name text not null)");
                connection.prepareStatement("insert into speaker(id, name) values" + "('S01', 'A. Blue'),"
                                + "('S02', 'B. Black')," + "('S03', 'C. Yellow')," + "('S04', 'D. White'),"
                                + "('S05', 'E. Red')," + "('S06', 'F. Green')," + "('S07', 'G. Gray')").executeUpdate();
        }

        static void handleRoom(Connection connection) throws SQLException {
                connection.createStatement().execute("create table room (id text not null, name text not null)");
                connection.prepareStatement(
                                "insert into room(id, name) values ('R01', 'room one'), ('R02', 'room two')")
                                .executeUpdate();
        }

        static void handleSessionTime(Connection connection) throws SQLException {
                connection.createStatement().execute(
                                "create table session_time (id text not null, time text not null, previous_id)");
                connection.prepareStatement("insert into session_time(id, time, previous_id) values"
                                + "('ST01', 'Day01 08:00 - 09:00', '')," + "('ST02', 'Day01 09:00 - 10:00', 'ST01'),"
                                + "('ST03', 'Day01 10:00 - 11:00', 'ST02'),"
                                + "('ST04', 'Day01 11:00 - 12:00', 'ST03'),"
                                + "('ST05', 'Day01 01:00 - 02:00', 'ST04'),"
                                + "('ST06', 'Day01 02:00 - 03:00', 'ST05'),"
                                + "('ST07', 'Day01 03:00 - 04:00', 'ST06'),"
                                + "('ST08', 'Day01 04:00 - 05:00', 'ST07')," + "('ST09', 'Day02 08:00 - 09:00', ''),"
                                + "('ST10', 'Day02 09:00 - 10:00', 'ST09'),"
                                + "('ST11', 'Day02 10:00 - 11:00', 'ST10'),"
                                + "('ST12', 'Day02 11:00 - 12:00', 'ST11'),"
                                + "('ST13', 'Day02 01:00 - 02:00', 'ST12'),"
                                + "('ST14', 'Day02 02:00 - 03:00', 'ST13'),"
                                + "('ST15', 'Day02 03:00 - 04:00', 'ST14'),"
                                + "('ST16', 'Day02 04:00 - 05:00', 'ST15')").executeUpdate();
        }

        static void handleWorkshop(Connection connection) throws SQLException {
                connection.createStatement().execute("create table workshop (id text not null, name text not null)");
                connection.prepareStatement("insert into workshop(id, name) values" + "('WS01', 'workshop 01'),"
                                + "('WS02', 'workshop 02')," + "('WS03', 'workshop 03')," + "('WS04', 'workshop 04'),"
                                + "('WS05', 'workshop 05')," + "('WS06', 'workshop 06')," + "('WS07', 'workshop 07'),"
                                + "('WS08', 'workshop 08')," + "('WS09', 'workshop 09')," + "('WS10', 'workshop 10'),"
                                + "('WS11', 'workshop 11')," + "('WS12', 'workshop 12')," + "('WS13', 'workshop 13'),"
                                + "('WS14', 'workshop 14')," + "('WS15', 'workshop 15')," + "('WS16', 'workshop 16'),"
                                + "('WS17', 'workshop 17')," + "('WS18', 'workshop 18')," + "('WS19', 'workshop 19')")
                                .executeUpdate();
        }

        static void handleWorkshopSpeaker(Connection connection) throws SQLException {
                connection.createStatement().execute(
                                "create table workshop_speaker (workshop_id text not null, speaker_id text not null)");
                connection.prepareStatement("insert into workshop_speaker(workshop_id, speaker_id) values"
                                + "('WS01', 'S01')," + "('WS02', 'S02')," + "('WS03', 'S03')," + "('WS04', 'S04'),"
                                + "('WS05', 'S05')," + "('WS06', 'S06')," + "('WS07', 'S07')," + "('WS08', 'S01'),"
                                + "('WS09', 'S02')," + "('WS10', 'S03')," + "('WS11', 'S04')," + "('WS12', 'S05'),"
                                + "('WS13', 'S06')," + "('WS14', 'S07')," + "('WS15', 'S01')," + "('WS16', 'S02'),"
                                + "('WS17', 'S03')," + "('WS18', 'S04')," + "('WS19', 'S05')").executeUpdate();
        }
}
