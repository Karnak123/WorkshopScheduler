# WorkshopScheduler

This is a command line application built in Java to help build conflictless workshop schedules for events. The problem has been solved using Genetic Algorithms(GA) which are a subclass of the larger Evolutionary Algorithms(EA).

## Demo

A sample inital data has been included in `scheduler.db`. Run `Driver.java` to begin using the command line interface.

## Usage

Ways to input your own data have not been incorporated into the application. The steps to work with individual data are:-
1. Delete `scheduler.db`.
2. Edit `CreateDB01.java` to remove the default data and add your own data.
3. Run `DBMgr.java` to create a `scheduler.db` with your own data.
4. Run `Driver.java` to begin using the command line interface.
