package ga;

import java.util.ArrayList;
import java.util.stream.IntStream;

import db.DBMgr;

public class GeneticAlgorithm {
    private DBMgr dbMgr;

    public GeneticAlgorithm(DBMgr dbMgr) {
        this.dbMgr = dbMgr;
    }

    public Population evolve(Population population) {
        return mutatePopulation(crossoverPopulation(population));
    }

    Population crossoverPopulation(Population population) {
        Population crossoverPopulation = new Population(population.getSchedules().size(), dbMgr);
        IntStream.range(0, Driver.NUMB_OF_ELITE_SCHEDULES).forEach(e -> {
            crossoverPopulation.getSchedules().set(e, population.getSchedules().get(e));
        });
        IntStream.range(Driver.NUMB_OF_ELITE_SCHEDULES, population.getSchedules().size()).forEach(e -> {
            if (Driver.CROSSOVER_RATE > Math.random()) {
                Schedule schedule1 = selectTournamentPopulation(population).sortByFitness().getSchedules().get(0);
                Schedule schedule2 = selectTournamentPopulation(population).sortByFitness().getSchedules().get(0);
                crossoverPopulation.getSchedules().set(e, crossoverSchedule(schedule1, schedule2));
            } else {
                crossoverPopulation.getSchedules().set(e, population.getSchedules().get(e));
            }
        });
        return crossoverPopulation;
    }

    Schedule crossoverSchedule(Schedule schedule1, Schedule schedule2) {
        Schedule crossoverSchedule = new Schedule(dbMgr).initialize();
        IntStream.range(0, crossoverSchedule.getSessions().size()).forEach(e -> {
            if (Math.random() > 0.5)
                crossoverSchedule.getSessions().set(e, schedule1.getSessions().get(e));
            else
                crossoverSchedule.getSessions().set(e, schedule2.getSessions().get(e));
        });
        return crossoverSchedule;
    }

    Population mutatePopulation(Population population) {
        Population mutatePopulation = new Population(population.getSchedules().size(), dbMgr);
        ArrayList<Schedule> schedules = mutatePopulation.getSchedules();
        IntStream.range(0, Driver.NUMB_OF_ELITE_SCHEDULES)
                .forEach(e -> schedules.set(e, population.getSchedules().get(e)));
        IntStream.range(Driver.NUMB_OF_ELITE_SCHEDULES, population.getSchedules().size()).forEach(e -> {
            schedules.set(e, mutateSchedule(population.getSchedules().get(e)));
        });
        return mutatePopulation;
    }

    Schedule mutateSchedule(Schedule mutateSchedule) {
        Schedule schedule = new Schedule(dbMgr).initialize();
        IntStream.range(0, mutateSchedule.getSessions().size()).forEach(e -> {
            if (Driver.MUTATION_RATE > Math.random()) {
                mutateSchedule.getSessions().set(e, schedule.getSessions().get(e));
            }
        });
        return mutateSchedule;
    }

    Population selectTournamentPopulation(Population population) {
        Population tournamentPopulation = new Population(Driver.TOURNAMENT_SELECTION_SIZE, dbMgr);
        IntStream.range(0, Driver.TOURNAMENT_SELECTION_SIZE).forEach(e -> {
            tournamentPopulation.getSchedules().set(e,
                    population.getSchedules().get((int) (Math.random() * population.getSchedules().size())));
        });
        return tournamentPopulation;
    }
}
