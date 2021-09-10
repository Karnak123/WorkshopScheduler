package ga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import db.DBMgr;
import domain.Room;
import domain.Session;
import domain.SessionTime;
import domain.Speaker;

public class Driver {
    public static final int POPULATION_SIZE = 9;
    public static final double MUTATION_RATE = 0.1;
    public static final double CROSSOVER_RATE = 0.9;
    public static final int TOURNAMENT_SELECTION_SIZE = 3;
    public static final int NUMB_OF_ELITE_SCHEDULES = 1;
    private Schedule fittestSchedule = null;
    private DBMgr dbMgr;
    private int scheduleNumb = 0;
    private int sessionId = 1;
    static boolean verboseFlag = false;

    public static void main(String[] args) throws SQLException, IOException {
        Driver driver = new Driver();
        driver.dbMgr = new DBMgr();
        driver.handleCommandLine();
    }

    private void handleCommandLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while (flag) {
            System.out.println(
                    "> What do you want to do (i:nitial data display, f:ind fittest schedule, d:efault mode, v:erbose mode, e:xit) ?");
            switch (br.readLine()) {
                case "i":
                    displayInitialData();
                    break;
                case "f":
                    if (findFittestSchedule())
                        handleScheduleDisplay();
                    break;
                case "d":
                    verboseFlag = false;
                    break;
                case "v":
                    verboseFlag = true;
                    break;
                case "e":
                    flag = false;
                    break;
                default:
                    System.out.println("> Invalid command");
                    break;
            }
        }
        System.exit(0);
    }

    private void handleScheduleDisplay() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while (flag) {
            System.out.println(
                    "> What do you want to display (w:orkshop schedule, t:ime schedule, r:oom schedule, s:peaker schedule, e:xit) ?");
            switch (br.readLine().toLowerCase()) {
                case "w":
                    System.out.println("\n> from 'workshop' perspective");
                    displayScheduleAsTable(fittestSchedule);
                    break;
                case "t":
                    displayScheduleSessionTimes(fittestSchedule);
                    break;
                case "r":
                    displayScheduleRooms(fittestSchedule);
                    break;
                case "s":
                    displayScheduleSpeakers(fittestSchedule);
                    break;
                case "e":
                    flag = false;
                    break;
                default:
                    System.out.println("> Invalid command");
                    break;
            }
        }
    }

    private boolean findFittestSchedule() {
        boolean flag = false;
        int generationNumber = 0;
        if (verboseFlag) {
            System.out.println("> generation # " + generationNumber);
            System.out.print("  schedule # | fitness | # conflicts | ");
            System.out.println("session [workshop id, room id, speaker id, meeting-time id]");
            IntStream.range(0, 96).forEach(i -> {
                System.out.print("-");
                if (i == 95)
                    System.out.println();
            });
        }
        Population population = new Population(Driver.POPULATION_SIZE, dbMgr).sortByFitness();
        if (verboseFlag) {
            population.getSchedules().forEach(schedule -> {
                System.out.println(String.format("  %1$03d", scheduleNumb++) + "  | "
                        + String.format("%.5f", schedule.getFitness()) + "|     "
                        + String.format("%1$03d", schedule.getConflicts().size()) + "   | " + schedule);
            });
            displayScheduleAsTable(population.getSchedules().get(0));
        }
        if (population.getSchedules().get(0).getFitness() != 1) {
            if (verboseFlag)
                displayScheduleConflicts(population.getSchedules().get(0));
            sessionId = 1;
            flag = handleEvolvePopulation(population, generationNumber);
        } else {
            System.out.println("\n> Solution Found in " + (generationNumber + 1) + " generations");
            fittestSchedule = population.getSchedules().get(0);
            flag = true;
        }
        return flag;
    }

    private boolean handleEvolvePopulation(Population population, int generationNumber) {
        boolean flag = false;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dbMgr);
        while (population.getSchedules().get(0).getFitness() != 1.0) {
            ++generationNumber;
            if (verboseFlag) {
                System.out.println("> generation # " + generationNumber);
                System.out.print("  schedule # | fitness | # conflicts | ");
                System.out.println("session [workshop id, room id, speaker id, meeting-time id]");
                IntStream.range(0, 96).forEach(i -> {
                    System.out.print("-");
                    if (i == 95)
                        System.out.println();
                });
            }
            population = geneticAlgorithm.evolve(population).sortByFitness();
            scheduleNumb = 0;
            if (verboseFlag) {
                population.getSchedules().forEach(schedule -> {
                    System.out.println(String.format("  %1$03d", scheduleNumb++) + "  | "
                            + String.format("%.5f", schedule.getFitness()) + "|     "
                            + String.format("%1$03d", schedule.getConflicts().size()) + "   | " + schedule);
                });
                displayScheduleAsTable(population.getSchedules().get(0));
            }
            if (population.getSchedules().get(0).getFitness() == 1) {
                System.out.println("\n> Solution Found in " + (generationNumber + 1) + " generations");
                fittestSchedule = population.getSchedules().get(0);
                flag = true;
            } else {
                sessionId = 1;
                if (verboseFlag)
                    displayScheduleConflicts(population.getSchedules().get(0));
            }
        }
        return flag;
    }

    private Driver displayScheduleConflicts(Schedule schedule) {
        if (schedule.getConflicts().size() > 0) {
            System.out.println("conflict type                   |   conflict between sessions");
            System.out.println("-------------------------------------------------------");
            for (Conflict conflict : schedule.getConflicts()) {
                System.out.println(conflict.getConflictType() + " | "
                        + Arrays.toString(conflict.getConflictBetweenSessions().toArray()));
            }
            System.out.println();
        }
        return this;
    }

    private void displayScheduleAsTable(Schedule schedule) {
        ArrayList<Session> sessions = schedule.getSessions();
        System.out.println("    session id  | " + String.format("%1$-82s", "workshop (id)")
                + "|   room id |   speaker (id)    | session time (id)");
        IntStream.range(0, 154).forEach(i -> {
            System.out.print("-");
            if (i == 153)
                System.out.println();
        });
        sessions.forEach(e -> {
            int workshopsIndex = dbMgr.getWorkshops().indexOf(e.getWorkshop());
            int roomsIndex = dbMgr.getRooms().indexOf(e.getRoom());
            int speakersIndex = dbMgr.getSpeakers().indexOf(e.getSpeaker());
            int sessionTimeIndex = dbMgr.getSessionTimes().indexOf(e.getSessionTime());
            System.out.print(String.format("    %1$03d  ", sessionId) + "  | ");
            System.out.print(String.format("%1$-82s", dbMgr.getWorkshops().get(workshopsIndex).getName() + " ("
                    + dbMgr.getWorkshops().get(workshopsIndex).getId() + ") ") + "| ");
            System.out.print(String.format("%1$-3s", dbMgr.getRooms().get(roomsIndex).getId()) + "  | ");
            System.out.print(String.format("%1$-15s", dbMgr.getSpeakers().get(speakersIndex).getName() + " ("
                    + dbMgr.getSpeakers().get(speakersIndex).getId() + ") ") + "| ");
            System.out.println(dbMgr.getSessionTimes().get(sessionTimeIndex).getTime() + " ("
                    + dbMgr.getSessionTimes().get(sessionTimeIndex).getId() + ")");
            ++sessionId;
        });
    }

    private void displayScheduleRooms(Schedule schedule) {
        System.out.println("\n> from 'room' perspective");
        System.out.println("    room id |   scheduled sessions [workshop id, room id, speaker id, sesion-time id]");
        IntStream.range(0, 118).forEach(i -> {
            System.out.print("-");
            if (i == 117)
                System.out.println();
        });
        ArrayList<Room> rooms = dbMgr.getRooms();
        for (int i = 0; i < rooms.size(); ++i) {
            ArrayList<Session> roomSessions = new ArrayList<Session>();
            for (int j = 0; j < schedule.getSessions().size(); ++j) {
                if (schedule.getSessions().get(j).getRoom().equals(rooms.get(i))) {
                    roomSessions.add(schedule.getSessions().get(j));
                }
            }
            System.out.println(String.format("      %1$-5s", rooms.get(i).getId()) + " | " + roomSessions);
        }
        System.out.println();
    }

    private void displayScheduleSpeakers(Schedule schedule) {
        System.out.println("\n> from 'speaker' perspective");
        System.out.println(
                "    speaker id  |   name    |   scheduled sessions [workshop id, room id, speaker id, sesion-time id]");
        IntStream.range(0, 118).forEach(i -> {
            System.out.print("-");
            if (i == 117)
                System.out.println();
        });
        ArrayList<Speaker> speakers = dbMgr.getSpeakers();
        for (int i = 0; i < speakers.size(); ++i) {
            ArrayList<Session> speakerSessions = new ArrayList<Session>();
            for (int j = 0; j < schedule.getSessions().size(); ++j) {
                if (schedule.getSessions().get(j).getSpeaker().equals(speakers.get(i))) {
                    speakerSessions.add(schedule.getSessions().get(j));
                }
            }
            System.out.println(String.format("      %1$-7s", speakers.get(i).getId()) + " | "
                    + String.format("   %1$-10s", speakers.get(i).getName()) + " | " + speakerSessions);
        }
        System.out.println();
    }

    private void displayScheduleSessionTimes(Schedule schedule) {
        System.out.println("\n> from 'session time' perspective");
        System.out.println("    session-time id |   time    | "
                + "scheduled sessions [workshop id, room id, speaker id, sesion-time id]");
        IntStream.range(0, 118).forEach(i -> {
            System.out.print("-");
            if (i == 117)
                System.out.println();
        });
        ArrayList<SessionTime> sessionTimes = dbMgr.getSessionTimes();
        for (int i = 0; i < sessionTimes.size(); ++i) {
            ArrayList<Session> sessionTimeSessions = new ArrayList<Session>();
            for (int j = 0; j < schedule.getSessions().size(); ++j) {
                if (schedule.getSessions().get(j).getSessionTime().equals(sessionTimes.get(i))) {
                    sessionTimeSessions.add(schedule.getSessions().get(j));
                }
            }
            System.out.println(String.format("      %1$-12s", sessionTimes.get(i).getId()) + " | "
                    + sessionTimes.get(i).getTime() + " | " + sessionTimeSessions);
        }
        System.out.println();
    }

    private void displayInitialData() {
        System.out.println("> Input data\n");
        displaySpeakers().displayWorkshops().displayRooms().displaySessionTimes();
    }

    private Driver displayWorkshops() {
        System.out.println("id    |" + String.format("%1$-74s", "workshop") + "|speaker");
        System.out.println("------+-----------------------------------+--------");
        dbMgr.getWorkshops().forEach(x -> System.out
                .println(x.getId() + "  |" + String.format("%1$-74s", x.getName()) + "|" + x.getSpeaker()));
        System.out.println();
        return this;
    }

    private Driver displayRooms() {
        System.out.println("id   |room");
        System.out.println("-----+----");
        dbMgr.getRooms().forEach(x -> System.out.println(x.getId() + "  |" + x.getName()));
        System.out.println();
        return this;
    }

    private Driver displaySpeakers() {
        System.out.println("id   |speaker");
        System.out.println("-----+--------");
        dbMgr.getSpeakers().forEach(x -> System.out.println(x.getId() + "  |" + x.getName()));
        System.out.println();
        return this;
    }

    private Driver displaySessionTimes() {
        System.out.println("id    |session time");
        System.out.println("------+------------");
        dbMgr.getSessionTimes().forEach(x -> System.out.println(x.getId() + "  |" + x.getTime()));
        System.out.println();
        return this;
    }
}
